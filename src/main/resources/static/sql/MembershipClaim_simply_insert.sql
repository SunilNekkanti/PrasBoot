   
delete from csv2Table_simply_Claim where product_label like 'Medicaid';

 drop table if exists csv2Table_simply_Claim_temp;
 create temporary table csv2Table_simply_Claim_temp as
 select *   from csv2Table_simply_Claim group by MedicaidNumber;

 select :activityMonth into @reportMonth;

   insert ignore INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
  SELECT    substring_index(`MemberName`,',',1) lastname,substring_index(`MemberName`,',',-1) firstname, IF(ISNULL(lg.gender_id), 4, lg.gender_id),STRING_TO_DATE(MemDOB) dob, 4, MedicaidNumber, MedicaidNumber, '', 1,  now(),now(),:username,:username,'Y'  
  FROM csv2table_simply_claim csv2BHClaim1
 Left  join lu_gender lg on lg.code = csv2BHClaim1.Gender
 LEFT OUTER JOIN membership m on  m.SRC_SYS_MBR_NBR  = csv2BHClaim1.MedicaidNumber  
 where   m.Mbr_id is null  and product_label='Medicare'
 group by  csv2BHClaim1.MedicaidNumber ;
 
 
 
drop table if exists temp_member_claims;
create  temporary table temp_member_claims as
    select   csv2BhClaim.*, m.mbr_id, rc.prvdr_id ,
now() created_date, now() updated_date, :username created_by, :username updated_by, 'Y'active_ind, :fileId file_id
  FROM  csv2table_simply_claim csv2BhClaim  
  JOIN membership m on     m.SRC_SYS_MBR_NBR  = csv2BhClaim.MedicaidNumber 
LEFT OUTER JOIN contract c on FIND_IN_SET(replace(csv2BhClaim.PCPID,' ',''), replace(c.PCP_PROVIDER_NBR,' ','')) 
 LEFT OUTER JOIN reference_contracts rc on  rc.insurance_id=:insId and    c.contract_Id = rc.contract_Id   
where  csv2BhClaim.product_label='Medicare' ;
 
 
replace INTO membership_claims
(
claim_id_number,mbr_id,prvdr_id,ins_id,report_month,Claim_Type,facility_type_code,bill_type_code,
frequency_type_code,bill_type,dischargestatus,
MemEnrollId,Diagnoses,product_label,product_lvl1,product_lvl2,product_lvl3,product_lvl4,product_lvl5,product_lvl6,product_lvl7,
market_lvl1,market_lvl2,market_lvl3,market_lvl4,market_lvl5,market_lvl6,market_lvl7,market_lvl8,
tin,dx_type_cd,proc_type_cd,created_date,updated_date,created_by,updated_by,active_ind,file_id,pcp_prov_Id
)
SELECT 
ClaimNum,csv2bhClaim.mbr_id,csv2bhClaim.prvdr_id, :insId, :activityMonth, csv2bhClaim.ClaimType,null,null,
null,csv2bhClaim.BILLTYPE,csv2bhClaim.patientstatus,EnrollID ,
 replace(CONCAT_WS(',',NULLIF(diag1,'') , NULLIF(diag2,'') , NULLIF(diag3,''), NULLIF(diag4,'') ) ,'.','') diagnoses,
csv2bhClaim.PRODUCT_LABEL,csv2bhClaim.PRODUCT,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null, 
case when STRING_TO_DATE(csv2BhClaim.ServiceStart) >= '2015-10-01' then 'ICD10' else null end ,null,
  now(),now(),:username,:username,'Y', :fileId ,csv2bhClaim.PCPID
  FROM temp_member_claims csv2bhClaim
  join membership m on m.SRC_SYS_MBR_NBR  = csv2BHClaim.MedicaidNumber  
LEFT OUTER JOIN membership_claims mc on mc.claim_id_number =  csv2bhClaim.ClaimNum and mc.mbr_id=csv2bhClaim.mbr_id  and mc.ins_id= :insId and mc.prvdr_id=csv2bhClaim.prvdr_id and report_month= :activityMonth
   WHERE  mc.claim_id_number is null and STRING_TO_DATE(MemDOB) is not null    
GROUP BY csv2bhClaim.mbr_id,csv2bhClaim.ClaimType, ClaimNum ;


SET SESSION group_concat_max_len = 1000000;

drop table if exists temp_member_claims_full;
create  temporary table temp_member_claims_full as
    select  csv2BhClaim.*, m.mbr_id, rc.prvdr_id 
   FROM  csv2Table_simply_Claim csv2BhClaim  
  JOIN membership m on     m.SRC_SYS_MBR_NBR  = csv2BhClaim.MedicaidNumber 
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
SELECT  distinct  mc.mc_mbr_claim_id, csv2BhClaim.claimline, csv2BhClaim.ReferralID clm_line_adj_seq_nbr, 
STRING_TO_DATE(csv2BhClaim.activity_date),    
 date_format(STRING_TO_DATE(csv2BhClaim.activity_date) ,'%Y%m'),
 STRING_TO_DATE(csv2BhClaim.ServiceStart ), 
 STRING_TO_DATE(csv2BhClaim.ServiceEnd),
