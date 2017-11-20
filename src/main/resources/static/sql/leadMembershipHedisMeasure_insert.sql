 drop table if exists hedisMeasureByICD;
 drop table if exists hedisMeasureByCPT;
  
 create temporary table hedisMeasureByCPT as
SELECT a.lead_mbr_id,a.hedis_msr_rule_id, a.duedate ,  cpt_or_icd
 FROM 
 (
 SELECT   
 m.lead_mbr_id,  hmr.hedis_msr_rule_id ,   
 DATE_ADD(m.lead_mbr_dob, INTERVAL ceil(365.25 * ceil (datediff( current_date(),m.lead_mbr_dob)/365.25))  DAY) duedate,
 null date_of_service,
 'Y' follow_up_ind,
 case when hmr.dose_count is null then 1 else hmr.dose_count end dosecount,  
 mbrClaimCPT.cpt_id claimCPT_ID, 
 hcptm.cpt_id ruleCPT_ID, 
 cpt.code,
 hmr.cpt_or_icd ,
 m.prvdr_id prvdrid,
 m.lead_mbr_dob,
 hmr.dose_count,
 hmr.dose_1,
 hmr.datepart_1,
  hmr.dose_2,
 hmr.datepart_2,
  hmr.dose_3,
 hmr.datepart_3,
  hmr.dose_4,
 hmr.datepart_4,
  hmr.dose_5,
 hmr.datepart_5,
 hmr.dose_6,
 hmr.datepart_6
 
 FROM  hedis_measure_rule     hmr
 join (select m.*,mi.ins_id, mp.prvdr_id from lead_membership m 
           join lead_membership_insurance mi on m.lead_mbr_id =mi.lead_mbr_id 
           join lead_membership_provider mp on mp.lead_mbr_id = m.lead_mbr_id
            where mi.active_ind='Y'  and mp.active_ind='Y' group by lead_mbr_id )  m on  m.lead_mbr_id  =  :leadMbrId and  m.ins_id =hmr.ins_id  and
    case when hmr.lower_age_limit is not null then TIMESTAMPDIFF(month,m.lead_mbr_dob,current_date()) >=  hmr.lower_age_limit * 12 else 1= 1 end and
        case when hmr.upper_age_limit is not null then TIMESTAMPDIFF( month,m.lead_mbr_dob,current_date()) < hmr.upper_age_limit * 12 else 1=1 end and
        case when hmr.gender_id is not null and hmr.gender_id != 3 and  hmr.gender_id != 4 then hmr.gender_id = m.lead_mbr_genderid else 1=1 end and
         case when  hmr.dose_count is not null 
         then  
			(case when dose_1 is not null then 
				case when hmr.datepart_1 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_1 
					 when hmr.datepart_1 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_1 
				end
             else 1=1 end
            OR 
            case when dose_2 is not null then 
				case when hmr.datepart_2 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_2
					 when hmr.datepart_2 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_2 
				end
             else 1=1 end
            OR 
            case when dose_3 is not null then 
				case when hmr.datepart_3 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_3 
					 when hmr.datepart_3 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_3 
				end
             else 1=1 end
            OR 
            case when dose_4 is not null then 
				case when hmr.datepart_4 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_4 
					 when hmr.datepart_4 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_4 
				end
             else 1=1 end
            OR 
            case when dose_5 is not null then 
				case when hmr.datepart_5 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_5 
					 when hmr.datepart_5 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_5 
				end
             else 1=1 end
            OR 
            case when dose_6 is not null then 
				case when hmr.datepart_6 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_6 
					 when hmr.datepart_6 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_6 
				end
             else 1=1 end
           )
           
         else 1=1 end  
 join hedis_cpt_measure hcptm on hcptm.hedis_msr_rule_Id =  hmr.hedis_msr_rule_Id  
  JOIN cpt_measure  cpt on    hcptm.cpt_id =cpt.cpt_id  
  left outer join lu_frequency_type lft on lft.code= hmr.frequency_type_code
 left OUTER join lead_membership_claims mc on mc.lead_mbr_id= m.lead_mbr_id and mc.ins_id=m.ins_id  and mc.report_month = :reportMonth
 left OUTER join lead_membership_claim_details mcd on mcd.lead_mbr_claim_id = mc.lead_mbr_claim_id  and  case when lft.noOfDays is not null then datediff(current_date(), mcd.activity_date) <= lft.noOfDays else  1=1 end
  LEFT OUTER JOIN cpt_measure  mbrClaimCPT on mbrClaimCPT.cpt_id = mcd.cpt_code  and mbrClaimCPT.cpt_id=cpt.cpt_id 
    LEFT outer Join lead_membership_problems mp on mp.pbm_id =hmr.problem_id and mp.lead_mbr_id=m.lead_mbr_id  and  mcd.claim_start_date >= mp.start_date
      where  hmr.effective_year= 2017 and
        case when  hmr.problem_flag = 'Y' then   hmr.problem_id is not null and mp.pbm_id is not null and mp.resolved_date is null else 1=1 end  and
         hmr.cpt_or_icd in (0,2)      and 
  hmr.active_ind='Y'   
  group by m.lead_mbr_id, hmr.hedis_msr_rule_id
having      count( mbrClaimCPT.cpt_id) 	     < case when dose_count is not null then dose_count  else  1  end  
) a ;

 
alter table hedisMeasureByCPT add key  lead_mbr_id(lead_mbr_id) , add key hedis_msr_rule_id(hedis_msr_rule_id), add key cpt_or_icd(cpt_or_icd);


  create temporary table hedisMeasureByICD  as  
