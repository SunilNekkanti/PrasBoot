INSERT INTO pharmacy (
pharmacy_id, `name`,address_1,address_2,city,state_code,
zip, phone_number,ncp_dpn_number,npi_number, pharmacy_number,file_id,
created_date, updated_date, created_by, updated_by,active_ind
)
SELECT NULL, PHARMACYNAME, PHARMACYADDRLINE1,PHARMACYADDRLINE2,
PHARMACYCITYNAME, PHARMACYSTATECODE, PHARMACYZIP, PHARMACYPHONENUMBER, PHARMACYNCPDPNUMBER,
PHARMACYNPINUMBER, PHARMACYNUMBER, :fileId, now(),now(),'sarath','sarath','Y' 
FROM 
csv2table_amg_pharmacy csv2talbe_phar
LEFT OUTER JOIN  pharmacy phar  on TRIM(csv2talbe_phar.PHARMACYNAME)  =  phar.name
WHERE   phar.pharmacy_id IS NULL
group by PHARMACYNAME;


 INSERT INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
SELECT   MLNAME,MFNAME,lg.gender_id,STRING_TO_DATE(MBRDOB), 4,MEDICAIDNO,MEDICARENO,:fileId ,  now(),now(),'sarath','sarath','Y'  
  FROM csv2table_amg_pharmacy csv2AmgClaim
   join lu_gender lg on lg.code = csv2AmgClaim.MBRGENDER
 LEFT OUTER JOIN membership_insurance mi on mi.SRC_SYS_MBR_NBR  =  convert(csv2AmgClaim.SRC_SYS_MEMBER_NBR,unsigned) and mi.ins_Id=:insId
 where mi.SRC_SYS_MBR_NBR is null
 group by csv2AmgClaim.SRC_SYS_MEMBER_NBR;


 INSERT INTO membership_insurance (ins_id,mbr_id,SRC_SYS_MBR_NBR,file_id,created_date,updated_date,created_by ,   updated_by ,   active_ind ) 
 SELECT :insId, m.mbr_id, csv2AmgClaim.SRC_SYS_MEMBER_NBR , :fileId, now(),now(),'sarath','sarath','Y'   FROM csv2table_amg_pharmacy  csv2AmgClaim
 join membership m  on m.Mbr_MedicaidNo=csv2AmgClaim.MEDICAIDNO
  LEFT OUTER JOIN (select * from membership_insurance group by SRC_SYS_MBR_NBR) mi on mi.SRC_SYS_MBR_NBR  =  convert(csv2AmgClaim.SRC_SYS_MEMBER_NBR,unsigned) and mi.ins_Id=:insId
 where mi.SRC_SYS_MBR_NBR is null
 group by csv2AmgClaim.SRC_SYS_MEMBER_NBR;
 


INSERT INTO membership_claims
( claim_id_number, mbr_id, prvdr_id, pcp_prov_Id, ins_id, report_Month, claim_type,
 location_id, facility_type_code, bill_type_code, frequency_type_code,  
 bill_type, dischargestatus, MemEnrollId, Diagnoses, 
 product_label, product_lvl1, product_lvl2, product_lvl3, product_lvl4, product_lvl5, product_lvl6, product_lvl7, 
 market_lvl1, market_lvl2, market_lvl3, market_lvl4, market_lvl5, market_lvl6, market_lvl7, market_lvl8, 
 risk_recon_cos_des, tin, dx_type_cd, proc_type_cd, created_date, updated_date, created_by, updated_by, active_ind, file_id
 )

SELECT 
CLAIMNUMBER,mi.mbr_id,rc.prvdr_id, phar.PCP_PROVIDER_NBR,:insId,:reportMonth reportMonth,CLAIMTYPE,NULL location_id,
NULL facility_type_code,NULL bill_type_code, NULL frequency_type_code, NULL bill_type,NULL dischargestatus,
NULL MemEnrollId,NULL Diagnoses,phar.PRODUCT_LABEL,phar.PRODUCT_LVL1,phar.PRODUCT_LVL2,phar.PRODUCT_LVL3,phar.PRODUCT_LVL4,phar.PRODUCT_LVL5,
phar.PRODUCT_LVL6,phar.PRODUCT_LVL7,phar.MARKET_LVL1,phar.MARKET_LVL2,phar.MARKET_LVL3,phar.MARKET_LVL4,phar.MARKET_LVL5,phar.MARKET_LVL6,
phar.MARKET_LVL7,phar.MARKET_LVL8,phar.RISK_RECON_COS_DESC,phar.TIN,NULL,NULL,NOW(),NOW(),'SARATH','SARATH','Y' , :fileId

