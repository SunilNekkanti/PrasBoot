
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

drop table if exists csv2Table_Amg_Claim_details;
   
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
 SELECT distinct csv2AmgClaim.mbr_claim_id,
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
  JOIN (
 select mc.mbr_claim_id mc_mbr_claim_id,mc.claim_id_number, mc.mbr_id,mc.ins_id,mc.prvdr_id, mc.report_month,mc.claim_type,mcd.* from  membership_claims mc 
		  LEFT  JOIN membership_claim_details mcd on mcd.mbr_claim_id =  mc.mbr_claim_id where mcd.mbr_claim_id is null)mc  on 
  (mc.report_month, mc.ins_id, mc.mbr_id, mc.claim_type, mc.claim_id_number) =   (:activityMonth,:insId,csv2AmgClaim.mbr_id ,csv2AmgClaim.ClaimType, csv2AmgClaim.CLAIMNUMBER) 
  left outer join cpt_measure  cpt on cpt.code =  csv2AmgClaim.PROCEDURECODE
  LEFT OUTER JOIN lu_place_of_service roomType on roomtype.code = csv2AmgClaim.PLACEOFSERVICE
where csv2AmgClaim.mbr_id is not null  and mc.mc_mbr_claim_id is not null and  mc.mbr_claim_id is null;



 insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select  distinct
csv2AmgClaim.mbr_id,csv2AmgClaim.ins_id,csv2AmgClaim.prvdr_id,  mcd.activity_month activityMonth,'N', 'N',:fileId fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by 
   FROM   csv2Table_Amg_Claim_details csv2AmgClaim 
   join membership_claim_details mcd on csv2AmgClaim.mbr_claim_id = mcd.mbr_claim_id
  left outer join membership_activity_month mam on mam.mbr_id=csv2AmgClaim.mbr_id and mam.prvdr_id =csv2AmgClaim.prvdr_id and mam.ins_id=csv2AmgClaim.ins_id  and mam.activity_month=mcd.activity_month
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
group by csv2AmgClaim.mbr_id,csv2AmgClaim.ins_id,csv2AmgClaim.prvdr_id,mcd.activity_Month; 

 

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
                        		  	 LEFT JOIN (select a.mbr_id,a.claim_type,a.prvdr_id,a.ins_id,a.report_month,mcd.activity_month,mcd.membership_claims from membership_claims a join membership_claim_details mcd on a.mbr_claim_id = mcd.mbr_claim_id) mc on  mam.ins_id=mc.ins_id and mc.prvdr_id=mam.prvdr_id and mam.mbr_id=mc.mbr_id and mc.ins_id = :insId and mc.activity_month=mam.activity_month and mc.report_month= @reportMonth
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
 
 
 
drop table if exists csv2Table_Amg_Claim_details;
drop table if exists csv2Table_Amg_Claim_temp;
 