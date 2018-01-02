drop table if exists temp_membership ;
 
select cast(concat(:activityMonth,'01') as date) into  @activityDate ;

create temporary table temp_membership  as
select  trim(substring_index(c2m.Membername,',',1)) as lastname,trim(substring_index(c2m.Membername,',',-1)) as firstname,
lg.gender_id sex, c2m.county , -- lc.code county,
 dob,
case when c2m.status = 'ENR' then 1
     when c2m.status = 'DIS' then 3
     else  2 
	end as status,
c2m.MCDMCR MCDMCR,
:fileId fileId,
:insId ins_id,
  MEMBEREFFDT,
  MEMBERTERMDT,
 PROVEFFDT  eff_start_date,
PROVTERMDT  eff_end_date,
PRPRNPI ,
 SBSB_ID,
phone,
address1,
address2,
city,
zip,
groupp,
class,
PRODUCT,
PLAN,
state,
now() created_date,
now() updated_date,
:username created_by,
:username updated_by
  from  csv2table_amg_roster c2m
join lu_gender lg on lg.code = c2m.sex
-- left outer join lu_county  lc on lc.description = c2m.county
-- left outer join lu_county_zip  lcz on  lcz.countycode = lc.code and lcz.zipcode = c2m.zip
;
 

alter table temp_membership add key MCDMCR(MCDMCR);
alter table temp_membership add key SBSB_ID(SBSB_ID);
alter table temp_membership add key eff_start_date(eff_start_date);
alter table temp_membership add key eff_end_date(eff_end_date);
alter table temp_membership add key phone(phone);
alter table temp_membership add key MEMBEREFFDT(MEMBEREFFDT);
alter table temp_membership add key MEMBERTERMDT(MEMBERTERMDT);
alter table temp_membership add key address(address1,address2,city,zip,fileId);
 
                
  update membership m
  join 
   (
 select m.mbr_id,m.SRC_SYS_MBR_NBR,tm.*  from   temp_membership tm
  join membership m on  tm.SBSB_ID=m.SRC_SYS_MBR_NBR
  group by tm.SBSB_ID
   having max(cast(MEMBEREFFDT as date))
 ) a on a.mbr_id = m.mbr_id
 set m.mbr_status= a.status,
   m.Mbr_MedicaidNo = a.MCDMCR,
   m.Mbr_FirstName = a.firstname,
   m.Mbr_lastName = a.lastname,
   m.Mbr_dob = a.dob ;
   
  
insert ignore into membership (  Mbr_LastName,Mbr_FirstName,Mbr_GenderID,Mbr_CountyCode,Mbr_DOB,Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,file_id,created_date,updated_date,created_by,updated_by)
select lastname,firstname, sex,county, 
case when tm.dob    > current_date   then  DATE_SUB( tm.dob ,INTERVAL 100 YEAR)   else  tm.dob  end  dob ,
tm.status,tm.SBSB_ID,tm.MCDMCR, tm.fileId ,tm.created_date,tm.updated_date,tm.created_by,tm.updated_by   from  temp_membership tm
LEFT join membership m on  m.SRC_SYS_MBR_NBR=tm.SBSB_ID 
where m.Mbr_id is null
group by tm.SBSB_ID;


update  membership_insurance mi
join membership m on m.Mbr_Id =mi.mbr_id
join temp_membership  tm on  tm.SBSB_ID=m.SRC_SYS_MBR_NBR and  tm.MEMBEREFFDT <= effective_strt_dt and MEMBERTERMDT>= mi.effecctive_end_dt 
set  mi.New_Medicare_Bene_Medicaid_Flag = case when tm.Status =1 then 'Y' else 'N' end ,
effecctive_end_dt= tm.MEMBERTERMDT ,
mi.product = tm.PRODUCT,
mi.product_label= tm.PRODUCT,
mi.groupp = tm.groupp,
mi.class= tm.class,
mi.planID = tm.PLAN
where m.SRC_SYS_MBR_NBR is not null and mi.effective_strt_dt is not null;