FROM csv2table_amg_pharmacy phar
LEFT OUTER JOIN (select * from membership_insurance group by SRC_SYS_MBR_NBR) mi  on mi.SRC_SYS_MBR_NBR =phar.SRC_SYS_MEMBER_NBR and mi.ins_id=:insId
LEFT OUTER JOIN contract c on INSTR(c.PCP_PROVIDER_NBR, trim(phar.PCP_PROVIDER_NBR))
LEFT OUTER JOIN reference_contract rc on  c.ref_contract_Id = rc.ref_contract_Id  
LEFT JOIN membership_claims mc on mc.claim_id_number= phar.CLAIMNUMBER and mc.mbr_id =mi.mbr_id and mc.report_month = :reportMonth
where mc.claim_id_number is null
group by  reportMonth,CLAIMNUMBER,phar.SRC_SYS_MEMBER_NBR;



INSERT INTO membership_claim_details 
(  mbr_claim_id, claim_line_seq_nbr, clm_line_adj_seq_nbr, activity_date, activity_month, claim_start_date, claim_end_date, paid_date, revenue_code, cpt_code,
cpt_code_modifier1, cpt_code_modifier2, claim_status, location_id, risk_recon_cos_des, amount_paid, allow_amt, co_insurance, co_pay, deductible, cob_paid_amount, processing_status,
 pharmacy_name, quantity, npos, risk_id, runn_date, ndc, ndc_description, mail_order_ind, number_of_refills, days_supply, ingredient_cost, dispensing_fee, sales_tax, check_mail_date,
 presdeanumber, pres_npi_number, prescribing_physician, prescriber_spec_code, generic_indicator, therapeutic_class, medicareb_indicator, medd_covered_drug_flag, pharmacy_id, pharmacy, 
 membership_claims, psychare, simple_county, triangles, cover, created_date, updated_date, created_by, updated_by, active_ind, file_id, mony, drug_label_name, drug_version
)
 
select
mc.mbr_claim_id,1 claim_line_seq_nbr,null clm_line_adj_seq_nbr,STRING_TO_DATE(phar.ACTIVITYDATE) activity_date,phar.ACTIVITYMONTH	 ,STRING_TO_DATE(phar.ACTIVITYDATE) claim_start_date,STRING_TO_DATE(phar.ACTIVITYDATE) claim_end_date,
STRING_TO_DATE(DATEFILLED) ,null revenue_code,null cpt_code,null cpt_code_modifier1,null cpt_code_modifier2,TRANSACTIONSTATUS claim_status,null location_id,RISK_RECON_COS_DESC,AMOUNTPAID amount_paid,null  allow_amt,
null  co_insurance,null COPAY, null deductible, null cob_paid_amount,  TRANSACTIONSTATUS processing_status,  PHARMACYNAME, METRICQUANTITY, null npos,null risk_id,null runn_date,
NDCNUMBER,NDCDESCRIPTION, MAILORDERIND,	 NUMBEROFREFILLS, DAYSSUPPLY, INGREDIENTCOST, DISPENSINGFEE, SALESTAX,STRING_TO_DATE(CHECKMAILDATE),if(trim(phar.PRESDEANUMBER)='',null,phar.PRESDEANUMBER),
if(trim(phar.PRESNPINUMBER)='',null,phar.PRESNPINUMBER),phar.PRESCRIBINGPHYSICIAN,phar.PRESCRIBERSPECCODE	,phar.GENERICINDICATOR,
phar.THERAPEUTICCLASS	,phar.MEDICAREBINDICATOR	,phar.MEDDCOVEREDDRUGFLAG,null pharmacyid,AMOUNTPAID pharmacy,AMOUNTPAID  membership_claims,null psychare,null simple_county,null triangles,null cover,now() created_date,now()  updated_date, 
'sarath' created_by, 'sarath' updated_by, 'Y'active_ind, :fileId file_id, null mony, null drug_label_name, null drug_version 
 from  csv2table_amg_pharmacy phar
 LEFT OUTER JOIN (select * from membership_insurance group by SRC_SYS_MBR_NBR) mi  on mi.SRC_SYS_MBR_NBR =phar.SRC_SYS_MEMBER_NBR and mi.ins_id=:insId
 join membership_claims  mc on mc.claim_id_number= phar.CLAIMNUMBER and mc.mbr_id =mi.mbr_id and  mc.report_month = :reportMonth
 left outer join membership_claim_details mcd on mcd.mbr_claim_id =mc.mbr_claim_id  
 where mcd.mbr_claim_id is null;
 
 
  insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
