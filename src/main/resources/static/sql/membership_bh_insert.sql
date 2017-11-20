drop table if exists temp_bh_membership ;
create temporary table temp_bh_membership  
select  PcpName , textbox44 pcpaddress1 , 
SUBSTRING_INDEX(SUBSTRING_INDEX(Pay2Mail, ',', 1), ',', -1) pcpcity, 
SUBSTRING_INDEX(SUBSTRING_INDEX(Pay2Mail, ' ', -2), ' ', 1) pcpstate, 
SUBSTRING_INDEX(SUBSTRING_INDEX(Pay2Mail, ' ', -1), ' ', -1) pcpzipcode, textbox192 status , textbox65 pcpstatus, 
REPLACE(textbox165, ',','') lastname,
textbox214 mcdmcr, textbox2 sex , STRING_TO_DATE(textbox9)   dob,   STRING_TO_DATE(textbox64) memeffstartdate ,
case when textbox192 = 'Termed Membership' then  textbox74 
     else '12/31/2099' end  memeffenddate,
case when textbox65 = 'PCP EFF' then textbox74 else textbox64 end  pcpstartdate,
case when textbox65 = 'PCP Term' then textbox74 end pcpenddate, 
 textbox81 phone, MemberCounty , textbox185 firstname, REPLACE(upper(SUBSTRING(textbox238, 1, LOCATE(SUBSTRING_INDEX(textbox238, ' ', -3),textbox238)-2)), 'TEMPLE', '') address1, REPLACE(upper(SUBSTRING_INDEX(SUBSTRING_INDEX(textbox238, ' ', -3), ' ', 1)), 'TERRACE', 'TEMPLE TERRACE') city, SUBSTRING_INDEX(SUBSTRING_INDEX(textbox238, ' ', -2), ' ', 1) state, 
 SUBSTRING(  SUBSTRING_INDEX(SUBSTRING_INDEX(textbox238, ' ', -1), ' ', -1), 1,5) zipcode from csv2table_bh_roster;

 alter table temp_bh_membership add key MCDMCR(MCDMCR);

insert ignore into membership (  Mbr_LastName,Mbr_FirstName,Mbr_GenderID,Mbr_CountyCode,Mbr_DOB,Mbr_Status,Mbr_MedicaidNo,file_id,created_date,updated_date,created_by,updated_by)
 select trim(lastname),trim(firstname) ,lg.gender_id,lc.code,
cast(  case when a.dob    > current_date   then  DATE_SUB( a.dob ,INTERVAL 100 YEAR)   else  a.dob  end  as date)  dob ,
 case when trim(a.status)='Current Membership' then 2
      when trim(a.status)='New Membership' then 1
      when trim(a.status)='Termed Membership' then 3 end status,
      convert(MCDMCR,unsigned),:fileId fileId,
      now() created_date,
      now() updated_date,'sarath' created_by,'sarath' updated_by 
 from temp_bh_membership a
 join lu_gender lg on lg.code=a.sex
 left outer join lu_county lc on ucase(trim(lc.description)) = ucase(trim(a.membercounty))
 left outer join lu_county_zip lcz on lcz.countycode=lc.code and lcz.zipcode=a.zipcode
 left outer join membership m on m.Mbr_MedicaidNo=a.MCDMCR
 where m.mbr_id is null  
 group by a.MCDMCR
 having max(memeffenddate) ;

 update membership m
 join temp_bh_membership tm on tm.mcdmcr =m.Mbr_MedicaidNo
 set m.mbr_status=  case when trim(tm.status)='Current Membership' then 2
      when trim(tm.status)='New Membership' then 1
      when trim(tm.status)='Termed Membership' then 3 end ;
 
update membership_insurance mi
 join membership m on mi.mbr_id= m.mbr_id
join temp_bh_membership  b on  b.mcdmcr =m.Mbr_MedicaidNo and  mi.effective_strt_dt =b.memeffstartdate
set  mi.New_Medicare_Bene_Medicaid_Flag = case when b.Status ='New Membership' then 'Y' else 'N' end ,
 mi.effecctive_end_dt =  STRING_TO_DATE(b.memeffenddate) 
where m.Mbr_MedicaidNo is not null and mi.effective_strt_dt is not null;