drop table if exists temp_cur_activity_month;
create table temp_cur_activity_month as
select   @activityDate activitydate,:activityMonth  activityMonth;

  
insert  into membership_insurance 
(
ins_id,mbr_id,New_Medicare_Bene_Medicaid_Flag,activitydate,activityMonth,effective_strt_dt,effecctive_end_dt,
product,product_label,planID,SRC_SYS_MBR_NBR,groupp,class,risk_flag,file_id,created_date,updated_date,created_by,updated_by)
select  
tm.ins_id,
m.mbr_id,
case when m.Mbr_Status =1 then 'Y' else 'N' end as new_benefits,
tcam.activitydate,
tcam.activityMonth,
tm.MEMBEREFFDT ,
tm.MEMBERTERMDT    ,
tm.PRODUCT,
tm.PRODUCT,
tm.PLAN,
tm.SBSB_ID,
tm.groupp,
tm.class,
'N' risk_flag,
 tm.fileId,
now() created_date,
now() updated_date,
:username created_by,
:username updated_by
 from temp_membership tm  
  join membership m on tm.SBSB_ID=m.SRC_SYS_MBR_NBR
  join temp_cur_activity_month tcam 
  left join membership_insurance mi on mi.mbr_id =  m.mbr_id and  tm.MEMBEREFFDT <= effective_strt_dt and MEMBERTERMDT>= mi.effecctive_end_dt 
  where   case when mi.mbr_id is not  null then effective_strt_dt is null else mi.mbr_id is   null end
  group by tm.SBSB_ID   ,tm.MEMBEREFFDT, tm.PRODUCT,tm.PLAN;
 
 
 update  membership_insurance set active_ind='N' where effecctive_end_dt <= @activityDate;
 
  update  membership_insurance set active_ind='Y' -- , effecctive_end_dt =null
  where effecctive_end_dt > @activityDate;

 
--  update membership_insurance  mi
-- join 
-- (select  
 
-- (select min( effective_strt_dt) from membership_insurance  where mbr_id=  mi.mbr_id and active_ind='Y' and (  mi.effecctive_end_dt is null or effecctive_end_dt > cast(concat(:activityMonth,'01')  as date)) group  by mbr_id) stdate,
--  ((select max( effective_strt_dt) from membership_insurance where mbr_id=  mi.mbr_id  and active_ind='Y' and mi.effecctive_end_dt is null group  by mbr_id) - INTERVAL 1 DAY )enddate,
-- mbr_id,
-- effective_strt_dt,
-- effecctive_end_dt
 --   from membership_insurance mi where active_ind='Y' and mi.effecctive_end_dt is null and mbr_id in 
-- (select   mbr_id from membership_insurance where   active_ind='Y' group by mbr_id  having count(ins_id)>1)
-- order by mbr_id,effective_strt_dt
-- )a   on mi.mbr_id =a.mbr_id
--        and mi.effective_strt_dt = stdate
--        set mi.effecctive_end_dt =a.enddate,  active_ind='N'
--        where  mi.effecctive_end_dt is null;
       
-- update  membership_provider mp 
-- join (
 -- select mp.mbr_id,mp.prvdr_id, tm.eff_start_date,
 -- tm.eff_end_date from  membership_provider mp 
-- join  membership  m on  mp.mbr_id = m.mbr_id 
-- join temp_membership tm  on  tm.SBSB_ID  =m.SRC_SYS_MBR_NBR
-- join  provider   d  on tm.PRPRNPI  =   d.code  and d.prvdr_id = mp.prvdr_id    
-- where 	  tm.eff_end_date is not null and  tm.eff_end_date != ''
-- group by mp.mbr_id,mp.prvdr_id,tm.eff_start_date
-- ) a on mp.mbr_id = a.mbr_id and mp.prvdr_id= a.prvdr_id and mp.eff_start_date =a.eff_start_date
-- set  mp.eff_end_date = a.eff_end_date ;
 

-- update  membership_provider mp 
-- left join (
--  	select tm.SBSB_ID,mp.mbr_id,mp.prvdr_id, tm.eff_start_date
--   		from  membership_provider mp 
 -- 		join  (select * from membership_insurance group by SRC_SYS_MBR_NBR) mi on  mp.mbr_id = mi.mbr_id 
--  		join temp_membership tm  on  tm.SBSB_ID  =mi.SRC_SYS_MBR_NBR
 -- 		join provider  p  on tm.PRPRNPI  =  p.code  and p.prvdr_id = mp.prvdr_id    
-- 		where 	  mp.active_ind='Y' and mi.active_ind='Y' and tm.eff_start_date between  mp.eff_start_date and  ifnull(mp.eff_end_date,'2099-12-31')
-- 		group by mp.mbr_id,mp.prvdr_id 
-- ) missingRoster  on mp.mbr_id = missingRoster.mbr_id and mp.prvdr_id=missingRoster.prvdr_id 
-- set  mp.eff_end_date = case when missingRoster.eff_start_date is not null then missingRoster.eff_start_date else  date_sub(date_format (concat(:activityMonth,'01') ,'%Y-%m-%d'), interval 1 day) end
-- where case when  missingRoster.SBSB_ID is not   null then missingRoster.prvdr_id  is null else missingRoster.SBSB_ID is   null end and mp.active_ind='Y';
 
