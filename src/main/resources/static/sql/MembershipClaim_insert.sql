drop table if exists csv2Table_Amg_Claim_temp;
 create temporary table csv2Table_Amg_Claim_temp as
 select *
 from csv2Table_Amg_Claim
 group by SRC_SYS_MEMBER_NBR;
 
INSERT ignore INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
select * from (
 SELECT   MLNAME,MFNAME,lg.gender_id,STRING_TO_DATE(MBRDOB), 4 status,MEDICAIDNO,MEDICARENO,:fileId fileId,  now() created_date,now() updated_date,'sarath' created_by,'sarath' updated_by,'Y' active_ind  
  FROM csv2Table_Amg_Claim_temp csv2AmgClaim
   join lu_gender lg on lg.code = csv2AmgClaim.MBRGENDER
 LEFT OUTER JOIN (select * from membership_insurance group by SRC_SYS_MBR_NBR) mi on mi.SRC_SYS_MBR_NBR  =  convert(csv2AmgClaim.SRC_SYS_MEMBER_NBR,unsigned) and mi.ins_Id=:insId
 where mi.SRC_SYS_MBR_NBR is null
 group by csv2AmgClaim.SRC_SYS_MEMBER_NBR
 )a 
ON DUPLICATE KEY UPDATE
 Mbr_Status =a.status ,
 Mbr_MedicaidNo =a.MEDICAIDNO,
 file_id=a.fileId;
 
  
 INSERT INTO membership_insurance (ins_id,mbr_id,SRC_SYS_MBR_NBR,file_id,created_date,updated_date,created_by ,   updated_by ,   active_ind ) 
 SELECT :insId, m.mbr_id, csv2AmgClaim.SRC_SYS_MEMBER_NBR , :fileId, now(),now(),'sarath','sarath','Y'   FROM csv2Table_Amg_Claim_temp csv2AmgClaim
 join membership m  on m.Mbr_MedicaidNo=csv2AmgClaim.MEDICAIDNO
  LEFT OUTER JOIN (select * from membership_insurance group by SRC_SYS_MBR_NBR) mi on mi.SRC_SYS_MBR_NBR  =  convert(csv2AmgClaim.SRC_SYS_MEMBER_NBR,unsigned) and mi.ins_Id=:insId
 where mi.SRC_SYS_MBR_NBR is null
 group by csv2AmgClaim.SRC_SYS_MEMBER_NBR;
 

INSERT INTO membership_claims
(
claim_id_number,mbr_id,prvdr_id,ins_id,report_month,claim_type,facility_type_code,bill_type_code,
frequency_type_code,bill_type,dischargestatus,
MemEnrollId,Diagnoses,product_label,product_lvl1,product_lvl2,product_lvl3,product_lvl4,product_lvl5,product_lvl6,product_lvl7,
market_lvl1,market_lvl2,market_lvl3,market_lvl4,market_lvl5,market_lvl6,market_lvl7,market_lvl8,
tin,dx_type_cd,proc_type_cd,created_date,updated_date,created_by,updated_by,active_ind,file_id
)
SELECT 
CLAIMNUMBER,mi.mbr_id,rc.prvdr_id,:insId, :reportMonth reportMonth,CLAIMTYPE,lft.code,null,
null,csv2AmgClaim.BILLTYPE,csv2AmgClaim.DISCHARGESTATUS,null,
CONCAT_WS(',',NULLIF(DIAGNOSIS1,'') , NULLIF(DIAGNOSIS2,'') , NULLIF(DIAGNOSIS3,''), NULLIF(DIAGNOSIS4,'') ,NULLIF(DIAGNOSIS5,'') , NULLIF(DIAGNOSIS6,'') , NULLIF(DIAGNOSIS7,'') ,NULLIF(DIAGNOSIS8,'')) diagnoses,
csv2AmgClaim.PRODUCT_LABEL,csv2AmgClaim.PRODUCT_LVL1,csv2AmgClaim.PRODUCT_LVL2,csv2AmgClaim.PRODUCT_LVL3,csv2AmgClaim.PRODUCT_LVL4,csv2AmgClaim.PRODUCT_LVL5,
csv2AmgClaim.PRODUCT_LVL6,csv2AmgClaim.PRODUCT_LVL7,csv2AmgClaim.MARKET_LVL1,csv2AmgClaim.MARKET_LVL2,csv2AmgClaim.MARKET_LVL3,csv2AmgClaim.MARKET_LVL4,
csv2AmgClaim.MARKET_LVL5,csv2AmgClaim.MARKET_LVL6,csv2AmgClaim.MARKET_LVL7,csv2AmgClaim.MARKET_LVL8,
csv2AmgClaim.TIN,csv2AmgClaim.DX_TYPE_CD,csv2AmgClaim.PROC_TYPE_CD,
  now(),now(),'sarath','sarath','Y', :fileId 
  FROM csv2Table_Amg_Claim csv2AmgClaim
 LEFT OUTER JOIN ( select * from membership_insurance group by SRC_SYS_MBR_NBR)  mi on mi.SRC_SYS_MBR_NBR  =   csv2AmgClaim.SRC_SYS_MEMBER_NBR and mi.ins_Id=:insId
 LEFT OUTER JOIN contract c on INSTR(c.PCP_PROVIDER_NBR, csv2AmgClaim.PCP_PROVIDER_NBR )
 LEFT OUTER JOIN reference_contract rc on  c.ref_contract_Id = rc.ref_contract_Id  
LEFT OUTER JOIN lu_facility_type lft on lft.description = csv2AmgClaim.FACILITY_TYPE_DESC
LEFT OUTER JOIN membership_claims mc on mc.claim_id_number =  csv2AmgClaim.CLAIMNUMBER and mc.mbr_id=mi.mbr_id and mc.claim_type = csv2AmgClaim.CLAIMTYPE and mc.ins_id=:insId and mc.report_month = :reportMonth
WHERE  mc.claim_id_number is null
GROUP BY CLAIMNUMBER,mi.mbr_id,CLAIMTYPE,reportMonth;




