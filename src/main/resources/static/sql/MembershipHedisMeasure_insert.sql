	drop table if exists baseline_hedis_measure;
    
    
	create   table baseline_hedis_measure as
  SELECT a.ins_id,a.prvdr_id, a.pcpname, a.mbr_id, a.mbr_lastname,a.mbr_firstname, a.Mbr_DOB, gender ,  a.hedis_msr_rule_id, a.short_description,   
  a.duedate , cpt_or_icd, duedate2,duedate3,duedate4,duedate5,duedate6,pbm_id,pbm_flag, freq_code,is_cap,is_roster,Mbr_Status
  FROM 
  (
  select 
  hmr.ins_id,p.prvdr_id,p.name pcpname, mam.mbr_id, hmr.hedis_msr_rule_id , mam.is_cap, mam.is_roster, 
  case when dose_1 is not null then 
  case when hmr.datepart_1 = 'year' then DATE_ADD(m.mbr_dob ,INTERVAL dose_1 YEAR) 
  when hmr.datepart_1 = 'month' then DATE_ADD(m.mbr_dob ,INTERVAL dose_1 MONTH) 
  end
  else MAKEDATE(year(CONCAT(:activityMonth,'01')),1) end duedate , 
  case when dose_2 is not null then 
  case when hmr.datepart_2 = 'year' then DATE_ADD(m.mbr_dob ,INTERVAL dose_2 YEAR) 
  when hmr.datepart_2 = 'month' then DATE_ADD(m.mbr_dob ,INTERVAL dose_2 MONTH) 
  end
  else null end duedate2 ,
  case when dose_3 is not null then 
  case when hmr.datepart_3 = 'year' then DATE_ADD(m.mbr_dob ,INTERVAL dose_3 YEAR) 
  when hmr.datepart_3 = 'month' then DATE_ADD(m.mbr_dob ,INTERVAL dose_3 MONTH) 
  end
  else null end duedate3 ,
  case when dose_4 is not null then 
  case when hmr.datepart_4 = 'year' then DATE_ADD(m.mbr_dob ,INTERVAL dose_4 YEAR) 
  when hmr.datepart_4 = 'month' then DATE_ADD(m.mbr_dob ,INTERVAL dose_4 MONTH) 
  end
  else null end duedate4 , 
  case when dose_5 is not null then 
  case when hmr.datepart_5 = 'year' then DATE_ADD(m.mbr_dob ,INTERVAL dose_5 YEAR) 
  when hmr.datepart_5 = 'month' then DATE_ADD(m.mbr_dob ,INTERVAL dose_5 MONTH) 
  end
  else null end duedate5 , 
  case when dose_6 is not null then 
  case when hmr.datepart_6 = 'year' then DATE_ADD(m.mbr_dob ,INTERVAL dose_6 YEAR) 
  when hmr.datepart_6 = 'month' then DATE_ADD(m.mbr_dob ,INTERVAL dose_6 MONTH) 
  end
  else null end duedate6,
  null date_of_service, 'Y' follow_up_ind, hmr.short_description,
  case when hmr.dose_count is null then 1 else hmr.dose_count end dosecount, 
  hmr.cpt_or_icd , mam.prvdr_id prvdrid, m.Mbr_DOB,m.mbr_firstname,m.Mbr_LastName,
  hmr.dose_count, hmr.dose_1, hmr.datepart_1, hmr.dose_2, hmr.datepart_2, hmr.dose_3, hmr.datepart_3, hmr.dose_4, hmr.datepart_4, hmr.dose_5, hmr.datepart_5, hmr.dose_6, hmr.datepart_6 ,
  hmr.problem_id pbm_id, hmr.problem_flag pbm_flag, hmr.frequency_type_code freq_code, m.Mbr_Status,g.description gender 
  from 
  hedis_measure_rule hmr
  join membership_activity_month mam on mam.ins_id=hmr.ins_id   
   join membership m on mam.mbr_id =m.mbr_id 
  join provider p on p.prvdr_id = mam.prvdr_id
  inner join lu_gender g on g.gender_id = m.Mbr_GenderID
  where hmr.ins_id= :insId and hmr.effective_year= year(concat(:activityMonth,'01')) and mam.activity_month = :activityMonth and hmr.active_ind='Y' 
  and case when hmr.lower_age_limit is not null then TIMESTAMPDIFF(day,m.mbr_dob,current_date()) >= floor(hmr.lower_age_limit * 365.25) else 1= 1 end and
  case when hmr.upper_age_limit is not null then TIMESTAMPDIFF( day,m.mbr_dob,current_date()) < floor(hmr.upper_age_limit * 365.25) else 1=1 end and
  case when hmr.gender_id is not null and hmr.gender_id != 3 and hmr.gender_id != 4 then hmr.gender_id = m.mbr_genderid else 1=1 end 
  
  group by mam.mbr_id, hmr.hedis_msr_rule_id)a;
  
  
  drop table if exists hedis_due_dates;
  
  create table  hedis_due_dates
 select * from (
 select  *,duedate visitduedate  from baseline_hedis_measure where duedate is not null  group by mbr_id,hedis_msr_rule_Id,duedate
 union all
 select  *,duedate2 visitduedate from baseline_hedis_measure where duedate2 is not null  group by mbr_id,hedis_msr_rule_Id,duedate2
 union all
 select  *,duedate3 visitduedate  from baseline_hedis_measure where duedate3 is not null  group by mbr_id,hedis_msr_rule_Id,duedate3
 union all
 select  *,duedate4 visitduedate  from baseline_hedis_measure where duedate4 is not null  group by mbr_id,hedis_msr_rule_Id,duedate4
 union all
 select  * ,duedate5 visitduedate from baseline_hedis_measure where duedate5 is not null  group by mbr_id,hedis_msr_rule_Id,duedate5
 union all
 select  * ,duedate6 visitduedate from baseline_hedis_measure where duedate6 is not null  group by mbr_id,hedis_msr_rule_Id,duedate6
 )a;
 
 
  
   DELETE  mhm.* FROM  membership_hedis_measure mhm   
  join  file f2    
  join file f on case when  LOCATE( 'pharmacy', f2.file_name) > 0 then LOCATE( 'pharmacy', f.file_name) >0 
                      when  LOCATE( 'claims', f2.file_name) >0  then  LOCATE( 'claims', f.file_name)>0 
                       
                      end and mhm.file_id = f.file_id
  where f2.file_id=:fileId    and  mhm.active_ind='Y'  ;
  
  
  
 insert into membership_hedis_measure  
 select   
 null,mbr_id,hedis_msr_rule_id,greatest(ifnull(date_add(last_visit_date, interval 1 year),visitduedate),visitduedate) duedate,
 if(visitduedate > if(last_visit_date is null, date_sub( visitduedate,interval 1 day),last_visit_date),null,last_visit_date) date_of_service,
 if(visitduedate > if(last_visit_date is null, date_sub( visitduedate,interval 1 day),last_visit_date),'Y','N') follow_up_ind,
 :fileId fileId, now(),now(),:username,:username,if(visitduedate > if(last_visit_date is null, date_sub( visitduedate,interval 1 day),last_visit_date),'Y','N') activeInd
   from 
   (
   select  Mbr_lastName, Mbr_FirstName, mbr_dob,gender,bhm.hedis_msr_rule_id,bhm.ins_id,bhm.prvdr_id,bhm.is_cap,bhm.is_roster,bhm.pcpname, bhm.mbr_status, bhm.mbr_id,bhm.short_description,visitduedate, max(claim_start_date)last_visit_date 
   from hedis_due_dates bhm 
    join (
 select * from membership_activity_month where activity_month >= 201601 group by mbr_id having count(mbr_id) >3) mam on mam.mbr_id = bhm.mbr_id
  LEFT Join membership_hedis_problems mp on mp.mbr_id=bhm.mbr_id 
   LEFT join (select group_concat(cpt_id) cpt_ids , hedis_msr_rule_Id from hedis_cpt_measure group by hedis_msr_rule_Id) hcptm on hcptm.hedis_msr_rule_Id = bhm.hedis_msr_rule_Id 
   left join (select replace(im.code, '.','') icd_code , hedis_msr_rule_Id from hedis_icd_measure hicdm
   join icd_measure im on im.icd_id = hicdm.icd_id ) hicdm on hicdm.hedis_msr_rule_Id = bhm.hedis_msr_rule_Id 
   left join (select mcd.cpt_code, mcd.claim_start_date, mcd.activity_date, mc.mbr_id,mc.ins_id, mc.report_month, replace(replace(mc.Diagnoses,' ',''),'.','') Diagnoses 
   from membership_claims mc 
   join membership_claim_details mcd on mcd.mbr_claim_id = mc.mbr_claim_id
   where mc.report_month= :activityMonth ) mc on mc.mbr_id= bhm.mbr_id and mc.ins_id=:insId and 
   case when bhm.cpt_or_icd = 0 then FIND_IN_SET(mc.cpt_code, hcptm.cpt_ids) > 0 
   when bhm.cpt_or_icd = 1 then FIND_IN_SET(replace(hicdm.icd_code,'.',''), mc.Diagnoses) > 0 
   when bhm.cpt_or_icd = 2 then FIND_IN_SET(mc.cpt_code, hcptm.cpt_ids) > 0 or FIND_IN_SET(hicdm.icd_code, mc.Diagnoses) > 0 end
   where case when bhm.pbm_flag = 'Y' then bhm.pbm_id is not null and mp.pbm_id is not null and mp.resolved_date is null else 1=1 end 
   group by bhm.mbr_id, bhm.hedis_msr_rule_id ,visitduedate     
   )a;
    
