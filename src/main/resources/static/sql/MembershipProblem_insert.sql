INSERT INTO membership_hedis_problems (
mbr_id,pbm_id,start_date,resolved_date,created_date,updated_date,
created_by,updated_by,active_ind,file_id
 )
SELECT  mc.mbr_id, p.pbm_id, min(mcd.claim_start_date) start_date, null resolved_date,
 now() created_date, now() updated_date,:username created_by ,:username updated_by,'Y', :fileId
 FROM membership_claims mc 
 JOIN membership_claim_details mcd ON mc.mbr_claim_id = mcd.mbr_claim_id and mc.report_month = :activityMonth
JOIN problems p ON p.ins_id=:insId and effective_year = year(CONCAT(mcd.activity_month,'01'))
JOIN problems_icd  pbmicd ON p.pbm_id = pbmicd.pbm_id
JOIN icd_measure  icd ON icd.icd_id = pbmicd.icd_id AND INSTR(Diagnoses, REPLACE(code, ".","")) 
LEFT OUTER JOIN membership_hedis_problems mp ON mp.mbr_id= mc.mbr_id and mp.pbm_id =p.pbm_id   
where mc.DX_TYPE_CD = 'ICD10' and  mc.report_month = :activityMonth
and case when mp.mbr_id is not null   
          then   case when  mp.pbm_id is not null 
                      then  mp.resolved_date is not null  
                      else  mp.pbm_id is  null 
                      end
           else mp.mbr_id is  null 
           end
group by mc.mbr_id, p.pbm_id