SELECT a.lead_mbr_id,a.hedis_msr_rule_id, a.duedate,  cpt_or_icd
 FROM 
 (
 SELECT   
 m.lead_mbr_id,  hmr.hedis_msr_rule_id ,   
 DATE_ADD(m.lead_mbr_dob, INTERVAL ceil(365.25 * ceil (datediff( current_date(),m.lead_mbr_dob)/365.25))  DAY) duedate,
 null date_of_service,
 'Y' follow_up_ind,
 case when hmr.dose_count is null then 1 else hmr.dose_count end dosecount,  
 hicdm.icd_id ruleICD_ID,
MbrClaimICD.icd_id  claimICD_ID,
 hmr.cpt_or_icd ,
 m.prvdr_id prvdrid,
 m.lead_mbr_dob,
hmr.dose_count,
 hmr.dose_1,
 hmr.datepart_1,
  hmr.dose_2,
 hmr.datepart_2,
  hmr.dose_3,
 hmr.datepart_3,
  hmr.dose_4,
 hmr.datepart_4,
  hmr.dose_5,
 hmr.datepart_5,
 hmr.dose_6,
 hmr.datepart_6


 FROM  hedis_measure_rule     hmr
 join (select m.*,mi.ins_id, mp.prvdr_id from lead_membership m 
           join lead_membership_insurance mi on m.lead_mbr_id =mi.lead_mbr_id 
           join lead_membership_provider mp on mp.lead_mbr_id = m.lead_mbr_id
            where mi.active_ind='Y' and mp.active_ind='Y' group by lead_mbr_id ) m   on  m.lead_mbr_id  =  :leadMbrId and  m.ins_id =hmr.ins_id   and
    case when hmr.lower_age_limit is not null then TIMESTAMPDIFF(month,m.lead_mbr_dob,current_date()) >=  hmr.lower_age_limit * 12 else 1= 1 end and
    case when hmr.upper_age_limit is not null then TIMESTAMPDIFF( month,m.lead_mbr_dob,current_date()) < hmr.upper_age_limit * 12 else 1=1 end and
    case when hmr.gender_id is not null and hmr.gender_id != 3 and  hmr.gender_id != 4 then hmr.gender_id = m.lead_mbr_genderid else 1=1 end and
      case when  hmr.dose_count is not null 
         then  
			(case when dose_1 is not null then 
				case when hmr.datepart_1 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_1 
					 when hmr.datepart_1 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_1 
				end
             else 1=1 end
            OR 
            case when dose_2 is not null then 
				case when hmr.datepart_2 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_2
					 when hmr.datepart_2 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_2 
				end
             else 1=1 end
            OR 
            case when dose_3 is not null then 
				case when hmr.datepart_3 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_3 
					 when hmr.datepart_3 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_3 
				end
             else 1=1 end
            OR 
            case when dose_4 is not null then 
				case when hmr.datepart_4 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_4 
					 when hmr.datepart_4 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_4 
				end
             else 1=1 end
            OR 
            case when dose_5 is not null then 
				case when hmr.datepart_5 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_5 
					 when hmr.datepart_5 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_5 
				end
             else 1=1 end
            OR 
            case when dose_6 is not null then 
				case when hmr.datepart_6 = 'year' then TIMESTAMPDIFF(year, m.lead_mbr_dob,current_date()) >= dose_6 
					 when hmr.datepart_6 = 'month' then TIMESTAMPDIFF(month, m.lead_mbr_dob,current_date()) >= dose_6 
				end
             else 1=1 end
           )
           
         else 1=1 end  
 join hedis_icd_measure hicdm on   hicdm.hedis_msr_rule_Id =  hmr.hedis_msr_rule_Id 
  JOIN icd_measure  icd  on  icd.icd_id   = hicdm.icd_id  
    left outer join lu_frequency_type lft on lft.code= hmr.frequency_type_code
 left OUTER join lead_membership_claims mc on mc.lead_mbr_id= m.lead_mbr_id and mc.ins_id=m.ins_id   and mc.report_month = :reportMonth
 left OUTER join lead_membership_claim_details mcd on mcd.lead_mbr_claim_id = mc.lead_mbr_claim_id and  case when lft.noOfDays is not null then datediff(current_date(), mcd.activity_date) <= lft.noOfDays else 1=1 end
    LEFT outer join  icd_measure MbrClaimICD on  icd.icd_id =MbrClaimICD.icd_id and INSTR(mc.Diagnoses, REPLACE(icd.code, ".",""))    
  LEFT outer join lead_membership_problems mp on mp.pbm_id =hmr.problem_id and mp.lead_mbr_id=m.lead_mbr_id  and  mcd.claim_start_date >= mp.start_date
  where hmr.effective_year= 2017 and
        case when  hmr.problem_flag = 'Y' then   hmr.problem_id is not null and mp.pbm_id is not null and mp.resolved_date is null else 1=1 end  and
        hmr.cpt_or_icd in(1,2)   and hmr.active_ind='Y'    
  group by m.lead_mbr_id, hmr.hedis_msr_rule_id 
having       count( MbrClaimICD.icd_id)     <   case when dose_count  is not null then dose_count   else  1  end
) a ;

