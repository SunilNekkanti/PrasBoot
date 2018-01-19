
 
 INSERT INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
SELECT   MLNAME,MFNAME,lg.gender_id,STRING_TO_DATE(MBRDOB), 4,SRC_SYS_MEMBER_NBR,MEDICAIDNO,MEDICARENO,:fileId ,  now(),now(),:username,:username,'Y'  
  FROM csv2table_amg_member_level csv2AmgMbrLvl
   join lu_gender lg on lg.code = csv2AmgMbrLvl.MBRGENDER
 LEFT  JOIN membership  m on m.SRC_SYS_MBR_NBR  =  csv2AmgMbrLvl.SRC_SYS_MEMBER_NBR  
 where m.SRC_SYS_MBR_NBR is null
 group by csv2AmgMbrLvl.SRC_SYS_MEMBER_NBR;

 select :activityMonth into @reportMonth;

replace into membership_level_claim_summary select  distinct :insId,:fileId, :activityMonth, IPA_CD, IPANAME, PCP_PROVIDER_NBR, PCPFIRSTNAME, PCPLASTNAME, CONTRACT_YR, 
TIME_PERIOD, RISK_ENTITY_TYPE, EFFECTIVE_DT, EXPIRATION_DT, TRACK_MODEL, RISK_FLAG, ACTIVITYDATE, ACTIVITYMONTH, SRC_SYS_MEMBER_NBR, MLNAME, MFNAME, MBRDOB, 
MBRGENDER, MEDICAIDNO, MEDICARENO, MEMBER_MONTH_CNT, HIC_NUMBER, HOSPICE, ESRD, AGED_DISABLED_MSP, INSTITUTIONAL, NHC, MEDICAID_FLAG, LTI_FLAG, MEDICAID_INDICATOR,
RISK_ADJUSTER_FACTOR_A, PART_D_RA_FACTOR, LIS_PREMIUM_SUBSIDY, PREMIUM_REBATE_PARTD_AMOUNT, PART_D_SUBSIDY_PAYMENT_AMOUNT, TOTAL_MA_PAYMENT_AMOUNT, 
TOTAL_MB_PAYMENT_AMOUNT, TOTAL_PART_D_PAYMENT, PART_D_IND, PREMIUM, OB_KICK_PREMIUM, NEWBORN_KICK_PREMIUM, NICU_KICK_PREMIUM, LOW_BIRTH_KICK_PREMIUM, 
EXCESS_NICU_REV, FUNDING, INST_CLAIMS, PROF_CLAIMS, PHAR_CLAIMS, STOPLOSS_RATE, STOPLOSS_CHARGE, STOPLOSS_CREDIT_AMT, STOPLOSS_CREDIT, STOPLOSS_THRESHOLD, 
STOPLOSS_CREDIT_PCT, PREMIUM_TAX, VAB_ADJUSTMENT, MCO_ADMIN_FEE, MEF_PCT, MEFSHAREAMOUNT, MEF_BALANCE_SHARE, PRODUCT_LABEL, PRODUCT_LVL1, PRODUCT_LVL2, PRODUCT_LVL3,
PRODUCT_LVL4, PRODUCT_LVL5, PRODUCT_LVL6, PRODUCT_LVL7, MARKET_LVL1, MARKET_LVL2, MARKET_LVL3, MARKET_LVL4, MARKET_LVL5, MARKET_LVL6, MARKET_LVL7, MARKET_LVL8, 
CAPITATION_DENTAL, CAPITATION_PCP, CAPITATION_SPECIALIST, CAPITATION_TRANSPORTATION, CAPITATION_VISION, IBNR_INST, IBNR_PROF, IBNR_TOT, FINANCE_RESERVE_RATE, 
FINANCE_RESERVE_TYPE, FINANCE_RESERVE_AMOUNT, TIN 
from csv2table_amg_member_level ml;


 replace into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
m.mbr_id,c.insurance_id,c.prvdr_id,  csv2AmgMbrLvl.ACTIVITYMONTH activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by 
   FROM    csv2table_amg_member_level csv2AmgMbrLvl 
   join membership m on m.SRC_SYS_MBR_NBR  =  csv2AmgMbrLvl.SRC_SYS_MEMBER_NBR  
   JOIN  ( select c.*,rc.insurance_id,rc.prvdr_id from contract c 
                        JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id  and insurance_id =:insId
				 ) c on FIND_IN_SET(csv2AmgMbrLvl.PCP_PROVIDER_NBR, c.PCP_PROVIDER_NBR) 
  left outer join membership_activity_month mam on mam.mbr_id=m.mbr_id and mam.prvdr_id =c.prvdr_id and mam.ins_id=c.insurance_id  and mam.activity_month=csv2AmgMbrLvl.ACTIVITYMONTH 
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
group by m.mbr_id,c.insurance_id,c.prvdr_id,csv2AmgMbrLvl.activityMonth; 

replace  into new_medical_loss_ratio
( ins_id, prvdr_id, report_month, activity_month, qmlr, amg_funding, amg_mbr_cnt, amg_inst, amg_prof, amg_phar, amg_sl_exp, amg_sl_credit, amg_vab_adj, amg_adj, amg_pcp_cap, amg_spec_cap, amg_dental_cap, amg_trans_cap, amg_vision_cap, amg_ibnr_inst, amg_ibnr_prof,  file_id, created_date, updated_date, created_by, updated_by, active_ind)
 select  c.insurance_id,c.prvdr_id , :activityMonth reportmonth,ACTIVITYMONTH, 
 qmlrfunction(:activityMonth, c.insurance_id,c.prvdr_id,ACTIVITYMONTH ,false,false) qmlr,
sum(FUNDING) FUNDING, sum(MEMBER_MONTH_CNT)MEMBER_MONTH_CNT, sum(INST_CLAIMS)INST_CLAIMS, sum(PROF_CLAIMS)PROF_CLAIMS, sum(PHAR_CLAIMS)PHAR_CLAIMS,
 sum(STOPLOSS_CHARGE)STOPLOSS_CHARGE, sum(STOPLOSS_CREDIT_AMT)STOPLOSS_CREDIT_AMT, sum(VAB_ADJUSTMENT)VAB_ADJUSTMENT, mlr.amg_adj  ADJUSTMENT, sum(CAPITATION_PCP)CAPITATION_PCP,
 sum(CAPITATION_SPECIALIST)CAPITATION_SPECIALIST, sum(CAPITATION_DENTAL)CAPITATION_DENTAL, sum(CAPITATION_TRANSPORTATION)CAPITATION_TRANSPORTATION,
 sum(CAPITATION_VISION)CAPITATION_VISION, ifnull(mlr.amg_ibnr_inst,0), ifnull(mlr.amg_ibnr_prof,0),    :fileId fileId, now() creadted,now() updated, :username created_by , :username updated_by, 'Y' active
 from csv2table_amg_member_level  ml 
JOIN  ( select c.*,rc.insurance_id,rc.prvdr_id from contract c 
                        JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id  and insurance_id =:insId
				 ) c on FIND_IN_SET(ml.PCP_PROVIDER_NBR, c.PCP_PROVIDER_NBR)       
  left join new_medical_loss_ratio mlr on mlr.report_month =:activityMonth and mlr.ins_id =c.insurance_id and mlr.prvdr_id =c.prvdr_id and mlr.activity_month = ml.ACTIVITYMONTH
  where ml.activityMonth >201512
 group by  reportmonth,c.insurance_id,c.prvdr_id ,ACTIVITYMONTH
 order by  reportmonth,c.insurance_id,c.prvdr_id ,ACTIVITYMONTH;
