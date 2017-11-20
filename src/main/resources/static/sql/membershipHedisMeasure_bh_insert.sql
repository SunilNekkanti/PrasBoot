 drop table if exists hedisMeasureByICD;
 drop table if exists hedisMeasureByCPT;
  
 create temporary table hedisMeasureByCPT as
SELECT a.mbr_id,a.hedis_msr_rule_id, a.duedate,  cpt_or_icd
 FROM 
 (
 SELECT   
 mi.mbr_id,  hmr.hedis_msr_rule_id ,   
 DATE_ADD(m.mbr_dob, INTERVAL ceil(365.25 * ceil (datediff( current_date(),m.mbr_dob)/365.25))  DAY) duedate,
 null date_of_service,
 'Y' follow_up_ind,
 case when hmr.dose_count is null then 1 else hmr.dose_count end dosecount,  
 mbrClaimCPT.cpt_id claimCPT_ID, 
 hcptm.cpt_id ruleCPT_ID, 
 cpt.code,
 hmr.cpt_or_icd ,
 mprvdr.prvdr_id prvdrid,
 m.Mbr_DOB,
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
 hmr.datepart_5


 FROM  hedis_measure_rule     hmr
 join  membership_insurance mi  on mi.ins_id = hmr.ins_id and hmr.ins_id= :insId
 join membership m on mi.mbr_id =m.mbr_id  and 
    case when hmr.lower_age_limit is not null then TIMESTAMPDIFF(year,m.mbr_dob,current_date()) >=  hmr.lower_age_limit else 1= 1 end and
        case when hmr.upper_age_limit is not null then TIMESTAMPDIFF( year,m.mbr_dob,current_date()) < hmr.upper_age_limit else 1=1 end and
        case when hmr.gender_id is not null and hmr.gender_id != 3 and  hmr.gender_id != 4 then hmr.gender_id = m.mbr_genderid else 1=1 end and
        case when  hmr.dose_count is not null 
         then  
			(case when dose_1 is not null then 
				case when hmr.datepart_1 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_1 
					 when hmr.datepart_1 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_1 
				end
             else 1=1 end
            OR 
            case when dose_2 is not null then 
				case when hmr.datepart_2 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_2
					 when hmr.datepart_2 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_2 
				end
             else 1=1 end
            OR 
            case when dose_3 is not null then 
				case when hmr.datepart_3 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_3 
					 when hmr.datepart_3 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_3 
				end
             else 1=1 end
            OR 
            case when dose_4 is not null then 
				case when hmr.datepart_4 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_4 
					 when hmr.datepart_4 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_4 
				end
             else 1=1 end
            OR 
            case when dose_5 is not null then 
				case when hmr.datepart_5 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_5 
					 when hmr.datepart_5 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_5 
				end
             else 1=1 end
            
           )
           
         else 1=1 end  
 join membership_provider mprvdr  on  mi.mbr_id=mprvdr.mbr_id
 join hedis_cpt_measure hcptm on hcptm.hedis_msr_rule_Id =  hmr.hedis_msr_rule_Id  
  JOIN cpt_measure  cpt on    hcptm.cpt_id =cpt.cpt_id  
  left outer join lu_frequency_type lft on lft.code= hmr.frequency_type_code
 left OUTER join membership_claims mc on mc.mbr_id= mi.mbr_id and mc.ins_id=mi.ins_id and mc.report_month=:reportMonth
 left OUTER join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id  and  case when lft.noOfDays is not null then datediff(current_date() , mcd.activity_date) <= lft.noOfDays else  datediff(current_date(), mcd.activity_date) <=365 end
  LEFT OUTER JOIN cpt_measure  mbrClaimCPT on mbrClaimCPT.cpt_id = mcd.cpt_code  and mbrClaimCPT.cpt_id=cpt.cpt_id 
    LEFT outer Join membership_problems mp on mp.pbm_id =hmr.problem_id and mp.mbr_id=mi.mbr_id  and  mcd.claim_start_date >= mp.start_date
      where  hmr.effective_year= 2016 and
        case when  hmr.problem_flag = 'Y' then   hmr.problem_id is not null and mp.pbm_id is not null and mp.resolved_date is null else 1=1 end  and
         hmr.cpt_or_icd in (0,2)      and 
  hmr.active_ind='Y'   and mprvdr.active_ind='Y' and mi.active_ind='Y'  
  group by mi.mbr_id, hmr.hedis_msr_rule_id 
having      count( mbrClaimCPT.cpt_id) 	     < case when dose_count is not null then dose_count  else  1 end 
) a ;

alter table hedisMeasureByCPT add key  mbr_id(mbr_id) , add key hedis_msr_rule_id(hedis_msr_rule_id), add key cpt_or_icd(cpt_or_icd);


  create temporary table hedisMeasureByICD  as  