mc.mbr_id,mc.ins_id,mc.prvdr_id,  mcd.activity_month activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,'sarath' created_by,'sarath' updated_by 
   FROM   membership_claims mc 
  JOIN membership_claim_details mcd on  mc.mbr_claim_id = mcd.mbr_claim_id   and mc.ins_id=:insId and mc.report_month = :reportMonth and mc.claim_type='PHAR'
  left outer join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mam.ins_id= mc.ins_id  and mam.activity_month=mcd.activity_month
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
group by mc.mbr_id,mc.ins_id,mc.prvdr_id,mcd.activity_Month; 


 
INSERT IGNORE INTO medical_loss_ratio
  (ins_id, prvdr_id, report_month, activity_month, patients, fund, prof, inst, pharmacy, unwanted_claims,  stop_loss, file_id, created_date, updated_date, created_by, updated_by)
 SELECT  
  mam.ins_id, mam.prvdr_id,  if(mc.report_month is null,:reportMonth,mc.report_month) as  report_month , mam.activity_month CAP_PERIOD,  cap.patients patients, cap.patients *150  fund,
round(sum(if (claim_type = 'PROF',membership_claims,null)) ,2) as 'PROF',
 round(sum(if (claim_type = 'INST',membership_claims,null)),2) as 'INST',
  round(sum(if (claim_type = 'PHAR',membership_claims,null)),2)   as 'PHAR' ,
round(uc.unwantedClaims ,2) unwantedClaims,
  round(stoploss,2) stoploss,  1 ,  now(),  now(),  'sarath',  'sarath'    
  FROM  membership_activity_month mam
  join ( select sum(mm) patients,ins_id,prvdr_id,CAP_PERIOD from membership_cap_report group by ins_id,prvdr_id,CAP_PERIOD)cap on cap.cap_period=mam.activity_month and cap.prvdr_id=mam.prvdr_id and cap.ins_id=mam.ins_id
LEFT JOIN  membership_claims mc on  mam.ins_id=mc.ins_id and mc.prvdr_id=mam.prvdr_id and mam.mbr_id=mc.mbr_id and mc.ins_id = :insId and mc.report_month= :reportMonth
LEFT JOIN  membership_claim_details mcd on mc.mbr_claim_id=mcd.mbr_claim_id and mcd.activity_month=mam.activity_month 
LEFT JOIN  (SELECT mc.prvdr_id,mcd.activity_month  ,sum(mcd.membership_claims)  unwantedClaims  ,
CASE WHEN    mam.is_cap ='Y' then 'wanted'  else  'unwanted' end type1 
from membership_claims mc 
join membership_claim_details mcd on mc.mbr_claim_id=mcd.mbr_claim_id and  mc.report_month= :reportMonth
left join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mcd.activity_month =mam.activity_month
where     
case when  mam.is_cap ='Y' then 1=0 else
( case when mam.mbr_id is  not  null then case when mam.prvdr_id is not null then mam.activity_month is null   else mam.prvdr_id is  null end else  mam.mbr_id is   null end 
 or mam.is_cap='N' )
end
group by mc.prvdr_id, mcd.activity_month ,type1)  uc on  uc.prvdr_id= mam.prvdr_id and  uc.activity_month =mam.activity_month 
left join ( select prvdr_id ,activity_month , sum(stoploss) stoploss from   (   select mc.prvdr_id,mcd.activity_month,mc.mbr_id  , sum(if ( mc.claim_type = 'INST',membership_claims ,null) ) -30000 stoploss   
from membership_claims mc
join membership_claim_details mcd on mc.mbr_claim_id=mcd.mbr_claim_id and mc.report_month= :reportMonth
left join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mcd.activity_month =mam.activity_month
where    mam.is_cap ='Y'  
group by mc.prvdr_id, mcd.activity_month, mc.mbr_id having sum(if ( mc.claim_type = 'INST',membership_claims ,null) ) -30000 > 0    )a group by prvdr_id ,activity_month ) slr on  slr.prvdr_id= mam.prvdr_id and  slr.activity_month =mam.activity_month
where mam.activity_month >201512
group by  mam.ins_id,  mam.prvdr_id, mam.activity_month 
having  if(PROF is null ,0,PROF) +if(INST is null, 0 ,INST) + if(PHAR is null, 0,PHAR)>0 ;