alter table hedisMeasureByICD add key  lead_mbr_id(lead_mbr_id) , add key hedis_msr_rule_id(hedis_msr_rule_id), add key cpt_or_icd(cpt_or_icd);

drop table if exists hedisMeasureByCPT1;
create temporary table hedisMeasureByCPT1 as select * from hedisMeasureByCPT where cpt_or_icd =2;
  alter table hedisMeasureByCPT1 add key  lead_mbr_id(lead_mbr_id) , add key hedis_msr_rule_id(hedis_msr_rule_id);

drop table if exists hedisMeasureByICD1;
create temporary table hedisMeasureByICD1 as select * from hedisMeasureByICD  where cpt_or_icd =2;
alter table hedisMeasureByICD1 add key  lead_mbr_id(lead_mbr_id) , add key hedis_msr_rule_id(hedis_msr_rule_id);

update lead_membership_hedis_measure mhm
left join 
(select * from hedisMeasureByCPT  where cpt_or_icd =0
 union 
 select * from hedisMeasureByICD  where cpt_or_icd =1
 union
 (select a.* from hedisMeasureByCPT1  a 
    inner join   hedisMeasureByICD1 b using (lead_mbr_id,hedis_msr_rule_id) )
 ) a  on a.lead_mbr_id=mhm.lead_mbr_id and a.hedis_msr_rule_id=mhm.hedis_msr_rule_id 
 set active_ind='N', file_id = :fileId
 where  mhm.active_ind='Y' and case when a.lead_mbr_id is not null then  a.hedis_msr_rule_id is null else  a.lead_mbr_id is  null end;

 
INSERT INTO lead_membership_hedis_measure (
lead_mbr_id,hedis_msr_rule_id,due_date,date_of_service,follow_up_ind,created_date,updated_date,created_by,updated_by,active_ind,file_id
 ) 
select 
 a.lead_mbr_id,  a.hedis_msr_rule_id ,   a.duedate
, null date_of_service, 'Y' follow_up_ind, now() created_date,now() updated_date,'sarath' created_by, 'sarath' updated_by, 'Y'active_ind, :fileId file_id
 from 
 (select * from hedisMeasureByCPT  where cpt_or_icd =0
 union 
 select * from hedisMeasureByICD  where cpt_or_icd =1
 union
 (select a.* from hedisMeasureByCPT1  a 
    inner join   hedisMeasureByICD1 b using (lead_mbr_id,hedis_msr_rule_id) )
 ) a 
 left outer join lead_membership_hedis_measure mhm  on a.lead_mbr_id=mhm.lead_mbr_id and a.hedis_msr_rule_id=mhm.hedis_msr_rule_id and active_ind='Y'
 where   case when mhm.lead_mbr_id is not null then  mhm.hedis_msr_rule_id is null else  mhm.lead_mbr_id is  null end;

select * from lead_membership