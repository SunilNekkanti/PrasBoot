 
replace into provider_adjustment select null,c.insurance_id, c.prvdr_id, :fileId, :activityMonth reportMonth, cast(concat(:activityMonth, '01') as date) reportdate,  ml.IPA_CD, ml.RISK_ENTITY_TYPE, ml.CONTRACT_YEAR, ml.TIME_PERIOD, ml.CONTRACT_PERIOD_START_DT, 
ml.CONTRACT_PERIOD_END_DT, ml.TRACK_MODEL, ml.RISK_IND, ml.PCP_PROVIDER_NBR, ml.ACTIVITYDATE, ml.ACTIVITYMONTH, ml.RISK_RECON_COS_DESC, ml.ADJUSTMENT_DESC, ml.PRODUCT_LABEL, ml.PRODUCT_LVL1, 
ml.PRODUCT_LVL2, ml.PRODUCT_LVL3, ml.PRODUCT_LVL4, ml.PRODUCT_LVL5, ml.PRODUCT_LVL6, ml.PRODUCT_LVL7, ml.MARKET_LVL1, ml.MARKET_LVL2, ml.MARKET_LVL3, ml.MARKET_LVL4, ml.MARKET_LVL5, ml.MARKET_LVL6,
ml.MARKET_LVL7, ml.MARKET_LVL8,(ml.ADJUSTMENT_AMT), ml.TIN
from csv2table_amg_adjust ml
JOIN  ( select c.*,rc.insurance_id,rc.prvdr_id from contract c 
                        JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id  and insurance_id =:insId
				 ) c on FIND_IN_SET(ml.PCP_PROVIDER_NBR, c.PCP_PROVIDER_NBR)    
left join provider_adjustment pa on pa.report_month =:activityMonth and pa.ins_id =c.insurance_id and pa.prvdr_id = c.prvdr_id and pa.ACTIVITYMONTH = ml.ACTIVITYMONTH and pa.ADJUSTMENT_DESC = ml.ADJUSTMENT_DESC and pa.PRODUCT_LABEL = ml.PRODUCT_LABEL
where pa.PRVDR_ADJ_ID is null
 group by reportMonth,c.insurance_id,c.prvdr_id,ACTIVITYMONTH,ml.ADJUSTMENT_DESC,ml.PRODUCT_LABEL,PRODUCT_LVL1
 ;
  
 
 replace  into new_medical_loss_ratio  
  ( ins_id, prvdr_id, report_month, activity_month, qmlr,qmlr_claims, amg_funding, mbr_cnt,  inst_claims, prof_claims, phar_claims,	 unwanted_claims,sl_credit_claims,
		  amg_mbr_cnt, amg_inst, amg_prof, amg_phar, amg_sl_exp, amg_sl_credit, amg_vab_adj, amg_adj, amg_pcp_cap, amg_spec_cap,
  amg_dental_cap, amg_trans_cap, amg_vision_cap, amg_ibnr_inst, amg_ibnr_prof,  file_id, created_date, updated_date, created_by, updated_by, active_ind)
 select a.insurance_id,a.prvdr_id , :activityMonth reportMonth,ACTIVITYMONTH, 
 qmlrfunction(:activityMonth, a.insurance_id,a.prvdr_id,ACTIVITYMONTH,false,false) qmlr,
  qmlr_claims_function(:activityMonth, a.insurance_id,a.prvdr_id,ACTIVITYMONTH,false,false) qmlr_claims,
  amg_funding FUNDING, mlr.mbr_cnt ,  mlr.inst_claims, mlr.prof_claims, mlr.phar_claims,	 mlr.unwanted_claims,mlr.sl_credit_claims, amg_mbr_cnt MEMBER_MONTH_CNT,  amg_inst  , amg_prof , amg_phar ,
    amg_sl_exp STOPLOSS_CHARGE, amg_sl_credit STOPLOSS_CREDIT_AMT, amg_vab_adj VAB_ADJUSTMENT,a.ADJUSTMENT_AMT, amg_pcp_cap CAPITATION_PCP,
   amg_spec_cap CAPITATION_SPECIALIST, amg_dental_cap CAPITATION_DENTAL, amg_trans_cap CAPITATION_TRANSPORTATION,
   amg_vision_cap CAPITATION_VISION, ibnr_inst ,ibnr_prof IBNR_PROF,    :fileId fileId, now() creadted,now() updated, :username created_by , :username  updated_by, 'Y' active
    from (
    select    sum(if(ml.ADJUSTMENT_DESC = 'INST IBNR',ADJUSTMENT_AMT,0))  ibnr_inst , sum(if(ml.ADJUSTMENT_DESC = 'PROF IBNR',ADJUSTMENT_AMT,0)) ibnr_prof,  sum(if(ml.ADJUSTMENT_DESC not like '%IBNR',ADJUSTMENT_AMT,0))  ADJUSTMENT_AMT,c.insurance_id,c.prvdr_id,ml.ACTIVITYMONTH
 from csv2table_amg_adjust  ml 
JOIN  ( select c.*,rc.insurance_id,rc.prvdr_id from contract c 
                        JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id  and insurance_id = :insId
				 ) c on FIND_IN_SET(ml.PCP_PROVIDER_NBR, c.PCP_PROVIDER_NBR)
  
 group by  c.prvdr_id ,ACTIVITYMONTH
	) a	 
  left join new_medical_loss_ratio mlr on mlr.report_month =:activityMonth  and mlr.ins_id =a.insurance_id and mlr.prvdr_id =a.prvdr_id and mlr.activity_month = a.ACTIVITYMONTH
    where a.activitymonth > 201512 
 order by reportMonth,a.insurance_id,a.prvdr_id ,ACTIVITYMONTH;
 