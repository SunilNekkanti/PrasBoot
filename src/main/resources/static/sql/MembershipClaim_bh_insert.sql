  
   INSERT INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
  SELECT    substring_index(`MemberName`,',',1) lastname,substring_index(`MemberName`,',',-1) firstname, IF(ISNULL(lg.gender_id), 4, lg.gender_id),STRING_TO_DATE(MemDOB) dob, 4,  MedicaidNumber, '', 2 ,  now(),now(),'sarath','sarath','Y'  
  FROM csv2Table_BH_Claim1 csv2BHClaim1
 Left  join lu_gender lg on lg.code = csv2BHClaim1.Gender
 LEFT OUTER JOIN membership m on  m.Mbr_MedicaidNo  = csv2BHClaim1.MedicaidNumber  
 where   csv2BHClaim1.MedicaidNumber is not null and STRING_TO_DATE(MemDOB) is not null and m.Mbr_id is null  
 group by  csv2BHClaim1.MedicaidNumber ;
 
 
 
drop table if exists temp_member_claims;
create  temporary table temp_member_claims as
    select  csv2BhClaim.*, m.mbr_id, rc.prvdr_id ,
now() created_date, now() updated_date, 'sarath' created_by, 'sarath' updated_by, 'Y'active_ind, :fileId file_id
  FROM  csv2Table_BH_Claim1 csv2BhClaim  
LEFT JOIN membership m on     m.Mbr_MedicaidNo  = csv2BhClaim.MedicaidNumber 
LEFT OUTER JOIN contract c on INSTR(c.PCP_PROVIDER_NBR ,trim(csv2BhClaim.PCPID))
 LEFT OUTER JOIN reference_contract rc on  rc.insurance_id=:insId and    c.ref_contract_Id = rc.ref_contract_Id   
where  m.mbr_id is not null
group by ClaimNum, m.mbr_id,rc.prvdr_id;

 
INSERT INTO membership_claims
(
claim_id_number,mbr_id,prvdr_id,ins_id,report_month,Claim_Type,facility_type_code,bill_type_code,
frequency_type_code,bill_type,dischargestatus,
MemEnrollId,Diagnoses,product_label,product_lvl1,product_lvl2,product_lvl3,product_lvl4,product_lvl5,product_lvl6,product_lvl7,
market_lvl1,market_lvl2,market_lvl3,market_lvl4,market_lvl5,market_lvl6,market_lvl7,market_lvl8,
tin,dx_type_cd,proc_type_cd,created_date,updated_date,created_by,updated_by,active_ind,file_id,pcp_prov_Id
)
SELECT 
ClaimNum,csv2bhClaim.mbr_id,csv2bhClaim.prvdr_id, :insId, :reportMonth, csv2bhClaim.ClaimType,null,null,
null,csv2bhClaim.BILLTYPE,csv2bhClaim.patientstatus,null,
 replace(CONCAT_WS(',',NULLIF(diag1,'') , NULLIF(diag2,'') , NULLIF(diag3,''), NULLIF(diag4,'') ) ,'.','') diagnoses,
csv2bhClaim.PRODUCT_LABEL,csv2bhClaim.PRODUCT,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null, 
case when STRING_TO_DATE(csv2BhClaim.ServiceStart) >= '2015-10-01' then 'ICD10' else null end ,null,
  now(),now(),'sarath','sarath','Y', 1 ,csv2bhClaim.PCPID
 FROM temp_member_claims csv2bhClaim
LEFT OUTER JOIN membership_claims mc on mc.claim_id_number =  csv2bhClaim.ClaimNum and mc.mbr_id=csv2bhClaim.mbr_id  and mc.ins_id= :insId and mc.prvdr_id=csv2bhClaim.prvdr_id and report_month= :reportMonth
   WHERE  mc.claim_id_number is null and STRING_TO_DATE(MemDOB) is not null    
GROUP BY ClaimNum,csv2bhClaim.mbr_id, csv2bhClaim.prvdr_id;



update membership_claims  mcc
join   
(
SELECT ClaimNum,mbr_id, PCPID,GROUP_CONCAT(DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(t.Diagnoses, ',', sub0.aNum), ',', -1)) AS Diagnoses
FROM 
( SELECT  ClaimNum, mbr_id, PCPID, group_concat( distinct Diagnoses) Diagnoses
 from ( 
 SELECT   ClaimNum, mbr_id,PCPID,CONCAT_WS(',',NULLIF(diag1,'') , NULLIF(diag2,'') , NULLIF(diag3,''), NULLIF(diag4,'') )  Diagnoses
  FROM temp_member_claims csv2BhClaim
    union all
 select claim_id_number as ClaimNum,mbr_id,pcp_prov_Id as PCPID,Diagnoses  from membership_claims mc  where ins_id=:insId and report_month=:reportMonth
 )a 
 group by ClaimNum, mbr_id, PCPID)t
INNER JOIN
(
    SELECT 1 + units.i + tens.i * 10 AS aNum, units.i + tens.i * 10 AS aSubscript
    FROM (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units
    CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 ) tens
) sub0
  ON (1 + LENGTH(t.Diagnoses) - LENGTH(REPLACE(t.Diagnoses, ',', ''))) >= sub0.aNum
GROUP BY ClaimNum,mbr_id, PCPID

) a on mcc.claim_id_number =a.ClaimNum  and mcc.mbr_id=a.mbr_id and mcc.pcp_prov_Id=a.PCPID
set mcc.Diagnoses =a.Diagnoses;


 