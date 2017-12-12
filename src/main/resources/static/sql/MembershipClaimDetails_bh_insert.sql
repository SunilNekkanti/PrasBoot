  
drop table if exists temp_member_claims_full;
create  temporary table temp_member_claims_full as
    select  csv2BhClaim.*, m.mbr_id, rc.prvdr_id 
  FROM  csv2Table_BH_Claim1 csv2BhClaim  
LEFT JOIN membership m on     m.Mbr_MedicaidNo  = csv2BhClaim.MedicaidNumber 
LEFT OUTER JOIN contract c on INSTR(c.PCP_PROVIDER_NBR ,trim(csv2BhClaim.PCPID))
 LEFT OUTER JOIN reference_contract rc on  rc.insurance_id=1 and    c.ref_contract_Id = rc.ref_contract_Id   
where  m.mbr_id is not null
group by ClaimNum,claimline, m.mbr_id,rc.prvdr_id,claimline,activity_date, ServiceStart, ServiceEnd,  revcode, servcode;

  
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
  
SELECT  distinct mc.mbr_claim_id, csv2BhClaim.claimline, csv2BhClaim.ReferralID clm_line_adj_seq_nbr, 
STRING_TO_DATE(csv2BhClaim.activity_date),    
 date_format(STRING_TO_DATE(csv2BhClaim.activity_date) ,'%Y%m'),
 STRING_TO_DATE(csv2BhClaim.ServiceStart ), 
 STRING_TO_DATE(csv2BhClaim.ServiceEnd),
STRING_TO_DATE(csv2BhClaim.activity_date ),
 csv2BhClaim.revcode, cpt.cpt_id, NULL cpt_code_modifier1, null cpt_code_modifier2, csv2BhClaim.ParStatus, 
 roomType.id, null risk_recon_cos_des, csv2BhClaim.paid, null allow_amt, null co_insurance, 
 csv2BhClaim.Copay,  csv2BhClaim.deductible,csv2BhClaim.cobamt , null processing_status, null pharmacy_name, 
 NULLIF(csv2BhClaim.Qty,''), null npos , csv2BhClaim.Risk, null runndate, null ndc,
 null pharmacy, csv2BhClaim.billed, csv2BhClaim.fromfile, csv2BhClaim.memcounty, null triangles,  
null cover, now() created_date, now() updated_date, 'sarath' created_by ,'sarath' updated_by,'Y', :fileId,
 null mony, csv2BhClaim.drg, null drug_version
    FROM temp_member_claims_full csv2BhClaim  
  LEFT JOIN membership_claims mc on  mc.claim_id_number= csv2BhClaim.ClaimNum   and csv2BhClaim.mbr_id =mc.mbr_id and  mc.ins_id=:insId and mc.prvdr_id=csv2BhClaim.prvdr_id and mc.report_month=:reportMonth
  LEFT OUTER JOIN cpt_measure  cpt on cpt.code =  NULLIF(csv2BhClaim.servcode,'')
  LEFT OUTER JOIN lu_place_of_service roomType on ucase(roomtype.name) = ucase(trim(POS))
  LEFT OUTER JOIN membership_claim_details mcd on mcd.mbr_claim_id =  mc.mbr_claim_id
  WHERE csv2BhClaim.mbr_id is not null and mc.mbr_claim_id is not null and  mcd.mbr_claim_id is   null   ;

  

  
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
  