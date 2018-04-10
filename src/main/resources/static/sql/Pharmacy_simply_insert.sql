delete from csv2table_simply_pharmacy where product_label like 'Medicaid';

 select :activityMonth into @reportMonth;
 
 INSERT IGNORE INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
 SELECT   substring_index(`MemberName`,',',1) lastname,substring_index(`MemberName`,',',-1) firstname, 4 ,STRING_TO_DATE(DateofBirth) dob, 4, MedicaidNumber, MedicaidNumber, '',:fileId,  now(),now(),:username,:username,'Y'  
  FROM csv2table_simply_pharmacy pharClaim
 LEFT OUTER JOIN membership m on    m.SRC_SYS_MBR_NBR = pharClaim.MedicaidNumber
 where m.mbr_id is null  group by pharClaim.MedicaidNumber;
  

drop table if exists temp_mbr_prvdr_claims;
 
 create temporary table temp_mbr_prvdr_claims as 
select    pharClaims.* , c.prvdr_id ,m.mbr_id,
null diagnoses1, null product_lvl1, null product_lvl2, null market_lvl1, 
null product_lvl4, null product_lvl5, null product_lvl6, null product_lvl7, null product_lvl8, 
null market_lvl2, null market_lvl3, null market_lvl4, null market_lvl5, null market_lvl6,
null market_lvl7, null market_lvl8, null tin,'ICD10', null proc_type_cd,
now() created_date, now() updated_date, :username created_by, :username updated_by, 'Y'active_ind, :fileId file_id 
 FROM csv2table_simply_pharmacy pharClaims 
  join membership m on m.SRC_SYS_MBR_NBR = pharClaims.MedicaidNumber
 LEFT JOIN  ( select c.*,rc.prvdr_id,rc.insurance_id from contract c 
  JOIN reference_contracts rc  on   c.contract_Id = rc.contract_Id  ) c on FIND_IN_SET(pharClaims.PCPID,c.PCP_PROVIDER_NBR) and  c.insurance_id=:insId   ;



 INSERT INTO membership_claims
(
claim_id_number, mbr_id, prvdr_id,  ins_id, report_month,claim_type,
facility_type_code, bill_type_code, frequency_type_code, bill_type,  dischargestatus, MemEnrollId,
Diagnoses, product_label, product_lvl1, product_lvl2, product_lvl3, 
product_lvl4, product_lvl5, product_lvl6, product_lvl7, market_lvl1,
market_lvl2, market_lvl3, market_lvl4, market_lvl5, market_lvl6,
market_lvl7, market_lvl8, tin, dx_type_cd, proc_type_cd,
created_date, updated_date, created_by, updated_by, active_ind, file_id,pcp_prov_Id
)

 SELECT  ClaimNumber, csv2BhClaim.mbr_id,csv2BhClaim.prvdr_id,:insId, :activityMonth report_month, csv2BhClaim.DrugType,
null facility_type_code, NULL bill_type_code, NULL frequency_type_code,  csv2BhClaim.TherClass  ,  null dischargestatus, csv2BhClaim.EnrollID,
null diagnoses,  csv2BhClaim.product_label, null product_lvl1, null product_lvl2, null market_lvl1, 
null product_lvl4, null product_lvl5, null product_lvl6, null product_lvl7, null product_lvl8, 
null market_lvl2, null market_lvl3, null market_lvl4, null market_lvl5, null market_lvl6,
null market_lvl7, null market_lvl8, null tin,'ICD10', null proc_type_cd,
now() created, now()updated, :username createdby, :username updatedby, 'Y' activeInd, :fileId fileid,csv2BhClaim.PCPID 

  FROM temp_mbr_prvdr_claims csv2BhClaim 
 LEFT  JOIN membership_claims mc on  mc.claim_id_number = ClaimNumber  and report_month= :activityMonth
 WHERE    mc.claim_id_number is null 
GROUP BY ClaimNumber, csv2BhClaim.mbr_id;



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
 SELECT  distinct
 mc.mc_mbr_claim_id, 1, null clm_line_adj_seq_nbr, 
