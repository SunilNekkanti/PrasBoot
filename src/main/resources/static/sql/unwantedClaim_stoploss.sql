    select   claim_type claimType,m.Mbr_LastName lastName,m.mbr_firstname firstName,m.mbr_dob dob ,lg.code gender ,  round(sum(membership_claims),2)-30000 unwantedClaims  
     from membership_claims mc ,membership_claim_details mcd, membership m , lu_gender lg where mc.mbr_claim_id=mcd.mbr_claim_id  and claim_type = 'INST'    and mc.mbr_id=m.mbr_id
     and      ins_id = :insId and  prvdr_id = :prvdrId and  report_month = :reportMonth and  activity_month = :activityMonth and lg.gender_id= m.Mbr_GenderID 
 group by  ins_id,prvdr_id, m.Mbr_LastName,m.mbr_firstname,m.mbr_dob,  mcd.activity_month
 having sum(membership_claims) > 30000 
 order by mc.ins_id,mc.prvdr_id,mc.report_month,mcd.activity_month,mc.claim_type,m.Mbr_LastName,m.mbr_firstname ,m.mbr_dob  ;