select   claimType,MFNAME firstName ,MLNAME lastName,MBRDOB dob,MBRGENDER gender,  round(sum(claims),2) unwantedClaims
from 
(
select ins_id,prvdr_id,MFNAME,MLNAME,MBRDOB,MBRGENDER,report_month,activitymonth ,claimtype, sum(netamt)  claims from unprocessed_amg_claims group by ins_id,prvdr_id,report_month,claimtype,activitymonth,SRC_SYS_MEMBER_NBR
union all
select ins_id,prvdr_id,MFNAME,MLNAME,MBRDOB,MBRGENDER ,report_month,activitymonth ,claimtype, sum(amountpaid) claims from unprocessed_amg_pharmacy_claims group by ins_id,prvdr_id,report_month,activitymonth ,SRC_SYS_MEMBER_NBR
union all
     select   mc.ins_id,mc.prvdr_id,m.mbr_lastname,m.Mbr_FirstName,m.Mbr_DOB,lg.code MBRGENDER,  mc.report_month, mcd.activity_month,   mc.claim_type,  sum(round( mcd.membership_claims  ,2)) claims
 from       membership_claims mc 
	 join membership m on m.mbr_id=mc.mbr_id and mc.report_month =:reportMonth
	 join   lu_gender lg on lg.gender_id= m.Mbr_GenderID 
     join membership_claim_details mcd on mc.mbr_claim_id=mcd.mbr_claim_id 
     left join membership_activity_month mam 
         on mam.mbr_id = mc.mbr_id and mam.ins_id=mc.ins_id and mc.prvdr_id=mam.prvdr_id and mcd.activity_month=mam.activity_month
  where mam.is_cap ='N' or case when mam.mbr_id is not null then  case when mam.prvdr_id is not null then mam.activity_month is null else mam.prvdr_id is   null end else mam.mbr_id is null end  
 group by  mc.ins_id,mc.prvdr_id,m.mbr_lastname,m.Mbr_FirstName,m.Mbr_DOB,mcd.activity_month,mc.claim_type

) a 
where prvdr_id is not null and   a.ins_id = :insId and a.prvdr_id = :prvdrId and a.report_month =:reportMonth and a.activitymonth =:activityMonth
group by ins_id,prvdr_id,claimtype,report_month,activitymonth,MFNAME,MLNAME,MBRDOB 
order by ins_id,prvdr_id,report_month,activitymonth,claimtype,MFNAME,MLNAME,MBRDOB ;


