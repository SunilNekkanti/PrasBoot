
 INSERT INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
 SELECT   substring_index(`MemFullName`,',',1) lastname,substring_index(`MemFullName`,',',-1) firstname, 4 ,STRING_TO_DATE(MemDOB) dob, 4,  MedicaidId, '',1,  now(),now(),'sarath','sarath','Y'  
  FROM csv2Table_BH_Claim csv2BHClaim
 LEFT OUTER JOIN membership m on   ( m.Mbr_LastName=substring_index(`MemFullName`,',',1) and m.Mbr_FirstName=substring_index(`MemFullName`,',',-1) and m.Mbr_DOB=STRING_TO_DATE(MemDOB))
 LEFT OUTER JOIN membership m2 on m2.Mbr_MedicaidNo  = csv2BHClaim.MedicaidId  
 where m.mbr_id is null and  m2.mbr_id is null group by lastname, firstname,dob;
 
 
 drop table if exists temp_csv2Table_BH_Claim;
create temporary table  temp_csv2Table_BH_Claim   as 
select      substring_index(`MemFullName`,',',1)  lastname, substring_index(`MemFullName`,',',-1)  firstname ,STRING_TO_DATE(MemDOB) dob,
csv2BhClaim.*
from csv2Table_BH_Claim csv2BhClaim;
alter table temp_csv2Table_BH_Claim add key  MedicaidId(MedicaidId) , add key lastname(lastname), add key firstname(firstname), add key  dob(dob),add key ClaimId(ClaimId)
, add key PCPAffiliationId(PCPAffiliationId);


drop table if exists temp_member_claims;
create  temporary table temp_member_claims as
    select  csv2BhClaim.*, m.mbr_id
  FROM temp_csv2Table_BH_Claim csv2BhClaim  
LEFT JOIN membership m on    if(nullif(csv2BhClaim.MedicaidId,'') is null  , m.Mbr_LastName=lastname and m.Mbr_FirstName=firstname and m.Mbr_DOB=dob ,m.Mbr_MedicaidNo  = csv2BhClaim.MedicaidId)
where  m.mbr_id is not null
group by ClaimId,m.mbr_id;


drop table if exists temp_mbr_prvdr_claims;
 
 create temporary table temp_mbr_prvdr_claims as 
select    csv2BhClaim.* , rc.prvdr_id ,
replace(group_concat( distinct replace(csv2BhClaim.Diagnoses,'.','')),'|',',') diagnoses1, null product_label, null product_lvl1, null product_lvl2, null market_lvl1, 
null product_lvl4, null product_lvl5, null product_lvl6, null product_lvl7, null product_lvl8, 
null market_lvl2, null market_lvl3, null market_lvl4, null market_lvl5, null market_lvl6,
null market_lvl7, null market_lvl8, null tin,'ICD10', null proc_type_cd,
now() created_date, now() updated_date, 'sarath' created_by, 'sarath' updated_by, 'Y'active_ind, 1 file_id,csv2BhClaim.PCPAffiliationId as  PCPAffiliationId1
  FROM temp_member_claims csv2BhClaim 
 LEFT OUTER JOIN contract c on INSTR(c.PCP_PROVIDER_NBR ,trim(csv2BhClaim.PCPAffiliationId))
 LEFT OUTER JOIN reference_contract rc on  rc.insurance_id=1 and    c.ref_contract_Id = rc.ref_contract_Id     
GROUP BY ClaimId, csv2BhClaim.mbr_id   ;



 INSERT INTO membership_claims
(
claim_id_number, mbr_id, prvdr_id, ins_id, report_month,claim_type,
facility_type_code, bill_type_code, frequency_type_code, bill_type,  dischargestatus, MemEnrollId,
Diagnoses, product_label, product_lvl1, product_lvl2, product_lvl3, 
product_lvl4, product_lvl5, product_lvl6, product_lvl7, market_lvl1,
market_lvl2, market_lvl3, market_lvl4, market_lvl5, market_lvl6,
market_lvl7, market_lvl8, tin, dx_type_cd, proc_type_cd,
created_date, updated_date, created_by, updated_by, active_ind, file_id,pcp_prov_Id
)

 SELECT  ClaimId, csv2BhClaim.mbr_id,csv2BhClaim.prvdr_id,:insId, :reportMonth report_month, csv2BhClaim.Triangles,
NULLIF(lft.code,''), NULLIF(lbt.code,''), NULLIF(csv2BhClaim.FrequencyCode,''), csv2BhClaim.BillClassCode,  null dischargestatus, csv2BhClaim.MemEnrollId,
replace(group_concat( distinct replace(csv2BhClaim.Diagnoses,'.','')),'|',',') diagnoses, null product_label, null product_lvl1, null product_lvl2, null market_lvl1, 
null product_lvl4, null product_lvl5, null product_lvl6, null product_lvl7, null product_lvl8, 
null market_lvl2, null market_lvl3, null market_lvl4, null market_lvl5, null market_lvl6,
null market_lvl7, null market_lvl8, null tin,'ICD10', null proc_type_cd,
now() created, now()updated, 'sarath' createdby, 'sarath' updatedby, 'Y' activeInd, :fileId fileid,csv2BhClaim.PCPAffiliationId

  FROM temp_mbr_prvdr_claims csv2BhClaim 
 LEFT  JOIN lu_facility_type lft on lft.code = csv2BhClaim.FacilityCode