-- update  membership_provider mp 
--   join (
--  	select tm.SBSB_ID,mp.mbr_id,mp.prvdr_id, tm.eff_start_date
--   		from  membership_provider mp 
 -- 		join  (select * from membership_insurance group by SRC_SYS_MBR_NBR) mi on  mp.mbr_id = mi.mbr_id 
--  		join temp_membership tm  on  tm.SBSB_ID  =mi.SRC_SYS_MBR_NBR
 -- 		join provider  p  on tm.PRPRNPI  =  p.code  and p.prvdr_id = mp.prvdr_id    
-- 		where 	  tm.eff_start_date between  mp.eff_start_date and  ifnull(mp.eff_end_date,'2099-12-31')
-- 		group by mp.mbr_id,mp.prvdr_id 
-- ) missingRoster  on mp.mbr_id = missingRoster.mbr_id and mp.prvdr_id=missingRoster.prvdr_id 
-- set  mp.eff_end_date = case when missingRoster.eff_start_date is not null then missingRoster.eff_start_date else  date_sub(date_format (concat(:activityMonth,'01') ,'%Y-%m-%d'), interval 1 day) end
-- where case when  missingRoster.SBSB_ID is not   null then missingRoster.prvdr_id  is null else missingRoster.SBSB_ID is   null end and mp.active_ind='Y';
 
insert into membership_provider (mbr_id,prvdr_id,file_id,eff_start_date,eff_end_date,created_date,updated_date,created_by,updated_by) 
select  
  m.mbr_id, 
 d.prvdr_id,
tm.fileid,
tm.eff_start_date,
tm.eff_end_date,
 now() created_date,
 now() updated_date,
 :username created_by,
:username updated_by
 from temp_membership tm
 join  provider   d  on  d.code =tm.PRPRNPI 
 join membership m on m.SRC_SYS_MBR_NBR = tm.SBSB_ID 
 left outer join membership_provider mp on mp.prvdr_id= d.prvdr_id and mp.mbr_id=m.mbr_id and mp.active_ind='Y'  and   tm.eff_start_date <= mp.eff_start_date and ifnull(tm.eff_end_date,'2099-12-31')
   >= ifnull(mp.eff_end_date,'2099-12-31')
 where case when mp.mbr_id is not null then mp.prvdr_id is null  else mp.mbr_id is  null  end 
group by tm.MCDMCR,tm.PRPRNPI,tm.eff_start_date; 

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
join temp_membership tm on  m.SRC_SYS_MBR_NBR=tm.SBSB_ID   
set mam.is_roster='Y';


   
insert  into contact (home_phone,mobile_phone,address1,address2,city,zipcode,statecode,file_id,created_Date,updated_date,created_by,updated_by)
select
 tm.phone,
tm.phone,
tm.address1,
tm.address2,
tm.city,
 tm.zip,
 d.statecode,
tm.fileId ,
now() created_date,
now() updated_date,
:username created_by,
:username updated_by
 from temp_membership tm
join  membership m on  m.SRC_SYS_MBR_NBR= tm.SBSB_ID 
 join lu_state_zip d on  d.zipcode = tm.zip
 join lu_state e on e.shot_name =  tm.state and  e.code=d.statecode
   ON DUPLICATE KEY UPDATE home_phone= tm.phone , mobile_phone=tm.phone ,  address1=tm.address1
                , address2=tm.address2 , city =tm.city , zipcode= tm.zip and contact.file_id =tm.fileId;

 
insert ignore into reference_contacts (Mbr_id,cnt_id) 
select m.Mbr_Id, c.cnt_id
 from  temp_membership tm
join  membership  m  on tm.SBSB_ID =m.SRC_SYS_MBR_NBR
join contact c on c.home_phone= tm.phone and c.mobile_phone=tm.phone and  c.address1=tm.address1
                and c.address2=tm.address2 and c.city =tm.city and c.zipcode= tm.zip and c.file_id =tm.fileId
left join reference_contacts rc on rc.cnt_id = c.cnt_Id
where rc.cnt_id is null;
  
               

drop table if exists temp_membership  ;