insert into membership_insurance 
(
ins_id,mbr_id,New_Medicare_Bene_Medicaid_Flag,activitydate,activityMonth,effective_strt_dt,effecctive_end_dt,
product,product_label,planID,SRC_SYS_MBR_NBR,risk_flag,file_Id,created_date,updated_date,created_by,updated_by)
select 
:insId ins_id,
a.mbr_id,
case when a.Mbr_Status =1 then 'Y' else 'N' end as new_benefits,
CAST(DATE_FORMAT(NOW() ,'%Y-%m-01') as DATE) activitydate,
DATE_FORMAT(NOW() ,'%m%y')  activityMonth,
STRING_TO_DATE(b.memeffstartdate) effective_strt_dt,
STRING_TO_DATE(b.memeffenddate)   effective_end_dt,
null PRODUCT,
null PRODUCT,
null PLAN,
null SBSB_ID,
'N' risk_flag,
:fileId fileId,
now() created_date,
now() updated_date,
'sarath' created_by,
'sarath' updated_by
  from temp_bh_membership  b  
  join membership   a on   a.mbr_medicaidNo=b.MCDMCR  
  left outer join membership_insurance mi on mi.mbr_id=a.mbr_id  and mi.effective_strt_dt =b.memeffstartdate
  where     case when mi.mbr_id is not  null then mi.effective_strt_dt is null else mi.mbr_id is  null end
 group by a.mbr_id, effective_strt_dt, PRODUCT, PLAN;


update  membership_insurance set active_ind='N' where effecctive_end_dt <= cast(now() as date);
update  membership_insurance set active_ind='Y', effecctive_end_dt = null where effecctive_end_dt > cast(now() as date);

  update membership_insurance  mi
join 
(select  
 
(select min( effective_strt_dt) from membership_insurance where mbr_id=  mi.mbr_id  and active_ind='Y' and mi.effecctive_end_dt is null  group  by mbr_id) stdate,
 ((select max( effective_strt_dt) from membership_insurance where mbr_id=  mi.mbr_id  and active_ind='Y' and mi.effecctive_end_dt is null group  by mbr_id) - INTERVAL 1 DAY )enddate,
mbr_id,
effective_strt_dt,
effecctive_end_dt
   from membership_insurance mi where active_ind='Y' and mi.effecctive_end_dt is null and mbr_id in 
(select   mbr_id from membership_insurance where   active_ind='Y' group by mbr_id  having count(ins_id)>1)
order by mbr_id,effective_strt_dt
)a   on mi.mbr_id =a.mbr_id
       and mi.effective_strt_dt = stdate
       set mi.effecctive_end_dt =a.enddate,  active_ind='N'
       where  mi.effecctive_end_dt is null;
       
update  membership_provider mp 
join (
	 select
 p.prvdr_id,
 m.mbr_id    ,  STRING_TO_DATE(b.pcpstartdate) eff_start_date
    ,   STRING_TO_DATE(b.pcpenddate) eff_end_date    
  from  membership m  
  join temp_bh_membership b on b.mcdmcr =m.mbr_medicaidNo
  join provider p on ucase(p.name) LIKE CONCAT('%',TRIM(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(ucase(PcpName), ',', 2), ',', 1),'.','')),'%') 
         and  ucase(p.name) LIKE CONCAT('%',TRIM(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(ucase(PcpName), ',', 2), ',', -1),'.','')),'%')   
where 	  b.pcpenddate is not null and  b.pcpenddate != '' 
group by m.mbr_id,p.prvdr_id,STRING_TO_DATE( b.pcpstartdate) 
) a on mp.mbr_id = a.mbr_id and mp.prvdr_id= a.prvdr_id and mp.eff_start_date =a.eff_start_date
set  mp.eff_end_date = a.eff_end_date 
where mp.active_ind='Y';

insert into membership_provider (mbr_id,prvdr_id,file_id,eff_start_date,eff_end_date,created_date,updated_date,created_by,updated_by)
select a.mbr_id,a.prvdr_id,:fileId file_id,a.eff_start_date,a.eff_end_date,now() created_date, now() updated_date, 'sarath' created_by,'sarath' updated_by
from 
(
select
 m.mbr_id, 
 p.prvdr_id, 
  STRING_TO_DATE(b.pcpstartdate) eff_start_date,
STRING_TO_DATE(b.pcpenddate) eff_end_date 
 
  from   temp_bh_membership b  
  join  membership m on m.mbr_medicaidNo=b.mcdmcr 
  join provider p on ucase(p.name) LIKE CONCAT('%',TRIM(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(ucase(PcpName), ',', 2), ',', 1),'.','')),'%') 
         and  ucase(p.name) LIKE CONCAT('%',TRIM(REPLACE(SUBSTRING_INDEX(SUBSTRING_INDEX(ucase(PcpName), ',', 2), ',', -1),'.','')),'%')  
)a 
  left outer join  membership_provider mp  on mp.prvdr_id = a.prvdr_id and mp.mbr_id =a.mbr_id
