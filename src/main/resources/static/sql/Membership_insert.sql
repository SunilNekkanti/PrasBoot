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
   
  
insert  into membership (  Mbr_LastName,Mbr_FirstName,Mbr_GenderID,Mbr_CountyCode,Mbr_DOB,Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,file_id,created_date,updated_date,created_by,updated_by)
select * from (select lastname,firstname, sex,county, 
case when tm.dob    > current_date   then  DATE_SUB( tm.dob ,INTERVAL 100 YEAR)   else  tm.dob  end  dob ,
tm.status,tm.SBSB_ID,tm.MCDMCR, tm.fileId ,tm.created_date,tm.updated_date,tm.created_by,tm.updated_by   from  temp_membership tm
LEFT join membership m on  m.SRC_SYS_MBR_NBR=tm.SBSB_ID 
where m.Mbr_id is null
group by tm.SBSB_ID) x
ON DUPLICATE KEY UPDATE Mbr_LastName =lastname ,Mbr_FirstName= firstname,Mbr_DOB= dob,Mbr_Status= status;



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

  
insert ignore into membership_insurance 
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
 


 insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster,file_id,created_date,updated_date,created_by,updated_by)
  select  
 m.mbr_id,:insId,p.prvdr_id,  :activityMonth,'Y', :fileId fileId,
 now() created_date,now() updated_date,:username created_by,:username updated_by 
 from temp_membership tm
 join  membership  m    on m.SRC_SYS_MBR_NBR =tm.SBSB_ID 
 join provider p  on tm.PRPRNPI= p.code
 left outer join membership_activity_month mam on mam.mbr_id=m.mbr_id and mam.prvdr_id =p.prvdr_id and mam.ins_id= :insId  and mam.activity_month=:activityMonth
   where    mam.activity_month is null
 group by m.mbr_id ; 

 



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

