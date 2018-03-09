 
drop table if exists temp_member_claims_full;
create  temporary table temp_member_claims_full as
    select  csv2BhClaim.*, m.mbr_id, rc.prvdr_id 
   FROM  csv2Table_simply_Claim csv2BhClaim  
  JOIN membership m on     m.Mbr_MedicaidNo  = csv2BhClaim.MedicaidNumber 
  join membership_claims mc on mc.mbr_id = m.mbr_id and csv2BhClaim.ClaimNum = mc.claim_id_number
LEFT OUTER JOIN contract c on FIND_IN_SET(replace(csv2BhClaim.PCPID,' ',''), replace(c.PCP_PROVIDER_NBR,' ',''))
 LEFT OUTER JOIN reference_contracts rc on  rc.insurance_id=:insId and    c.contract_Id = rc.contract_Id   
where   csv2BhClaim.product_label='Medicare' 
-- group by ClaimNum,claimline, m.mbr_id,rc.prvdr_id,claimline,activity_date, ServiceStart, ServiceEnd,  revcode, servcode
;
  
 
  insert into membership_claim_details 
  (mbr_claim_id, claim_line_seq_nbr, clm_line_adj_seq_nbr,
 activity_date, activity_month, claim_start_date, claim_end_date, paid_date,
 revenue_code, cpt_code, cpt_code_modifier1, cpt_code_modifier2, claim_status,
 location_id, risk_recon_cos_des, amount_paid, allow_amt, co_insurance,
 co_pay, deductible, cob_paid_amount, processing_status, pharmacy_name,
quantity, npos, risk_id, runn_date, ndc,
pharmacy, membership_claims, psychare, simple_county, triangles,
cover, created_date, updated_date, created_by, updated_by, active_ind, file_id,
 mony, drug_label_name, drug_version
 )  
SELECT   distinct  mc.mc_mbr_claim_id, csv2BhClaim.claimline, csv2BhClaim.ReferralID clm_line_adj_seq_nbr, 
STRING_TO_DATE(csv2BhClaim.activity_date),    
 date_format(STRING_TO_DATE(csv2BhClaim.activity_date) ,'%Y%m'),
 STRING_TO_DATE(csv2BhClaim.ServiceStart ), 
 STRING_TO_DATE(csv2BhClaim.ServiceEnd),
STRING_TO_DATE(csv2BhClaim.activity_date ),
 csv2BhClaim.revcode, cpt.cpt_id, NULL cpt_code_modifier1, null cpt_code_modifier2, csv2BhClaim.ParStatus, 
 roomType.id, claim_cat risk_recon_cos_des, csv2BhClaim.paid, null allow_amt, null co_insurance, 
 csv2BhClaim.Copay,  csv2BhClaim.deductible,csv2BhClaim.cobamt , null processing_status, null pharmacy_name, 
 NULLIF(csv2BhClaim.Qty,''), null npos , csv2BhClaim.Risk, null runndate, null ndc,
 null pharmacy, csv2BhClaim.billed, csv2BhClaim.fromfile, csv2BhClaim.memcounty, null triangles,  
null cover, now() created_date, now() updated_date, :username created_by ,:username updated_by,'Y', :fileId,
 null mony, csv2BhClaim.drg, null drug_version
  FROM temp_member_claims_full csv2BhClaim  
   JOIN (
  select mc.mbr_claim_id mc_mbr_claim_id,mc.claim_id_number, mc.mbr_id,mc.ins_id,mc.prvdr_id, mc.report_month,mc.claim_type,mcd.* from  membership_claims mc 
		  LEFT  JOIN membership_claim_details mcd on mcd.mbr_claim_id =  mc.mbr_claim_id where mcd.mbr_claim_id is null)mc  on 
   (mc.report_month, mc.ins_id, mc.mbr_id, mc.claim_type, mc.claim_id_number) =   (:activityMonth,:insId,csv2BhClaim.mbr_id ,csv2BhClaim.ClaimType, csv2BhClaim.ClaimNum) 
  LEFT OUTER JOIN cpt_measure  cpt on cpt.code =  NULLIF(csv2BhClaim.servcode,'')
  LEFT OUTER JOIN lu_place_of_service roomType on ucase(roomtype.name) = ucase(trim(POS))
  WHERE csv2BhClaim.mbr_id is not null and mc.mc_mbr_claim_id is not null and  mc.mbr_claim_id is null  ;


  
   insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