LEFT  JOIN lu_bill_type lbt on lbt.description = csv2BhClaim.BillClassCode 
 LEFT  JOIN membership_claims mc on  mc.claim_id_number = ClaimId  and report_month= :reportMonth
 WHERE    mc.claim_id_number is null 
GROUP BY ClaimId, csv2BhClaim.mbr_id is not null;


INSERT INTO membership_claims
(
claim_id_number,mbr_id,prvdr_id,ins_id,report_month,Claim_Type,facility_type_code,bill_type_code,
frequency_type_code,bill_type,dischargestatus,
MemEnrollId,Diagnoses,product_label,product_lvl1,product_lvl2,product_lvl3,product_lvl4,product_lvl5,product_lvl6,product_lvl7,
market_lvl1,market_lvl2,market_lvl3,market_lvl4,market_lvl5,market_lvl6,market_lvl7,market_lvl8,
tin,dx_type_cd,proc_type_cd,created_date,updated_date,created_by,updated_by,active_ind,file_id,pcp_prov_Id
)
SELECT 
ClaimNum,m.mbr_id,mp.prvdr_id,:insId, :reportMonth, csv2bhClaim.ClaimType,null,null,
null,csv2bhClaim.BILLTYPE,csv2bhClaim.patientstatus,null,
 replace(CONCAT_WS(',',NULLIF(diag1,'') , NULLIF(diag2,'') , NULLIF(diag3,''), NULLIF(diag4,'') ) ,'.','') diagnoses,
csv2bhClaim.PRODUCT_LABEL,csv2bhClaim.PRODUCT,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null, 
case when STRING_TO_DATE(csv2BhClaim.ServiceStart) >= '2015-10-01' then 'ICD10' else null end ,null,
  now(),now(),'sarath','sarath','Y', 1 ,csv2bhClaim.PCPID
   FROM csv2Table_bh_Claim1 csv2bhClaim
  JOIN membership  m on  csv2bhClaim.MedicaidNumber != ''  and m.Mbr_MedicaidNo  =  csv2bhClaim.MedicaidNumber  and MemDOB is not null
 LEFT OUTER JOIN contract c on trim(c.PCP_PROVIDER_NBR) = trim(csv2BhClaim.PCPID)
 LEFT OUTER JOIN provider prvdr on trim(prvdr.code)  =  trim(csv2BhClaim.PCPID)
 LEFT OUTER JOIN reference_contract rc on  rc.insurance_id= :insId and  (rc.prvdr_id= prvdr.prvdr_id or  c.ref_contract_Id = rc.ref_contract_Id)      
 LEFT OUTER JOIN membership_provider mp on mp.mbr_id  =  m.mbr_id  and rc.prvdr_id= mp.prvdr_id
LEFT OUTER JOIN membership_claims mc on mc.claim_id_number =  csv2bhClaim.ClaimNum and mc.mbr_id=m.mbr_id and report_month= :reportMonth
  WHERE  mc.claim_id_number is null and STRING_TO_DATE(MemDOB) is not null    
GROUP BY ClaimNum,m.mbr_id;


update membership_claims  mcc
join   
(
SELECT ClaimId,mbr_id, PCPAffiliationId,GROUP_CONCAT(DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(t.Diagnoses, ',', sub0.aNum), ',', -1)) AS Diagnoses
FROM 
( SELECT  ClaimId, mbr_id, PCPAffiliationId, group_concat( distinct Diagnoses) Diagnoses
 from ( 
 SELECT  ClaimId, mbr_id, PCPAffiliationId, diagnoses1 as Diagnoses from  temp_mbr_prvdr_claims
 union all
 select claim_id_number,mbr_id,pcp_prov_Id as PCPAffiliationId,Diagnoses  from membership_claims mc
 )a
 group by ClaimId, mbr_id, PCPAffiliationId)t
INNER JOIN
(
    SELECT 1 + units.i + tens.i * 10 AS aNum, units.i + tens.i * 10 AS aSubscript
    FROM (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units
    CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 ) tens
) sub0
  ON (1 + LENGTH(t.Diagnoses) - LENGTH(REPLACE(t.Diagnoses, ',', ''))) >= sub0.aNum
GROUP BY ClaimId,mbr_id, PCPAffiliationId

) a on mcc.claim_id_number =a.ClaimId  and mcc.mbr_id=a.mbr_id and mcc.pcp_prov_Id=a.PCPAffiliationId
set mcc.Diagnoses =a.Diagnoses;





