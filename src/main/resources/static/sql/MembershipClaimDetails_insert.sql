drop table if exists csv2Table_Amg_Claim_details;

select :activityMonth into @reportMonth;
   
create temporary table csv2Table_Amg_Claim_details
as 
select csv2AmgClaim.*,mc.mbr_claim_id,mc.mbr_id,mc.prvdr_id,mc.ins_id,mc.claim_type,mc.claim_id_number
  FROM csv2Table_Amg_Claim csv2AmgClaim 
    JOIN membership m on m.SRC_SYS_MBR_NBR  = csv2AmgClaim.SRC_SYS_MEMBER_NBR 
  JOIN membership_claims mc on  (mc.report_month,mc.ins_id,mc.mbr_id, mc.claim_type,mc.claim_id_number)  = (@reportMonth,:insId,m.mbr_id, CLAIMTYPE , csv2AmgClaim.CLAIMNUMBER) ;
  
  alter table csv2Table_Amg_Claim_details add key PROCEDURECODE(PROCEDURECODE), add key PLACEOFSERVICE(PLACEOFSERVICE),add key mbr_claim_id(mbr_claim_id),
  add key mbr_id(mbr_id),add key prvdr_id(prvdr_id),add key ins_id(ins_id);
  
  
   
insert into membership_claim_details 
 (mbr_claim_id,claim_line_seq_nbr,clm_line_adj_seq_nbr,activity_date,activity_month,claim_start_date,claim_end_date,paid_date,revenue_code,cpt_code,cpt_code_modifier1,cpt_code_modifier2,
claim_status,location_id,srv_provider,srv_provider_name,srv_provider_code,srv_provider_desc,risk_recon_cos_des,amount_paid,allow_amt,co_insurance,co_pay,deductible,cob_paid_amount,processing_status,pharmacy_name,
quantity,npos,risk_id,runn_date,ndc,mony,drug_label_name,drug_version,pharmacy,membership_claims,psychare,simple_county,triangles,cover,
created_date,updated_date,created_by,updated_by,active_ind,file_id
 )
 SELECT  csv2AmgClaim.mbr_claim_id,
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
 null cover,   now() created_date, now() updated_date,:username created_by ,:username updated_by,'Y', :fileId
  FROM csv2Table_Amg_Claim_details csv2AmgClaim 
  left outer join cpt_measure  cpt on cpt.code =  csv2AmgClaim.PROCEDURECODE
  LEFT OUTER JOIN lu_place_of_service roomType on roomtype.code = csv2AmgClaim.PLACEOFSERVICE
LEFT OUTER JOIN membership_claim_details mcd on mcd.mbr_claim_id =  csv2AmgClaim.mbr_claim_id   
where mcd.mbr_claim_id is null ;


 insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
csv2AmgClaim.mbr_id,csv2AmgClaim.ins_id,csv2AmgClaim.prvdr_id,  mcd.activity_month activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by 
   FROM   csv2Table_Amg_Claim_details csv2AmgClaim 
   join membership_claim_details mcd on csv2AmgClaim.mbr_claim_id = mcd.mbr_claim_id
  left outer join membership_activity_month mam on mam.mbr_id=csv2AmgClaim.mbr_id and mam.prvdr_id =csv2AmgClaim.prvdr_id and mam.ins_id=csv2AmgClaim.ins_id  and mam.activity_month=mcd.activity_month
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
group by csv2AmgClaim.mbr_id,csv2AmgClaim.ins_id,csv2AmgClaim.prvdr_id,mcd.activity_Month; 


 
drop table if exists csv2Table_Amg_Claim_details;
drop table if exists csv2Table_Amg_Claim_temp;
 