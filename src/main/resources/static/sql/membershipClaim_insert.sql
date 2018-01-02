
drop table if exists csv2Table_Amg_Claim_temp;
 create temporary table csv2Table_Amg_Claim_temp as
 select *   from csv2Table_Amg_Claim group by SRC_SYS_MEMBER_NBR;
  
 alter table csv2Table_Amg_Claim_temp add key MBRGENDER(MBRGENDER),  add key SRC_SYS_MEMBER_NBR(SRC_SYS_MEMBER_NBR)  ;
 
 select :activityMonth into @reportMonth;
 
INSERT ignore INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
 SELECT   MLNAME,MFNAME,lg.gender_id,
 case when STRING_TO_DATE(MBRDOB)    > current_date   then  DATE_SUB( STRING_TO_DATE(MBRDOB) ,INTERVAL 100 YEAR)   else  STRING_TO_DATE(MBRDOB)  end  dob,  
 4 status,csv2AmgClaim.SRC_SYS_MEMBER_NBR,MEDICAIDNO,MEDICARENO,:fileId fileId,  now() created_date,now() updated_date,:username created_by,:username updated_by,'Y' active_ind  
  FROM csv2Table_Amg_Claim_temp csv2AmgClaim
   join lu_gender lg on lg.code = csv2AmgClaim.MBRGENDER
 LEFT  JOIN  membership m on m.SRC_SYS_MBR_NBR  =   csv2AmgClaim.SRC_SYS_MEMBER_NBR
 where m.SRC_SYS_MBR_NBR is null;
 
 

 -- INSERT INTO membership_insurance (ins_id,mbr_id,SRC_SYS_MBR_NBR,file_id,created_date,updated_date,created_by ,   updated_by ,   active_ind ) 
 -- SELECT :insId, m.mbr_id, csv2AmgClaim.SRC_SYS_MEMBER_NBR , :fileId, now(),now(),:username,:username,'Y'   FROM csv2Table_Amg_Claim_temp csv2AmgClaim
 -- join membership m  on m.SRC_SYS_MBR_NBR=csv2AmgClaim.SRC_SYS_MBR_NBR
 --  LEFT  JOIN  membership_insurance mi on mi.mbr_id = m.mbr_id and  mi.ins_Id=:insId
 -- where mi.mbr_id is null
 -- group by csv2AmgClaim.SRC_SYS_MEMBER_NBR;
 

INSERT INTO membership_claims
(
claim_id_number,mbr_id,prvdr_id,ins_id,report_month,claim_type,facility_type_code,bill_type_code,
frequency_type_code,bill_type,dischargestatus,
MemEnrollId,Diagnoses,product_label,product_lvl1,product_lvl2,product_lvl3,product_lvl4,product_lvl5,product_lvl6,product_lvl7,
market_lvl1,market_lvl2,market_lvl3,market_lvl4,market_lvl5,market_lvl6,market_lvl7,market_lvl8,
tin,dx_type_cd,proc_type_cd,created_date,updated_date,created_by,updated_by,active_ind,file_id
)
SELECT 
CLAIMNUMBER,m.mbr_id,c.prvdr_id,:insId, @reportMonth reportMonth,CLAIMTYPE,lft.code,null,
null,csv2AmgClaim.BILLTYPE,csv2AmgClaim.DISCHARGESTATUS,null,
CONCAT_WS(',',NULLIF(DIAGNOSIS1,'') , NULLIF(DIAGNOSIS2,'') , NULLIF(DIAGNOSIS3,''), NULLIF(DIAGNOSIS4,'') ,NULLIF(DIAGNOSIS5,'') , NULLIF(DIAGNOSIS6,'') , NULLIF(DIAGNOSIS7,'') ,NULLIF(DIAGNOSIS8,'')) diagnoses,
csv2AmgClaim.PRODUCT_LABEL,csv2AmgClaim.PRODUCT_LVL1,csv2AmgClaim.PRODUCT_LVL2,csv2AmgClaim.PRODUCT_LVL3,csv2AmgClaim.PRODUCT_LVL4,csv2AmgClaim.PRODUCT_LVL5,
csv2AmgClaim.PRODUCT_LVL6,csv2AmgClaim.PRODUCT_LVL7,csv2AmgClaim.MARKET_LVL1,csv2AmgClaim.MARKET_LVL2,csv2AmgClaim.MARKET_LVL3,csv2AmgClaim.MARKET_LVL4,
csv2AmgClaim.MARKET_LVL5,csv2AmgClaim.MARKET_LVL6,csv2AmgClaim.MARKET_LVL7,csv2AmgClaim.MARKET_LVL8,
csv2AmgClaim.TIN,csv2AmgClaim.DX_TYPE_CD,csv2AmgClaim.PROC_TYPE_CD,
  now(),now(),:username,:username,'Y', :fileId
  FROM csv2Table_Amg_Claim csv2AmgClaim
  JOIN  membership m on m.SRC_SYS_MBR_NBR  =   csv2AmgClaim.SRC_SYS_MEMBER_NBR 
   LEFT JOIN (select c.*,rc.prvdr_id,insurance_id from contract c 
                   JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id  and insurance_id =:insId
                   )c on FIND_IN_SET( csv2AmgClaim.PCP_PROVIDER_NBR,c.PCP_PROVIDER_NBR )
LEFT OUTER JOIN lu_facility_type lft on lft.description = csv2AmgClaim.FACILITY_TYPE_DESC
LEFT OUTER JOIN membership_claims mc on mc.claim_id_number =  csv2AmgClaim.CLAIMNUMBER and mc.mbr_id=m.mbr_id and mc.claim_type = csv2AmgClaim.CLAIMTYPE and mc.ins_id=:insId and mc.report_month = @reportMonth
WHERE  mc.claim_id_number is null
GROUP BY CLAIMNUMBER,m.mbr_id,CLAIMTYPE,reportMonth;