STRING_TO_DATE(csv2BhClaim.activity_date ),
 csv2BhClaim.revcode, cpt.cpt_id, NULL cpt_code_modifier1, null cpt_code_modifier2, csv2BhClaim.ParStatus, 
 roomType.id, claim_cat risk_recon_cos_des, csv2BhClaim.paid, null allow_amt, null co_insurance, 
 csv2BhClaim.Copay,  csv2BhClaim.deductible,csv2BhClaim.cobamt , null processing_status, null pharmacy_name, 
 NULLIF(csv2BhClaim.Qty,''), null npos , csv2BhClaim.Risk, null runndate, null ndc,
 null pharmacy, csv2BhClaim.paid, csv2BhClaim.fromfile, csv2BhClaim.memcounty, null triangles,  
null cover, now() created_date, now() updated_date, :username created_by ,:username updated_by,'Y', :fileId,
 null mony, csv2BhClaim.drg, null drug_version
  FROM temp_member_claims_full csv2BhClaim  
   JOIN (
  select mc.mbr_claim_id mc_mbr_claim_id,mc.claim_id_number, mc.mbr_id,mc.ins_id,mc.prvdr_id, mc.report_month,mc.claim_type,mcd.mbr_claim_id from  membership_claims mc 
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
   
   
   
   
   
   
   REPLACE into membership_raf_scores    
   SELECT   null, m.mbr_id,  :activityMonth,  raf_period,   activityMonths   
   ,  calculate_risk_score_function(cast(a.Diagnoses as char), 'Comm',   2016, description, mbr_dob, 'NoMedicaid', case when mbr_dob > '1953-02-01' then 'Aged' else  'Disabled' end , case when mbr_dob > '1953-02-01' then  'Originally Disabled' else ''  end )  rafscore ,
  :fileId fileId,now() created_date, now()updated_date,:username created_by ,:username  updated_by, 'Y' active
from membership m 
left  join (

select mbr_id,   description,  reportmonth,  raf_period,   activityMonths, GROUP_CONCAT(DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(t.Diagnoses, ',', sub0.aNum), ',', -1)) Diagnoses
from 
(
   SELECT lg.description, m.mbr_dob,  m.mbr_id,  :activityMonth reportmonth,  concat(year( concat(mam.activity_month,'01')) ,'Q',quarter( concat(mam.activity_month,'01'))) raf_period,
 group_concat( distinct  Diagnoses) Diagnoses, 
group_concat(distinct  mam.activity_month  ORDER BY mam.activity_month   SEPARATOR ',') activityMonths    
from membership m
join membership_activity_month mam on m.mbr_id = mam.mbr_id  and mam.ins_id=:insId
join insurance i  on i.insurance_id = mam.ins_id
join provider p on p.prvdr_id = mam.prvdr_id
join lu_gender lg on lg.gender_id = m.mbr_genderid  
left join ( select c.*, rc.mbr_id  from contact c  join reference_contacts rc on rc.cnt_id = c.cnt_id)c on  c.mbr_id=m.mbr_id  
left join  ( select mc.report_month, mc.ins_id,mc.mbr_id ,  group_concat( distinct mc.Diagnoses) Diagnoses, mcd.activity_month from membership_claims mc  
join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id group by report_month,mbr_id,activity_month)  mc on  (mc.report_month ,mc.ins_id,mc.mbr_id) =(:activityMonth, mam.ins_id, m.mbr_id)  and       mc.activity_month  =  mam.activity_month
where   mod(quarter( concat(mam.activity_month,'01')),2) =1
group by m.mbr_id ,raf_period  
) t
LEFT  JOIN
(
SELECT 1 + units.i + tens.i * 10 AS aNum, units.i + tens.i * 10 AS aSubscript
FROM (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units
CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2  UNION SELECT 3  UNION SELECT 4 ) tens
) sub0
ON (1 + LENGTH(t.Diagnoses) - LENGTH(REPLACE(t.Diagnoses, ',', ''))) >= sub0.aNum
GROUP BY    mbr_id ,raf_period 
) a on m.mbr_id =a.mbr_id
where raf_period is not null
union
( SELECT   null, m.mbr_id,  :activityMonth,  raf_period,   activityMonths   
   ,  calculate_risk_score_function(cast(a.Diagnoses as char), 'Comm',   2016, description, mbr_dob, 'NoMedicaid', case when mbr_dob > '1953-02-01' then 'Aged' else  'Disabled' end , case when mbr_dob > '1953-02-01' then  'Originally Disabled' else ''  end )  rafscore ,
   :fileId fileId,now() created_date, now()updated_date,:username created_by ,:username  updated_by, 'Y' active
from membership m 
left  join (

select mbr_id,   description,  reportmonth,  raf_period,   activityMonths, GROUP_CONCAT(DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(t.Diagnoses, ',', sub0.aNum), ',', -1)) Diagnoses
from 
(
   SELECT lg.description, m.mbr_dob,  m.mbr_id,  :activityMonth reportmonth,  concat(year( concat(mam.activity_month,'01')) ,'H',CEIL(MONTH( concat(mam.activity_month,'01'))/6 )) raf_period,
 group_concat( distinct  Diagnoses) Diagnoses, 
group_concat(distinct  mam.activity_month  ORDER BY mam.activity_month   SEPARATOR ',') activityMonths    
from membership m
join membership_activity_month mam on m.mbr_id = mam.mbr_id and mam.ins_id=:insId
join insurance i  on i.insurance_id = mam.ins_id
join provider p on p.prvdr_id = mam.prvdr_id
join lu_gender lg on lg.gender_id = m.mbr_genderid  
left join ( select c.*, rc.mbr_id  from contact c  join reference_contacts rc on rc.cnt_id = c.cnt_id)c on  c.mbr_id=m.mbr_id  
left join  ( select mc.report_month, mc.ins_id,mc.mbr_id ,  group_concat( distinct mc.Diagnoses) Diagnoses, mcd.activity_month from membership_claims mc  
join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id group by report_month,mbr_id,activity_month)  mc on  (mc.report_month ,mc.ins_id,mc.mbr_id) =(:activityMonth, mam.ins_id, m.mbr_id)  and       mc.activity_month  =  mam.activity_month
where   mod(CEIL(MONTH( concat(mam.activity_month,'01'))/6 ),2) =1
group by m.mbr_id ,raf_period  
) t
LEFT  JOIN
(
SELECT 1 + units.i + tens.i * 10 AS aNum, units.i + tens.i * 10 AS aSubscript
FROM (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units
CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2  UNION SELECT 3  UNION SELECT 4 ) tens
) sub0
ON (1 + LENGTH(t.Diagnoses) - LENGTH(REPLACE(t.Diagnoses, ',', ''))) >= sub0.aNum
GROUP BY    mbr_id ,raf_period 
) a on m.mbr_id =a.mbr_id
where raf_period is not null
)
union
(
SELECT   null, m.mbr_id,    :activityMonth,  raf_period,   activityMonths   
   
  ,  calculate_risk_score_function(cast(a.Diagnoses as char), 'Comm',   2016, description, mbr_dob, 'NoMedicaid', case when mbr_dob > '1953-02-01' then 'Aged' else  'Disabled' end , case when mbr_dob > '1953-02-01' then  'Originally Disabled' else ''  end )  rafscore ,
  :fileId fileId,now() created_date, now()updated_date,:username created_by ,:username  updated_by, 'Y' active   
from membership m 
left  join (

select mbr_id,   description,  reportmonth,  raf_period,   activityMonths, GROUP_CONCAT(DISTINCT SUBSTRING_INDEX(SUBSTRING_INDEX(t.Diagnoses, ',', sub0.aNum), ',', -1)) Diagnoses
from 
(
   SELECT lg.description, m.mbr_dob,  m.mbr_id,  :activityMonth reportmonth,  year( concat(mam.activity_month,'01'))  raf_period,
 group_concat( distinct  Diagnoses) Diagnoses, 
group_concat(distinct  mam.activity_month  ORDER BY mam.activity_month   SEPARATOR ',') activityMonths    
from membership m
join membership_activity_month mam on m.mbr_id = mam.mbr_id and mam.ins_id=:insId
join insurance i  on i.insurance_id = mam.ins_id 
join provider p on p.prvdr_id = mam.prvdr_id
join lu_gender lg on lg.gender_id = m.mbr_genderid  
left join ( select c.*, rc.mbr_id  from contact c  join reference_contacts rc on rc.cnt_id = c.cnt_id)c on  c.mbr_id=m.mbr_id  
left join  ( select mc.report_month, mc.ins_id,mc.mbr_id ,  group_concat( distinct mc.Diagnoses) Diagnoses, mcd.activity_month from membership_claims mc  
join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id group by report_month,mbr_id,activity_month)  mc on  (mc.report_month ,mc.ins_id,mc.mbr_id) =(:activityMonth, mam.ins_id, m.mbr_id)  and       mc.activity_month  =  mam.activity_month 
group by m.mbr_id ,raf_period  
) t
left  JOIN
(
SELECT 1 + units.i + tens.i * 10 AS aNum, units.i + tens.i * 10 AS aSubscript
FROM (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units
CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2  UNION SELECT 3  UNION SELECT 4 ) tens
) sub0
ON (1 + LENGTH(t.Diagnoses) - LENGTH(REPLACE(t.Diagnoses, ',', ''))) >= sub0.aNum
GROUP BY    mbr_id ,raf_period 
) a on m.mbr_id =a.mbr_id 
where raf_period is not null 
);


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




    
  
 
 
 