SELECT a.mbr_id,a.hedis_msr_rule_id, a.duedate,  cpt_or_icd
 FROM 
 (
 SELECT   
 mi.mbr_id,  hmr.hedis_msr_rule_id ,   
 DATE_ADD(m.mbr_dob, INTERVAL ceil(365.25 * ceil (datediff( current_date(),m.mbr_dob)/365.25))  DAY) duedate,
 null date_of_service,
 'Y' follow_up_ind,
 case when hmr.dose_count is null then 1 else hmr.dose_count end dosecount,  
 hicdm.icd_id ruleICD_ID,
MbrClaimICD.icd_id  claimICD_ID,
 hmr.cpt_or_icd ,
 mprvdr.prvdr_id prvdrid,
 m.Mbr_DOB,
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
 hmr.datepart_5

 FROM  hedis_measure_rule     hmr
 join  membership_insurance mi  on mi.ins_id = hmr.ins_id and hmr.ins_id= :insId
  join membership m on mi.mbr_id =m.mbr_id  and 
    case when hmr.lower_age_limit is not null then TIMESTAMPDIFF(year,m.mbr_dob,current_date()) >=  hmr.lower_age_limit else 1= 1 end and
        case when hmr.upper_age_limit is not null then TIMESTAMPDIFF( year,m.mbr_dob,current_date()) < hmr.upper_age_limit else 1=1 end and
  case when hmr.gender_id is not null and hmr.gender_id != 3 and  hmr.gender_id != 4 then hmr.gender_id = m.mbr_genderid else 1=1 end and
      case when  hmr.dose_count is not null 
         then  
			(case when dose_1 is not null then 
				case when hmr.datepart_1 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_1 
					 when hmr.datepart_1 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_1 
				end
             else 1=1 end
            OR 
            case when dose_2 is not null then 
				case when hmr.datepart_2 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_2
					 when hmr.datepart_2 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_2 
				end
             else 1=1 end
            OR 
            case when dose_3 is not null then 
				case when hmr.datepart_3 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_3 
					 when hmr.datepart_3 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_3 
				end
             else 1=1 end
            OR 
            case when dose_4 is not null then 
				case when hmr.datepart_4 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_4 
					 when hmr.datepart_4 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_4 
				end
             else 1=1 end
            OR 
            case when dose_5 is not null then 
				case when hmr.datepart_5 = 'year' then TIMESTAMPDIFF(year, m.mbr_dob,current_date()) >= dose_5 
					 when hmr.datepart_5 = 'month' then TIMESTAMPDIFF(month, m.mbr_dob,current_date()) >= dose_5 
				end
             else 1=1 end
            
           )
           
         else 1=1 end  
 join membership_provider mprvdr  on  mi.mbr_id=mprvdr.mbr_id
  join hedis_icd_measure hicdm on   hicdm.hedis_msr_rule_Id =  hmr.hedis_msr_rule_Id 
  JOIN icd_measure  icd  on  icd.icd_id   = hicdm.icd_id  
    left outer join lu_frequency_type lft on lft.code= hmr.frequency_type_code
 left OUTER join membership_claims mc on mc.mbr_id= mi.mbr_id and mc.ins_id=mi.ins_id and mc.report_month=:reportMonth
 left OUTER join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id and  case when lft.noOfDays is not null then datediff(current_date(), mcd.activity_date) <= lft.noOfDays else datediff(current_date() , mcd.activity_date) <=365 end
    LEFT outer join  icd_measure MbrClaimICD on   icd.icd_id =MbrClaimICD.icd_id and INSTR(REPLACE(mc.Diagnoses, ".",""), REPLACE(icd.code, ".",""))   
  LEFT outer Join membership_problems mp on mp.pbm_id =hmr.problem_id and mp.mbr_id=mi.mbr_id  and  mcd.claim_start_date >= mp.start_date
  where hmr.effective_year= 2016 and
        case when  hmr.problem_flag = 'Y' then   hmr.problem_id is not null and mp.pbm_id is not null and mp.resolved_date is null else 1=1 end  and
        hmr.cpt_or_icd in(1,2)   and hmr.active_ind='Y'   and mprvdr.active_ind='Y' and mi.active_ind='Y'  
  group by mi.mbr_id, hmr.hedis_msr_rule_id 
having       count( MbrClaimICD.icd_id)     <  case when dose_count  is not null then dose_count   else  1  end   
) a ;

alter table hedisMeasureByICD add key  mbr_id(mbr_id) , add key hedis_msr_rule_id(hedis_msr_rule_id), add key cpt_or_icd(cpt_or_icd);

drop table if exists hedisMeasureByCPT1;
create temporary table hedisMeasureByCPT1 as select * from hedisMeasureByCPT where cpt_or_icd =2;
 

drop table if exists hedisMeasureByICD1;
create temporary table hedisMeasureByICD1 as select * from hedisMeasureByICD  where cpt_or_icd =2;


INSERT INTO membership_hedis_measure (
mbr_id,hedis_msr_rule_id,due_date,date_of_service,follow_up_ind,created_date,updated_date,created_by,updated_by,active_ind,file_id
 ) 
select 
 a.mbr_id,  a.hedis_msr_rule_id ,   a.duedate
, null date_of_service, 'Y' follow_up_ind, now() created_date,now() updated_date,'sarath' created_by, 'sarath' updated_by, 'Y'active_ind, :fileId file_id
 from 
 (select * from hedisMeasureByCPT  where cpt_or_icd =0
 union 
 select * from hedisMeasureByICD  where cpt_or_icd =1
 union
 (select a.* from hedisMeasureByCPT1  a 
    inner join   hedisMeasureByICD1 b using (mbr_id,hedis_msr_rule_id) )
 ) a 
 left outer join membership_hedis_measure mhm  on a.mbr_id=mhm.mbr_id and a.hedis_msr_rule_id=mhm.hedis_msr_rule_id
 where   case when mhm.mbr_id is not null then  mhm.hedis_msr_rule_id is null else  mhm.mbr_id is  null end;
