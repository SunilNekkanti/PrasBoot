
INSERT INTO hospital (
hos_id,name,file_id,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT DISTINCT null, Facility_Name, :fileId, now() created_date,now()updated_date,:username created_by,:username updated_by,'Y' activeInd FROM  
csv2table_amg_hospitalization mh
LEFT OUTER JOIN hospital  h on  h.name=mh.Facility_Name
WHERE h.name  IS NULL;

drop table if exists tmp_new_memberships;

create temporary table tmp_new_memberships
select :fileId fileId,csvAmgMbrHos.* from csv2table_amg_hospitalization csvAmgMbrHos
left join membership m on m.SRC_SYS_MBR_NBR = csvAmgMbrHos.Member_ID
where m.SRC_SYS_MBR_NBR is null;


INSERT ignore INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
 select  Patient_Last_Name,Patient_First_Name, lg.gender_id ,DOB, 1,Member_ID,Member_ID,null,:fileId fileId,  now() created_date,
 now() updated_date,:username created_by,:username updated_by,'Y' active_ind  
 from tmp_new_memberships csvAmgMbrHos
 join lu_gender lg on lg.code = Gender
 group by Member_ID;
 

insert  into contact (home_phone,mobile_phone,address1,address2,city,zipcode,statecode,file_id,created_Date,updated_date,created_by,updated_by)
select
 replace(tm.Phone_Number,'-',''),replace(tm.Phone_Number,'-',''),tm.Member_Street,null,tm.Member_City, tm.Member_Zip_Code, 
 d.statecode,tm.fileId ,now() created_date,
 now() updated_date, :username created_by,:username updated_by
  from tmp_new_memberships tm
 join  membership m on  m.SRC_SYS_MBR_NBR= tm.Member_ID 
 join lu_state_zip d on  d.zipcode = tm.Member_Zip_Code
 join lu_state e on e.shot_name =  tm.Member_State and  e.code=d.statecode
   ON DUPLICATE KEY UPDATE 
   home_phone= replace(tm.Phone_Number,'-','') , 
   mobile_phone=replace(tm.Phone_Number,'-','') ,  
   address1=tm.Member_Street , city =tm.Member_City ,
   zipcode= tm.Member_Zip_Code and contact.file_id =tm.fileId;


insert ignore into reference_contacts (Mbr_id,cnt_id) 
select m.Mbr_Id, c.cnt_id
from  tmp_new_memberships tm
join  membership  m  on tm.Member_ID =m.SRC_SYS_MBR_NBR
join contact c on c.home_phone= replace(tm.Phone_Number,'-','') and c.mobile_phone=replace(tm.Phone_Number,'-','') and  c.address1=tm.Member_Street
                 and c.city =tm.Member_City and c.zipcode= tm.Member_Zip_Code and c.file_id =tm.fileId
left join reference_contacts rc on rc.cnt_id = c.cnt_Id
where rc.cnt_id is null;

 insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster,is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  
  m.mbr_id, :insId ins_id, p.prvdr_id,  date_format(STRING_TO_DATE(ER_Visit_Date),'%Y%m') activityMonth,'N' isRoster, 'N' isCap
 , :fileId fileId, now() created_date,now() updated_date,:username created_by,:username updated_by 
from tmp_new_memberships csvAmgMbrHos
  join membership m on m.SRC_SYS_MBR_NBR = csvAmgMbrHos.Member_ID 
  join provider p on p.code = csvAmgMbrHos.Provider_NPI 
  left outer join membership_activity_month mam on mam.mbr_id=m.mbr_id and mam.prvdr_id =p.prvdr_id and mam.ins_id= :insId  and mam.activity_month=date_format(STRING_TO_DATE(ER_Visit_Date),'%Y%m')
  where      m.SRC_SYS_MBR_NBR is not null and  mam.activity_month is null
group by m.mbr_id,p.prvdr_id,activityMonth; 

 
 INSERT INTO membership_hospitalization
 (
 mbr_hos_id, process_date, mbr_id, ins_id, prvdr_id, Line_of_Business, Product, Home_Plan_Parent_Co, Home_Plan_Name, Hot_Spotter_Chronic, 
 Months_as_Hot_Spotter_Chronic, Hot_Spotter_Readmission, Risk_Drivers, Inpatient_Authorization, New_Attribution, Provider_Specialty, Organization, 
 Organization_Tin, Prospective_Risk_Score, Condition_based_Opportunities, CDOI, CDOI_High_Priority, Need_Annual_Visit, Last_Annual_Visit_Date,
  Health_Assessment, ER_Visits, ER_No_of_Visits, ER_Last_Visit_Date, ER_Visit_Date, Day_of_Week, Facility_Name, Potentially_Avoidable_ER_Visit,
  Primary_Diagnosis, Secondary_Diagnosis_1, Secondary_Diagnosis_2, Secondary_Diagnosis_3, file_id, created_date, updated_date, created_by, 
  updated_by, active_ind
 )

 SELECT
  null, cast(concat(:activityMonth,'01') as date) processDate,m.mbr_id,  mam.ins_id,mam.prvdr_id,
  Line_of_Business, Product, Home_Plan_Parent_Co, Home_Plan_Name, Hot_Spotter_Chronic, Months_as_Hot_Spotter_Chronic, Hot_Spotter_Readmission, 
  Risk_Drivers, Inpatient_Authorization, New_Attribution,  Provider_Specialty, Organization, Organization_Tin, Prospective_Risk_Score, Condition_based_Opportunities, CDOI, CDOI_High_Priority,
  Need_Annual_Visit, Last_Annual_Visit_Date, Health_Assessment, ER_Visits, ER_No_of_Visits, ER_Last_Visit_Date, ER_Visit_Date, Day_of_Week, 
  Facility_Name, Potentially_Avoidable_ER_Visit, Primary_Diagnosis, Secondary_Diagnosis_1, Secondary_Diagnosis_2, Secondary_Diagnosis_3,
   :fileId fileId, now() created_date, now() updated_date, :username created_by, :username updated_by, 'Y' active_ind

   FROM csv2table_amg_hospitalization csv2mh
   JOIN membership m on m.SRC_SYS_MBR_NBR  =  csv2mh.Member_ID
    JOIN membership_activity_month mam on mam.mbr_id  =  m.mbr_id and mam.activity_month = date_format(STRING_TO_DATE(ER_Visit_Date),'%Y%m')  
   GROUP BY Member_ID,ER_Visit_Date,Facility_Name,Potentially_Avoidable_ER_Visit,Primary_Diagnosis;
 

drop table if exists tmp_new_memberships;