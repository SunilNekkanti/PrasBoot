Select name, report_month,activity_month, risk_recon_cos_des, claim_type ,is_roster,is_cap, claims from provider prvdr
inner join (

select   prvdr_id, report_month,activity_month, risk_recon_cos_des, claim_type ,is_roster,is_cap, sum(membership_claims) as claims

from (

                select distinct  mam.mbr_id,mam.ins_id,mam.prvdr_id,mc.report_month,mc.claim_type,mc.claim_id_number, mam.is_roster,mam.is_cap,  mcd.*  from  membership_activity_month mam

                                left join membership_claims mc on mc.mbr_id=mam.mbr_id and mc.prvdr_id=mam.prvdr_id and mc.ins_id=mam.ins_id

                                left join membership_claim_details mcd on mcd.mbr_claim_id=mc.mbr_claim_id and mcd.activity_month=mam.activity_month

         join risk_recon rc on  rc.name= mcd.risk_recon_cos_des and  risk_recon_id = :category 

  where mam.ins_id =:insId   and  report_month = :repMonth

  and case when 'isCap' = 'isCap' then is_cap='Y' else   1=1 end

  and case when 'isCap'   ='isRoster' then is_roster='Y' else 1=1 end

 

 

  UNION

                select  distinct  mc.mbr_id,mc.ins_id,mc.prvdr_id,mc.report_month,mc.claim_type,mc.claim_id_number,mam.is_roster,mam.is_cap,mcd.*  from  membership_claims mc

                                                join membership_claim_details mcd on mcd.mbr_claim_id=mc.mbr_claim_id 

                                left join membership_activity_month mam on mcd.activity_month=mam.activity_month and mc.mbr_id=mam.mbr_id and mc.prvdr_id=mam.prvdr_id and mc.ins_id=mam.ins_id

         join risk_recon rc on  rc.name= mcd.risk_recon_cos_des and   risk_recon_id = :category 

  where mam.ins_id = :insId     and  report_month = :repMonth 

  and case when 'isCap' = 'isCap' then is_cap='Y' else   1=1 end

  and case when 'isCap'   ='isRoster' then is_roster='Y' else 1=1 end

  )a

  join risk_recon rc on  rc.name= a.risk_recon_cos_des and  risk_recon_id = :category 
  
  where activity_month =:activityMonth

  group by   prvdr_id,   report_month
 )b on b.prvdr_id = prvdr.prvdr_id
