 
drop table if exists temp_simply_membership ;

select cast(concat(:activityMonth,'01') as date) into  @activityDate ;


create temporary table temp_simply_membership  
select  PCP_Name ,  Program,
 case when Member_Term is not null and Member_Term != '' then 3
	  when Member_PCP_Status = 'Current'  then 2
      when Member_Eff = PCP_Eff  then 1
      else 2 end mbrstatus , 
      trim(substring_index(Member_Name,',',1)) as lastname,trim(substring_index(Member_Name,',',-1)) as firstname, 
ID mcdmcr, g.gender_id,   dob,  Member_Eff memeffstartdate ,
case when Member_Term is not null then  Member_Term 
     else '12/31/2099' end  memeffenddate,
PCP_Eff pcpstartdate,
PCP_Term pcpenddate, 
:insId insId,
:fileId fileId, now() created_date, now() updated_date, :username created_by, :username updated_by ,
 Phone phone, Member_County MemberCounty ,   REPLACE(upper(SUBSTRING(Member_Physical__Address, 1, LOCATE(SUBSTRING_INDEX(Member_Physical__Address, ',', -3),Member_Physical__Address)-2)), 'TEMPLE', '') address1, REPLACE(upper(SUBSTRING_INDEX(SUBSTRING_INDEX(Member_Physical__Address, ',', -3), ',', 1)), 'TERRACE', 'TEMPLE TERRACE') city,   trim(SUBSTRING_INDEX(SUBSTRING_INDEX(Member_Physical__Address, ',', -2), ',', 1)) state, 
 SUBSTRING(  SUBSTRING_INDEX(SUBSTRING_INDEX(Member_Physical__Address, ',', -1), ',', -1), 1,6) zipcode from csv2table_simply_roster
 join lu_gender g on g.code = sex;

 
alter table temp_simply_membership add key MCDMCR(MCDMCR);
alter table temp_simply_membership add key eff_start_date(memeffstartdate);
alter table temp_simply_membership add key eff_end_date(memeffenddate);
alter table temp_simply_membership add key MEMBEREFFDT(pcpstartdate);
alter table temp_simply_membership add key MEMBERTERMDT(pcpenddate);
alter table temp_simply_membership add key address(state,zipcode);
  
  
replace into membership (  Mbr_LastName,Mbr_FirstName,Mbr_GenderID,Mbr_CountyCode,Mbr_DOB,Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,file_id,created_date,updated_date,created_by,updated_by)
select lastname,firstname, gender_id,MemberCounty, 
case when tm.dob    > current_date   then  DATE_SUB( tm.dob ,INTERVAL 100 YEAR)   else  tm.dob  end  dob ,
tm.mbrstatus,tm.MCDMCR,tm.MCDMCR, tm.fileId ,tm.created_date,tm.updated_date,tm.created_by,tm.updated_by   from  temp_simply_membership tm
LEFT join membership m on  m.SRC_SYS_MBR_NBR=tm.MCDMCR 
where m.Mbr_id is null
group by tm.MCDMCR;



update  membership_insurance mi
join membership m on m.Mbr_Id =mi.mbr_id
join temp_simply_membership  tm on  tm.MCDMCR=m.SRC_SYS_MBR_NBR and  STRING_TO_DATE(tm.memeffstartdate) <= effective_strt_dt and STRING_TO_DATE(memeffenddate) >= mi.effecctive_end_dt 
set  mi.New_Medicare_Bene_Medicaid_Flag = case when tm.mbrstatus =1 then 'Y' else 'N' end ,
effecctive_end_dt= STRING_TO_DATE(tm.memeffenddate),
mi.product = tm.program,
mi.product_label= tm.program,
mi.planID = tm.program
where m.SRC_SYS_MBR_NBR is not null and mi.effective_strt_dt is not null;



drop table if exists temp_cur_activity_month;
create table temp_cur_activity_month as
select   @activityDate activitydate,:activityMonth  activityMonth;
 
insert  into membership_insurance 
(
ins_id,mbr_id,New_Medicare_Bene_Medicaid_Flag,activitydate,activityMonth,effective_strt_dt,effecctive_end_dt,
product,product_label,planID,SRC_SYS_MBR_NBR,groupp,class,risk_flag,file_id,created_date,updated_date,created_by,updated_by)
select  
:insId,
m.mbr_id,
case when m.Mbr_Status =1 then 'Y' else 'N' end as new_benefits,
tcam.activitydate,
tcam.activityMonth,
STRING_TO_DATE(tm.memeffstartdate) ,
STRING_TO_DATE(tm.memeffenddate)    ,
tm.program,
tm.program,
tm.program,
tm.MCDMCR,
null,
null,
'N' risk_flag,
 tm.fileId,
tm.created_date,
tm.updated_date,
tm.created_by,
tm.updated_by
 from temp_simply_membership tm  
  join membership m on tm.MCDMCR=m.SRC_SYS_MBR_NBR
  join temp_cur_activity_month tcam 
  left join membership_insurance mi on mi.mbr_id =  m.mbr_id and  STRING_TO_DATE(tm.memeffstartdate) <= effective_strt_dt and STRING_TO_DATE(memeffenddate) >= mi.effecctive_end_dt 
  where   case when mi.mbr_id is not  null then effective_strt_dt is null else mi.mbr_id is   null end
  group by tm.MCDMCR   ,STRING_TO_DATE(tm.memeffstartdate), tm.program ;
 
 
 update  membership_insurance set active_ind='N' where effecctive_end_dt <= @activityDate;
 
 update  membership_insurance set active_ind='Y'  where effecctive_end_dt > @activityDate;

 