where 	case when mp.mbr_id is not null  then mp.prvdr_id is null else mp.mbr_id is   null end
group by a.mbr_id,a.prvdr_id,a.eff_start_date;

update  membership_provider set active_ind='N' where eff_end_date is not null and eff_end_date < cast(now() as date);


drop table if exists activity_month_span;
create temporary table activity_month_span as 
select 
DATE_FORMAT(m1, '%Y%m') as activitymonth

from
(
select 
(select min(strt_date)
from 
 (
select min(effective_strt_dt) strt_date from membership_insurance  mi where :insId =mi.ins_id
union 
select min(eff_start_date) strt_date  from membership_provider 
) a)
+INTERVAL m MONTH as m1
from
(
select @rownum\:=@rownum+1 as m from
(select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9 )  t1,
(select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2,
(select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3,
(select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4,
(select @rownum\:=-1) t0
) d1
) d2 
where m1<=cast(now() as date)
order by m1;


insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster,is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
mi.mbr_id,mi.ins_id,mp.prvdr_id,  ams.activityMonth,'Y','Y', :fileId fileId,
now() created_date,now() updated_date,'sarath' created_by,'sarath' updated_by  
from  membership  m  
join  membership_insurance mi on  m.mbr_id = mi.mbr_id and mi.ins_id=:insId 
join  membership_provider mp  on  mp.mbr_id = mi.mbr_id  
join  reference_contract rc on rc.prvdr_id =mp.prvdr_id and rc.insurance_id = :insId
join contract c on c.ref_contract_id=rc.ref_contract_id
join  activity_month_span ams on ams.activitymonth  between   DATE_FORMAT(mi.effective_strt_dt, '%Y%m') 		and  case when mi.effecctive_end_dt is not null then  DATE_FORMAT(mi.effecctive_end_dt , '%Y%m') else :activityMonth end
								 and  ams.activitymonth between  DATE_FORMAT(c.start_date, '%Y%m')   and  case when c.end_date is not null  then DATE_FORMAT(c.end_date, '%Y%m') else  :activityMonth end
                                 and  ams.activitymonth between  DATE_FORMAT(mp.eff_start_date, '%Y%m')   and  case when mp.eff_end_date is not null  then DATE_FORMAT(mp.eff_end_date, '%Y%m') else  :activityMonth end
left outer join membership_activity_month mam on mam.mbr_id=mi.mbr_id and mam.prvdr_id =mp.prvdr_id and mam.ins_id= mi.ins_id  and mam.activity_month=ams.activityMonth
where    mam.activity_month is null 
group by mi.mbr_id,mi.ins_id,mp.prvdr_id,  ams.activityMonth; 


insert ignore into reference_contact (mbr_id, created_date,updated_date,created_by,updated_by) 
select m.Mbr_Id ,now() created_date,now()updated_date, 'sarath','sarath' from membership m
left outer join reference_contact rc on rc.mbr_id =m.mbr_id
where rc.mbr_id is null ;

insert ignore into contact (ref_cnt_id,home_phone,mobile_phone,address1,address2,city,zipcode,statecode,file_id,created_Date,updated_date,created_by,updated_by)
select
c.ref_cnt_id,
a.phone,
a.phone,
a.address1,
null address2,
a.city,
a.zipcode,
d.statecode,
:fileId fileId ,
now() created_date,
now() updated_date,
'sarath',
'sarath'
from temp_bh_membership a
join membership b on  b.Mbr_MedicaidNo = a.mcdmcr
join reference_contact c on  c.mbr_id = b.mbr_id  
join lu_state_zip d on  d.zipcode = a.zipcode
join lu_state e on e.shot_name =  a.state and  e.code=d.statecode
left outer join contact cnt on cnt.ref_cnt_id = c.ref_cnt_id
where cnt.ref_cnt_id is null
group by b.Mbr_id; 