update membership_claims  mcc
join   
(
SELECT ClaimNum,mbr_id, PCPID,GROUP_CONCAT(DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(t.Diagnoses, ',', sub0.aNum), ',', -1)) AS Diagnoses
FROM 
( SELECT  ClaimNum, mbr_id, PCPID, group_concat( distinct Diagnoses) Diagnoses
 from ( 
 SELECT   ClaimNum, m.mbr_id,PCPID,CONCAT_WS(',',NULLIF(diag1,'') , NULLIF(diag2,'') , NULLIF(diag3,''), NULLIF(diag4,'') )  Diagnoses
  FROM csv2Table_BH_Claim1 csv2BhClaim
 JOIN membership m on csv2bhClaim.MedicaidNumber != ''  and m.Mbr_MedicaidNo  =   csv2BhClaim.MedicaidNumber  and STRING_TO_DATE(MemDOB) is not null 
    union all
 select claim_id_number as ClaimNum,mbr_id,pcp_prov_Id as PCPID,Diagnoses  from membership_claims mc 
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

 insert into membership_claim_details 
 (mbr_claim_id, claim_line_seq_nbr, clm_line_adj_seq_nbr,
 activity_date, activity_month,
 claim_start_date,
 claim_end_date,
 paid_date,
 revenue_code, cpt_code, cpt_code_modifier1, cpt_code_modifier2, claim_status,
 location_id, risk_recon_cos_des, amount_paid, allow_amt, co_insurance,
 co_pay, deductible, cob_paid_amount, processing_status, pharmacy_name,
quantity, npos, risk_id, runn_date, ndc,
pharmacy, membership_claims, psychare, simple_county, triangles,
cover, created_date, updated_date, created_by, updated_by, active_ind, file_id,
 mony, drug_label_name, drug_version
 )
 SELECT  
 mc.mbr_claim_id, csv2BhClaim.ClaimLine, null clm_line_adj_seq_nbr, 
 cast(concat(csv2BhClaim.MOS,'-01') as date) ,
 date_format(cast(concat(csv2BhClaim.MOS,'-01') as date) ,'%Y%m'),
 cast(csv2BhClaim.ClaimStartDate as date), 
 cast(csv2BhClaim.ClaimEndDate as date),
 cast(csv2BhClaim.PaidDate as date),
 csv2BhClaim.RevCode, cpt.cpt_id, NULL cpt_code_modifier1, null cpt_code_modifier2, csv2BhClaim.ClaimDetailStatus, 
 roomType.id, csv2BhClaim.Cover risk_recon_cos_des, csv2BhClaim.amountpaid, if(csv2BhClaim.AllowAmt='',null,csv2BhClaim.AllowAmt), null co_insurance, 
 if( csv2BhClaim.Copay='',null,csv2BhClaim.Copay), null deductible, null cob_paid_amount, null processing_status, null pharmacy_name, 
 NULLIF(csv2BhClaim.Quantity,''),  csv2BhClaim.NPOS, csv2BhClaim.RiskId, cast(csv2BhClaim.RunnDate as date), csv2BhClaim.NDC,
 csv2BhClaim.PHARMACY, coalesce (if(csv2BhClaim.PHARMACY,0,null),if(csv2BhClaim.Claims,0,null),if(csv2BhClaim.Psychare,0,null)), csv2BhClaim.Psychare, csv2BhClaim.Simple_County, csv2BhClaim.Triangles,  
csv2BhClaim.Cover, now() created_date, now() updated_date, 'sarath' created_by ,'sarath' updated_by,'Y', :fileId,
  csv2BhClaim.Mony, csv2BhClaim.DrugLabelName, null drug_version
  
  FROM temp_csv2Table_BH_Claim csv2BhClaim
  LEFT JOIN membership m on    if(nullif(csv2BhClaim.MedicaidId,'') is null  , m.Mbr_LastName=lastname and m.Mbr_FirstName=firstname and m.Mbr_DOB=dob ,m.Mbr_MedicaidNo  = csv2BhClaim.MedicaidId)
  LEFT JOIN membership_claims mc on  mc.claim_id_number= csv2BhClaim.ClaimId   and  m.mbr_id =mc.mbr_id and  mc.ins_id=:insId and report_month=:reportMonth
  LEFT OUTER JOIN cpt_measure  cpt on cpt.code =  NULLIF(csv2BhClaim.ServCode,'')
  LEFT OUTER JOIN lu_place_of_service roomType on ucase(roomtype.name) = ucase(trim(POS))
  LEFT OUTER JOIN membership_claim_details mcd on mcd.mbr_claim_id =  mc.mbr_claim_id
  WHERE m.mbr_id is not null and mc.mbr_claim_id is not null and  mcd.mbr_claim_id is   null ;
  
  
 insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
mc.mbr_id,mc.ins_id,mc.prvdr_id,  mcd.activity_month activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,'sarath' created_by,'sarath' updated_by 
   FROM   membership_claims mc 
  JOIN membership_claim_details mcd on  mc.mbr_claim_id = mcd.mbr_claim_id   and mc.ins_id=:insId and mc.report_month = :reportMonth
  left outer join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mam.ins_id= mc.ins_id  and mam.activity_month=mcd.activity_month
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
  and mc.prvdr_id is not null   
group by mc.mbr_id,mc.ins_id,mc.prvdr_id,mcd.activity_Month; 
  