csv2BhClaim.Activity_Date ,
 date_format(csv2BhClaim.Activity_Date ,'%Y%m'),
 csv2BhClaim.FillDate , 
  csv2BhClaim.FillDate,
 csv2BhClaim.Activity_Date,
 null , null , NULL cpt_code_modifier1, null cpt_code_modifier2, 'PAID' , 
 pos.code, csv2BhClaim.claim_cat risk_recon_cos_des, csv2BhClaim.paid, null, null co_insurance, 
 if( csv2BhClaim.MemberCopay='',null,csv2BhClaim.MemberCopay), null deductible, null cob_paid_amount, null processing_status, PharmacyName pharmacy_name, 
 NULLIF(csv2BhClaim.MetricQuantity,''),  null, csv2BhClaim.Risk, csv2BhClaim.FillDate, csv2BhClaim.NDCNumber,
 null, if(csv2BhClaim.paid,0,null),null,   csv2BhClaim.memcounty, csv2BhClaim.claim_cat,  
csv2BhClaim.claim_cat, now() created_date, now() updated_date, :username created_by ,:username updated_by,'Y', 1 fileId,
  csv2BhClaim.RxNumber, csv2BhClaim.NDCDescription, null drug_version
  
  FROM temp_mbr_prvdr_claims csv2BhClaim
  JOIN (
  select mc.mbr_claim_id mc_mbr_claim_id,mc.claim_id_number, mc.mbr_id,mc.ins_id,mc.prvdr_id, mc.report_month,mc.claim_type,mcd.mbr_claim_id from  membership_claims mc 
		  LEFT  JOIN membership_claim_details mcd on mcd.mbr_claim_id =  mc.mbr_claim_id where mcd.mbr_claim_id is null)mc  on 
   (mc.report_month, mc.ins_id, mc.mbr_id, mc.claim_type, mc.claim_id_number) =   (:activityMonth,:insId,csv2BhClaim.mbr_id ,csv2BhClaim.DrugType, csv2BhClaim.ClaimNumber) 
     left  join lu_place_of_service pos on pos.name like concat(csv2BhClaim.claim_cat,'%') 
  WHERE  mc.mbr_claim_id is   null ;
  
 
  
 insert into membership_activity_month(mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
csv2BhClaim.mbr_id,:insId,csv2BhClaim.prvdr_id, date_format(csv2BhClaim.Activity_Date,'%Y%m') activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by 
FROM temp_mbr_prvdr_claims csv2BhClaim
 left outer join membership_activity_month mam on mam.mbr_id=csv2BhClaim.mbr_id and mam.prvdr_id =csv2BhClaim.prvdr_id and mam.ins_id= :insId  and mam.activity_month=date_format(csv2BhClaim.Activity_Date,'%Y%m')
   where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
  and csv2BhClaim.prvdr_id is not null   
group by csv2BhClaim.mbr_id,csv2BhClaim.prvdr_id,activityMonth; 

 
 replace into new_medical_loss_ratio
 ( ins_id, prvdr_id, report_month, activity_month, amg_funding, mbr_cnt, inst_claims, prof_claims, phar_claims, unwanted_claims, sl_credit_claims, amg_mbr_cnt, amg_inst, amg_prof, amg_phar,
  amg_sl_exp, amg_sl_credit, amg_vab_adj, amg_adj, amg_pcp_cap, amg_spec_cap, amg_dental_cap, amg_trans_cap, amg_vision_cap, amg_ibnr_inst, amg_ibnr_prof, 
   file_id, created_date, updated_date, created_by, updated_by)
  SELECT  
   mam.ins_id, mam.prvdr_id,  if(mc.report_month is null,@reportMonth,mc.report_month) as  report_month , mam.activity_month CAP_PERIOD,   nmlr.amg_funding fund,cap.patients patients,
     round(sum(if (claim_type = 'INST',mc.membership_claims,null)),2) as 'INST',
   round(sum(if (claim_type = 'PROF',mc.membership_claims,null)) ,2) as 'PROF', 
    round(sum(if (claim_type = 'PHAR',mc.membership_claims,null)),2)   as 'PHAR' ,
  round(uc.unwantedClaims ,2) unwantedClaims,
     round(ifnull(stoploss,0),2) sl_credit_claims,  
     nmlr.amg_mbr_cnt,nmlr.amg_inst,nmlr.amg_prof,nmlr.amg_phar,nmlr.amg_sl_exp,nmlr.amg_sl_credit,nmlr.amg_vab_adj,nmlr.amg_adj,nmlr.amg_pcp_cap,nmlr.amg_spec_cap,nmlr.amg_dental_cap,nmlr.amg_trans_cap,
 nmlr.amg_vision_cap,nmlr.amg_ibnr_inst,nmlr.amg_ibnr_prof, :fileId fileId,  now(),  now(),  :username createdBy,  :username  updatedBy  
    FROM  membership_activity_month mam
     left join ( select sum(mm) patients,ins_id,prvdr_id,CAP_PERIOD from membership_cap_report group by ins_id,prvdr_id,CAP_PERIOD)cap on cap.cap_period=mam.activity_month and cap.prvdr_id=mam.prvdr_id and cap.ins_id=mam.ins_id
 	 LEFT JOIN (select a.mbr_id,a.claim_type,a.prvdr_id,a.ins_id,a.report_month,mcd.activity_month,mcd.membership_claims  from membership_claims a join membership_claim_details mcd on a.mbr_claim_id = mcd.mbr_claim_id) mc on  mam.ins_id=mc.ins_id and mc.prvdr_id=mam.prvdr_id and mam.mbr_id=mc.mbr_id and mc.ins_id = :insId and mc.activity_month=mam.activity_month and mc.report_month= @reportMonth
 	LEFT JOIN  (SELECT mc.prvdr_id,mcd.activity_month  ,sum(mcd.membership_claims)  unwantedClaims  ,
 		CASE WHEN    mam.is_cap ='Y' then 'wanted'  else  'unwanted' end type1 
 		from membership_claims mc 
 		join membership_claim_details mcd on mc.mbr_claim_id=mcd.mbr_claim_id and  mc.report_month= @reportMonth
 		left join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mcd.activity_month =mam.activity_month
 		where     
 		case when  mam.is_cap ='Y' then 1=0 else
 		( case when mam.mbr_id is  not  null then case when mam.prvdr_id is not null then mam.activity_month is null   else mam.prvdr_id is  null end else  mam.mbr_id is   null end 	or mam.is_cap='N' )
 		 end
 	   group by mc.prvdr_id, mcd.activity_month ,type1)  uc on  uc.prvdr_id= mam.prvdr_id and  uc.activity_month =mam.activity_month 
   left join ( 
          select prvdr_id ,activity_month , sum(stoploss) stoploss from 
               (   select mc.prvdr_id,mcd.activity_month,mc.mbr_id  , sum(if ( mc.claim_type = 'INST',membership_claims ,null) ) -30000 stoploss   
 				from membership_claims mc
 				join membership_claim_details mcd on mc.mbr_claim_id=mcd.mbr_claim_id and mc.report_month= @reportMonth
 				left join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mcd.activity_month =mam.activity_month
 			   where    mam.is_cap ='Y'  
 				group by mc.prvdr_id, mcd.activity_month, mc.mbr_id having sum(if ( mc.claim_type = 'INST',membership_claims ,null)  ) -30000 > 0    
 			 )a group by prvdr_id ,activity_month 
 		) slr on  slr.prvdr_id= mam.prvdr_id and  slr.activity_month =mam.activity_month
  left join new_medical_loss_ratio nmlr on (nmlr.report_month, nmlr.activity_month ,nmlr.ins_id,nmlr.prvdr_id) = ( @reportMonth, mam.activity_month ,mam.ins_id,mam.prvdr_id) 
  where mam.activity_month >201512 
  group by  mam.ins_id,  mam.prvdr_id, mam.activity_month 
  having  if(PROF is null ,0,PROF) +if(INST is null, 0 ,INST) + if(PHAR is null, 0,PHAR)>0 ;