   
 drop table if exists csv2Table_simply_Claim_temp;
 create temporary table csv2Table_simply_Claim_temp as
 select *   from csv2Table_simply_Claim group by MedicaidNumber;

 select :activityMonth into @reportMonth;

   INSERT INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
  SELECT    substring_index(`MemberName`,',',1) lastname,substring_index(`MemberName`,',',-1) firstname, IF(ISNULL(lg.gender_id), 4, lg.gender_id),STRING_TO_DATE(MemDOB) dob, 4, MedicaidNumber, MedicaidNumber, '', 1,  now(),now(),:username,:username,'Y'  
  FROM csv2table_simply_claim csv2BHClaim1
 Left  join lu_gender lg on lg.code = csv2BHClaim1.Gender
 LEFT OUTER JOIN membership m on  m.Mbr_MedicaidNo  = csv2BHClaim1.MedicaidNumber  
 where   m.Mbr_id is null  and product_label='Medicare'
 group by  csv2BHClaim1.MedicaidNumber ;
 
 
 
drop table if exists temp_member_claims;
create  temporary table temp_member_claims as
    select   csv2BhClaim.*, m.mbr_id, rc.prvdr_id ,
now() created_date, now() updated_date, :username created_by, :username updated_by, 'Y'active_ind, :fileId file_id
  FROM  csv2table_simply_claim csv2BhClaim  
  JOIN membership m on     m.Mbr_MedicaidNo  = csv2BhClaim.MedicaidNumber 
LEFT OUTER JOIN contract c on FIND_IN_SET(replace(csv2BhClaim.PCPID,' ',''), replace(c.PCP_PROVIDER_NBR,' ','')) 
 LEFT OUTER JOIN reference_contracts rc on  rc.insurance_id=:insId and    c.contract_Id = rc.contract_Id   
where  csv2BhClaim.product_label='Medicare' ;
 
 
replace INTO membership_claims
(
claim_id_number,mbr_id,prvdr_id,ins_id,report_month,Claim_Type,facility_type_code,bill_type_code,
frequency_type_code,bill_type,dischargestatus,
MemEnrollId,Diagnoses,product_label,product_lvl1,product_lvl2,product_lvl3,product_lvl4,product_lvl5,product_lvl6,product_lvl7,
market_lvl1,market_lvl2,market_lvl3,market_lvl4,market_lvl5,market_lvl6,market_lvl7,market_lvl8,
tin,dx_type_cd,proc_type_cd,created_date,updated_date,created_by,updated_by,active_ind,file_id,pcp_prov_Id
)
SELECT 
ClaimNum,csv2bhClaim.mbr_id,csv2bhClaim.prvdr_id, :insId, :activityMonth, csv2bhClaim.ClaimType,null,null,
null,csv2bhClaim.BILLTYPE,csv2bhClaim.patientstatus,EnrollID ,
 replace(CONCAT_WS(',',NULLIF(diag1,'') , NULLIF(diag2,'') , NULLIF(diag3,''), NULLIF(diag4,'') ) ,'.','') diagnoses,
csv2bhClaim.PRODUCT_LABEL,csv2bhClaim.PRODUCT,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null, 
case when STRING_TO_DATE(csv2BhClaim.ServiceStart) >= '2015-10-01' then 'ICD10' else null end ,null,
  now(),now(),:username,:username,'Y', :fileId ,csv2bhClaim.PCPID
  FROM temp_member_claims csv2bhClaim
  join membership m on m.Mbr_MedicaidNo  = csv2BHClaim.MedicaidNumber  
LEFT OUTER JOIN membership_claims mc on mc.claim_id_number =  csv2bhClaim.ClaimNum and mc.mbr_id=csv2bhClaim.mbr_id  and mc.ins_id= :insId and mc.prvdr_id=csv2bhClaim.prvdr_id and report_month= :activityMonth
   WHERE  mc.claim_id_number is null and STRING_TO_DATE(MemDOB) is not null    
GROUP BY csv2bhClaim.mbr_id,csv2bhClaim.ClaimType, ClaimNum ;

   
 
 
 