insert into membership_claim_details 
 (mbr_claim_id,claim_line_seq_nbr,clm_line_adj_seq_nbr,activity_date,activity_month,claim_start_date,claim_end_date,paid_date,revenue_code,cpt_code,cpt_code_modifier1,cpt_code_modifier2,
claim_status,location_id,srv_provider,srv_provider_name,srv_provider_code,srv_provider_desc,risk_recon_cos_des,amount_paid,allow_amt,co_insurance,co_pay,deductible,cob_paid_amount,processing_status,pharmacy_name,
quantity,npos,risk_id,runn_date,ndc,mony,drug_label_name,drug_version,pharmacy,membership_claims,psychare,simple_county,triangles,cover,
created_date,updated_date,created_by,updated_by,active_ind,file_id
 )
 SELECT distinct mc.mbr_claim_id,
 csv2AmgClaim.CLM_LINE_SEQ_NBR,csv2AmgClaim.CLM_LINE_ADJ_SEQ_NBR,
 STRING_TO_DATE(csv2AmgClaim.ACTIVITYDATE),csv2AmgClaim.ACTIVITYMONTH,
  STRING_TO_DATE(  csv2AmgClaim.DETAILSVCDATE),
  STRING_TO_DATE(csv2AmgClaim.DETAILSVCTHRUDATE),
  STRING_TO_DATE( csv2AmgClaim.CHECKDATE),
 csv2AmgClaim.REVENUECODE,cpt.cpt_id, NULLIF(csv2AmgClaim.PROCEDUREMODIFIER1,'') ,
 NULLIF(csv2AmgClaim.PROCEDUREMODIFIER2,'') ,
 csv2AmgClaim.CLAIMSTATUS,roomType.id,csv2AmgClaim.SVCPROVIDERID,csv2AmgClaim.SVCPROVFULLNM,csv2AmgClaim.SVCPROVIDERCODE,csv2AmgClaim.SVCPROVIDERDESC,csv2AmgClaim.RISK_RECON_COS_DESC, csv2AmgClaim.BILLEDAMOUNT,csv2AmgClaim.ALLOWEDAMOUNT,
 csv2AmgClaim.COINSURANCE,csv2AmgClaim.MEMBERCOPAY,csv2AmgClaim.DEDUCTIBLE,csv2AmgClaim.COBPAIDAMOUNT,
 csv2AmgClaim.PROCESSINGSTATUS,null pharmacy_name, csv2AmgClaim.QUANTITY, null npos, csv2AmgClaim.RISK_IND,
 null runn_date, null ndc, null mony,csv2AmgClaim.DRG,csv2AmgClaim.DRG_VERSION, null pharmacy,round(NETAMT,3), null psychare, null simple_county, null triangles, 
 null cover,   now() created_date, now() updated_date,'sarath' created_by ,'sarath' updated_by,'Y', :fileId
  FROM csv2Table_Amg_Claim csv2AmgClaim 
    JOIN( select * from membership_insurance group by SRC_SYS_MBR_NBR) mi on mi.SRC_SYS_MBR_NBR  = csv2AmgClaim.SRC_SYS_MEMBER_NBR and mi.ins_Id=2
  JOIN membership_claims mc on  mc.claim_id_number= csv2AmgClaim.CLAIMNUMBER and mi.mbr_id =mc.mbr_id and mc.claim_type = CLAIMTYPE  and mc.ins_id=:insId and mc.report_month = :reportMonth
  left outer join cpt_measure  cpt on cpt.code =  csv2AmgClaim.PROCEDURECODE
  LEFT OUTER JOIN lu_place_of_service roomType on roomtype.code = PLACEOFSERVICE
LEFT OUTER JOIN membership_claim_details mcd on mcd.mbr_claim_id =  mc.mbr_claim_id   
where mcd.mbr_claim_id is null ;


 insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
mc.mbr_id,mc.ins_id,mc.prvdr_id,  mcd.activity_month activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,'sarath' created_by,'sarath' updated_by 
   FROM   membership_claims mc 
  JOIN membership_claim_details mcd on  mc.mbr_claim_id = mcd.mbr_claim_id   and mc.ins_id=:insId and mc.report_month = :reportMonth
  left outer join membership_activity_month mam on mam.mbr_id=mc.mbr_id and mam.prvdr_id =mc.prvdr_id and mam.ins_id= mc.ins_id  and mam.activity_month=mcd.activity_month
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
group by mc.mbr_id,mc.ins_id,mc.prvdr_id,mcd.activity_Month; 