mc.mbr_id,mc.ins_id,mc.prvdr_id,  mcd.activity_month activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by 
   FROM   membership_claims mc 
  JOIN membership_claim_details mcd on  mc.mbr_claim_id = mcd.mbr_claim_id   and mc.ins_id=:insId and mc.report_month = :activityMonth
  left outer join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mam.ins_id= mc.ins_id  and mam.activity_month=mcd.activity_month
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
  and mc.prvdr_id is not null   
group by mc.mbr_id,mc.ins_id,mc.prvdr_id,mcd.activity_Month; 
   
   
   
   REPLACE into membership_raf_scores  (mbr_raf_score_id, mbr_id, report_month, raf_period, activity_months, raf_score, file_id, created_date, updated_date, created_by, updated_by)
   SELECT 
   null,  m.mbr_id,  :activityMonth reportmonth,  concat(year( claim_start_date) ,'Q',quarter( claim_start_date)) raf_period,
   group_concat(distinct  mam.activity_month  ORDER BY mam.activity_month   SEPARATOR ',') activityMonths,  
   `mbr_list_calculate_risk_score_function`( m.mbr_id,:activityMonth, group_concat(distinct  mam.activity_month  ORDER BY mam.activity_month     SEPARATOR ','), 'Comm',   2016, lg.description, m.mbr_dob, 'NoMedicaid')  rafscore,
   :fileId fileId,  now() created_date, now() updated_date,:username created_by,:username updated_by  
    from membership m
  join membership_activity_month mam on m.mbr_id = mam.mbr_id
  join insurance i  on i.insurance_id = mam.ins_id
  join provider p on p.prvdr_id = mam.prvdr_id
  join lu_gender lg on lg.gender_id = m.mbr_genderid
  join ( select c.*, rc.mbr_id  from contact c  join reference_contacts rc on rc.cnt_id = c.cnt_id)c on  c.mbr_id=m.mbr_id
   join  ( select mc.report_month, mc.ins_id,mc.mbr_id , mc.Diagnoses, mcd.* from membership_claims mc  
    join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id)  mc on  (mc.report_month ,mc.ins_id,mc.mbr_id) =(:activityMonth, mam.ins_id, m.mbr_id)  and    mc.activity_month  =  mam.activity_month 
    group by m.mbr_id ,raf_period
UNION
   (select 
   null,
    m.mbr_id,
    :activityMonth  reportmonth,
    concat(year( claim_start_date) ) raf_period,
   group_concat(distinct  mam.activity_month  ORDER BY mam.activity_month     SEPARATOR ',') activityMonths,  
   `mbr_list_calculate_risk_score_function`( m.mbr_id,:activityMonth, group_concat(distinct  mam.activity_month  ORDER BY mam.activity_month     SEPARATOR ','), 'Comm',   2016, lg.description, m.mbr_dob, 'NoMedicaid')  rafscore,
   :fileId fileId,  now() created_date, now() updated_date,:username created_by,:username updated_by  
    from membership m
  join membership_activity_month mam on m.mbr_id = mam.mbr_id
  join insurance i  on i.insurance_id = mam.ins_id
  join provider p on p.prvdr_id = mam.prvdr_id
  join lu_gender lg on lg.gender_id = m.mbr_genderid
  join ( select c.*, rc.mbr_id  from contact c  join reference_contacts rc on rc.cnt_id = c.cnt_id)c on  c.mbr_id=m.mbr_id
   join  ( select mc.report_month, mc.ins_id,mc.mbr_id , mc.Diagnoses, mcd.* from membership_claims mc  
    join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id)  mc on  (mc.report_month ,mc.ins_id,mc.mbr_id) =(:activityMonth, mam.ins_id, m.mbr_id)  and    mc.activity_month  =  mam.activity_month 
    group by m.mbr_id ,raf_period
    );
 
    
  