
  drop table if exists csv2table_simply_member_level_temp;
 create temporary table csv2table_simply_member_level_temp as
 select *   from csv2table_simply_member_level  where product_label='Medicare' ;
 alter table csv2table_simply_member_level_temp add key MedicaidNumber(MedicaidNumber);

 select :activityMonth into @reportMonth;
 
 INSERT INTO membership ( Mbr_LastName,Mbr_FirstName,Mbr_GenderID, Mbr_DOB, Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,Mbr_MedicareNo,file_id,created_date,updated_date,created_by,updated_by	,active_ind )
 SELECT   substring_index(`MemberName`,',',1) lastname,substring_index(`MemberName`,',',-1) firstname, 4 ,STRING_TO_DATE(DateofBirth) dob, 4, MedicaidNumber, MedicaidNumber, '',:fileId,  now(),now(),:username,:username,'Y'  
  FROM csv2table_simply_member_level_temp mbrlevel
 LEFT OUTER JOIN membership m on    m.SRC_SYS_MBR_NBR = mbrlevel.MedicaidNumber
 where m.mbr_id is null  group by mbrlevel.MedicaidNumber;
   
replace into simply_member_level_summary  select  null,  :activityMonth,  :insId, c.prvdr_id, m.Mbr_Id,
IPA	,SubIPA	,QNXTMemberID	,MemberName	,EnrollID	,PlanID	,ProgramID	,ContractID	,MedicaidNumber	,HICN	,Gender	,DateofBirth	,CountyCode	,
PcpId	,PcpName	,Activity_Date	,Hospice	,ESRD	,Aged_Disabled_MSP	,Institutional	,NHC	,New_Medicare_Bene_Medicaid_Flag	,LTI_Flag,
Medicaid_Indicator	,Default_Risk_Factor_Code	,Risk_Adjuster_Factor_A	,Risk_Adjuster_Factor_B	,ESRD_MSP_Flag	,Frailty_Indicator	,Enrollment_Source	,EGHP_Flag	,Part_D_RA_Factor	,
Medicaid_Premium	,Payment_Adj_MSA_Start_Date	,Total_Part_A_MA_Payment	,Total_Part_B_MA_Payment	,Total_MA_Payment	,Rebate_Part_DBasicPremium_Reduct	,Part_D_Basic_Premium_Amt	,
Part_D_DirectSubsidy_Payment_Amt	,Reinsurance_Subsidy_Amt	,LowIncomeSubsidyCostsharing_Amt	,Total_Part_D_Payment	,premium	,Funding	,INST	,PROF	,PHARM	,OTC	,
PCPCap	,speccap	,ibnr	,MSOAdmin	,Surpluspct	,Deficitpct	,Contribution	,Reserve	,StoplossRate	,StoplossCredit	,Adjustments	,RiskType	,RiskEffDate	,ipadistribution	,
hmodistribution	,memcounty	,product	,product_label	,risk	,carrierid	,carrier_description	,progtype	,program	,planDesc	,MemId	,carriermemid	,PayToAffiliation	,
PayToId	,PayToName	,ServLocAffiliation	,ServiceLocationId	,ServiceLocationName,:fileId, now(),now(),:username,:username,'Y'
  from csv2table_simply_member_level_temp ml 
  join membership m on m.Mbr_MedicaidNo = ml.MedicaidNumber
 LEFT JOIN  ( select c.*,rc.prvdr_id,rc.insurance_id from contract c 
  JOIN reference_contracts rc  on   c.contract_Id = rc.contract_Id   and  rc.insurance_id=:insId) c on FIND_IN_SET(ml.PCPID,c.PCP_PROVIDER_NBR) and cast(Activity_Date as date) between  start_date and end_date   ;

 

 replace into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster, is_cap,file_id,created_date,updated_date,created_by,updated_by)
select   
m.mbr_id,c.insurance_id,c.prvdr_id,  date_format( Activity_Date,'%Y%m'),'N', 'N',:fileId fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by 
   FROM    csv2table_simply_member_level_temp csv2AmgMbrLvl 
   join membership m on m.SRC_SYS_MBR_NBR  =  csv2AmgMbrLvl.MedicaidNumber  
   JOIN  ( select c.*,rc.insurance_id,rc.prvdr_id from contract c 
                        JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id  and insurance_id =:insId
				 ) c on FIND_IN_SET(csv2AmgMbrLvl.PcpId, c.PCP_PROVIDER_NBR ) and cast(Activity_Date as date) between  start_date and end_date 
  left outer join membership_activity_month mam on mam.mbr_id=m.mbr_id and mam.prvdr_id =c.prvdr_id and mam.ins_id=c.insurance_id  and mam.activity_month=date_format( Activity_Date,'%Y%m') 
  where   if( mam.mbr_id is not null , if(mam.prvdr_id is not null ,mam.activity_month is null,mam.prvdr_id is  null ),mam.mbr_id is   null)
group by m.mbr_id,c.insurance_id,c.prvdr_id,date_format( Activity_Date,'%Y%m');

 
replace  into new_medical_loss_ratio
( ins_id, prvdr_id, report_month, activity_month, qmlr, amg_funding, amg_mbr_cnt, amg_inst, amg_prof, amg_phar, amg_sl_exp, amg_sl_credit, amg_vab_adj, amg_adj, amg_pcp_cap, amg_spec_cap, amg_dental_cap, amg_trans_cap, amg_vision_cap, amg_ibnr_inst, amg_ibnr_prof,  file_id, created_date, updated_date, created_by, updated_by, active_ind)
 select  c.insurance_id,c.prvdr_id , :activityMonth reportmonth,date_format(Activity_Date,'%Y%m') ACTIVITYMONTH, 
  qmlrfunction(:activityMonth, c.insurance_id,c.prvdr_id,date_format(Activity_Date,'%Y%m') ,false,false) qmlr,
sum(premium *.84) FUNDING, count(distinct MedicaidNumber)MEMBER_MONTH_CNT, sum( ml.INST)INST_CLAIMS, sum( ml.PROF)PROF_CLAIMS, sum( ml.PHARM)PHAR_CLAIMS,
 sum(StoplossRate)STOPLOSS_CHARGE, sum(ml.StoplossCredit)STOPLOSS_CREDIT_AMT, 0 VAB_ADJUSTMENT, sum(Adjustments)  ADJUSTMENT, sum( ml.PCPCap)CAPITATION_PCP,
 sum(ml.speccap)CAPITATION_SPECIALIST, 0 CAPITATION_DENTAL, 0 CAPITATION_TRANSPORTATION,
 0 CAPITATION_VISION, ifnull(ml.ibnr,0), 0,    :fileId fileId, now() created,now() updated, :username created_by , :username updated_by, 'Y' active
 from csv2table_simply_member_level_temp  ml 
JOIN  ( select c.*,rc.insurance_id,rc.prvdr_id from contract c 
                        JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id  and insurance_id =:insId
				 ) c on FIND_IN_SET(ml.PCPID, c.PCP_PROVIDER_NBR)       
  left join new_medical_loss_ratio mlr on mlr.report_month =:activityMonth and mlr.ins_id =c.insurance_id and mlr.prvdr_id =c.prvdr_id and mlr.activity_month = date_format(ml.Activity_Date,'%Y%m')
  where date_format(ml.Activity_Date,'%Y%m') >201512
 group by  reportmonth,c.insurance_id,c.prvdr_id ,ACTIVITYMONTH
 order by  reportmonth,c.insurance_id,c.prvdr_id ,ACTIVITYMONTH;

  
 