insert into membership_provider (mbr_id,prvdr_id,file_id,eff_start_date,eff_end_date,created_date,updated_date,created_by,updated_by) 
select  
  m.mbr_id, 
 prvdr.prvdr_id,
tm.fileid,
tm.pcpstartdate,
tm.pcpenddate,
tm.created_date,
tm.updated_date,
tm.created_by,
tm.updated_by
 from temp_simply_membership tm
 join  provider   prvdr  on  prvdr.name =tm.PCP_Name 
 join membership m on m.SRC_SYS_MBR_NBR = tm.MCDMCR 
 left outer join membership_provider mp on mp.prvdr_id= prvdr.prvdr_id and mp.mbr_id=m.mbr_id and mp.active_ind='Y'  and   tm.pcpstartdate <= mp.eff_start_date and ifnull(tm.pcpenddate,'2099-12-31')   >= ifnull(mp.eff_end_date,'2099-12-31')
 where case when mp.mbr_id is not null then mp.prvdr_id is null  else mp.mbr_id is  null  end 
group by tm.MCDMCR,tm.PCP_Name,tm.pcpstartdate; 

update  membership_provider set active_ind='N',file_id=:fileId where   eff_end_date < @activityDate;
 

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
select min(effective_strt_dt) strt_date from membership_insurance  mi where mi.ins_id = :insId
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
where m1<=@activityDate
order by m1;
alter table activity_month_span add key activitymonth(activitymonth);

 
insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster,file_id,created_date,updated_date,created_by,updated_by)
select  
mi.mbr_id,mi.ins_id,mp.prvdr_id,  ams.activityMonth,'Y', :fileId fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by 
from  membership  m  
join  membership_insurance mi on  m.mbr_id = mi.mbr_id and mi.ins_id= :insId 
join  membership_provider mp  on  mp.mbr_id = mi.mbr_id   
join  reference_contracts rc on rc.prvdr_id =mp.prvdr_id and rc.insurance_id = mi.ins_id
join contract c on c.contract_id=rc.contract_id
join  activity_month_span ams on  ams.activitymonth  between   DATE_FORMAT(mi.effective_strt_dt, '%Y%m') 		and  case when mi.effecctive_end_dt is not null then  DATE_FORMAT(mi.effecctive_end_dt , '%Y%m') else :activityMonth end
							 	 and  ams.activitymonth between  DATE_FORMAT(c.start_date, '%Y%m')   and  case when c.end_date is not null  then DATE_FORMAT(c.end_date, '%Y%m') else  :activityMonth end
                                 and  ams.activitymonth between  DATE_FORMAT(mp.eff_start_date, '%Y%m')   and  case when mp.eff_end_date is not null  then DATE_FORMAT(mp.eff_end_date, '%Y%m') else  :activityMonth end
  left outer join membership_activity_month mam on mam.mbr_id=mi.mbr_id and mam.prvdr_id =mp.prvdr_id and mam.ins_id= mi.ins_id  and mam.activity_month=ams.activityMonth
  where    mam.activity_month is null
group by mi.mbr_id,mi.ins_id,mp.prvdr_id,ams.activityMonth; 



update membership_activity_month mam
join  membership m on m.mbr_id = mam.mbr_id and mam.ins_id =:insId and mam.activity_month=:activityMonth
join temp_simply_membership tm on  m.SRC_SYS_MBR_NBR=tm.MCDMCR   
set mam.is_roster='Y';


drop table if exists mbr_contact_temp;

create temporary table mbr_contact_temp  as
  select
  m.mbr_id,
  tm.MCDMCR ,
 tm.phone home_phone,
tm.phone mobile_phone,
tm.address1,
 null address2,
tm.city,
 tm.zipcode,
 d.statecode,
tm.fileId ,
now() created_date,
now() updated_date,
:username created_by,
:username updated_by
  from temp_simply_membership tm
join  membership m on  m.SRC_SYS_MBR_NBR= tm.MCDMCR 
 join lu_state_zip d on  d.zipcode = tm.zipcode
 join lu_state e on e.shot_name =  tm.state  and  e.code=d.statecode
 left join reference_contacts rc on rc.mbr_id = m.mbr_id
 where  rc.mbr_id is null
 group by tm.MCDMCR  ;
  
insert   into contact (home_phone,mobile_phone,address1,address2,city,zipcode,statecode,file_id,created_Date,updated_date,created_by,updated_by)
 select    home_phone, mobile_phone,address1, null address2, city,zipcode,statecode,fileId , created_date, updated_date,created_by,updated_by 
 from mbr_contact_temp tmp_cnt
   ON DUPLICATE KEY UPDATE home_phone= tmp_cnt.home_phone , mobile_phone=tmp_cnt.mobile_phone ,  address1=tmp_cnt.address1,
                  city =tmp_cnt.city , zipcode= tmp_cnt.zipcode and contact.file_id =tmp_cnt.fileId;
 

replace into reference_contacts (Mbr_id,cnt_id) 
select tmp_cnt.Mbr_Id, c.cnt_id
 from  mbr_contact_temp tmp_cnt
join contact c on c.home_phone= tmp_cnt.home_phone and c.mobile_phone=tmp_cnt.mobile_phone and  c.address1=tmp_cnt.address1
                and c.city =tmp_cnt.city and c.zipcode= tmp_cnt.zipcode and c.file_id =tmp_cnt.fileId
left join reference_contacts rc on rc.cnt_id = c.cnt_Id
where rc.cnt_id is null
group by tmp_cnt.Mbr_Id;
  
               
 drop table if exists temp_simply_membership  ;
 drop table if exists mbr_contact_temp;

