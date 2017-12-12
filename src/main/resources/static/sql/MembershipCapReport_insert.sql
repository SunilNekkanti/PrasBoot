
drop table if exists temp_membership_cap_report ;
create temporary table temp_membership_cap_report  as
select  
 STRING_TO_DATE(CAP_PAY_DT) CAP_PAY_DT ,  STRING_TO_DATE(CAP_PERIOD) CAP_PERIOD, PAYEE_ID, PAYEE_NAME, PCP_ID,   MEMBER_ID,  STRING_TO_DATE(YMD_FROM) MEMBEREFFDT , 
 MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
:fileId fileId,
:insId ins_id,
now() created_date,
now() updated_date,
'sarath' created_by,
'sarath' updated_by
  from  csv2table_cap_report c2m ;

alter table temp_membership_cap_report add key MEMBER_ID(MEMBER_ID),add key MM(MM);

drop table if exists temp_membership_cap_report_1 ;
create temporary table temp_membership_cap_report_1 
select  LAST_NAME as lastname,  FIRST_NAME as firstname,ifnull(lg.gender_id,3) sex, NULL county,
ifnull(STRING_TO_DATE(mbr_dob),  DATE_SUB(STRING_TO_DATE(CAP_PERIOD), interval floor(ifnull(MBR_AGE,0) *365.25) DAY)) dob,  2 as status, NULL, 
STRING_TO_DATE(YMD_FROM) MEMBEREFFDT,   LAST_DAY(STRING_TO_DATE(CAP_PERIOD)) MEMBERTERMDT,
greatest( STRING_TO_DATE(CAP_PERIOD),STRING_TO_DATE(YMD_FROM))  eff_start_date, -- PRVDReff_start_date
 least(
DATE_ADD(greatest( STRING_TO_DATE(CAP_PERIOD),DATE_sub(STRING_TO_DATE(YMD_FROM), interval 1 day)),interval round(MM * DAY(LAST_DAY( STRING_TO_DATE(CAP_PERIOD)))) DAY) , LAST_DAY( STRING_TO_DATE(CAP_PERIOD)))  eff_end_date, -- PRVDReff_end_date
NULL PRPRNPI , convert(MEMBER_ID, unsigned) SBSB_ID, null phone, null address1, null address2, null city, null zip, 
null groupp, null class, PRODUCT_DESC PRODUCT, null PLAN, null state,
 STRING_TO_DATE(CAP_PAY_DT) CAP_PAY_DT ,  STRING_TO_DATE(CAP_PERIOD) CAP_PERIOD, PAYEE_ID, PAYEE_NAME, PCP_ID,   MEMBER_ID,   
 MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
:fileId fileId,:insId ins_id, rc.prvdr_id prvdr_id,now() created_date,now() updated_date,'sarath' created_by,'sarath' updated_by
from  csv2table_cap_report c2m
left join lu_gender lg on lg.code = c2m.MBR_SEX
  left JOIN contract c on INSTR( c.PCP_PROVIDER_NBR  , convert(c2m.PCP_ID, unsigned))
  left JOIN reference_contract rc on  c.ref_contract_Id = rc.ref_contract_Id 
where rc.prvdr_id is not null and c2m.PAYEE_ID is not null and c2m.PAYEE_ID != '' 
group by SBSB_ID,prvdr_id,CAP_PERIOD,eff_start_date  ;


insert ignore into membership (  Mbr_LastName,Mbr_FirstName,Mbr_GenderID,Mbr_CountyCode,Mbr_DOB,Mbr_Status,Mbr_MedicaidNo,file_id,created_date,updated_date,created_by,updated_by)

select * from (
select lastname,firstname, sex,county, 
case when tm.dob    > current_date   then  DATE_SUB( tm.dob ,INTERVAL 100 YEAR)   else  tm.dob  end  dob ,
tm.status,null , tm.fileId ,tm.created_date,tm.updated_date,tm.created_by,tm.updated_by   from  temp_membership_cap_report_1 tm
LEFT join  membership_insurance mi on  mi.SRC_SYS_MBR_NBR=tm.SBSB_ID 
LEFT OUTER JOIN membership m on m.mbr_id = mi.mbr_id
where m.Mbr_id is null
 group by tm.SBSB_ID  having max(MEMBEREFFDT) ) a
  ON DUPLICATE KEY UPDATE
   membership.Mbr_Status =a.status ,
 membership.file_id=a.fileId;

 
 insert  into membership_insurance 
(
ins_id,mbr_id,New_Medicare_Bene_Medicaid_Flag,effective_strt_dt,effecctive_end_dt,
product,product_label,planID,SRC_SYS_MBR_NBR,groupp,class,risk_flag,file_id,created_date,updated_date,created_by,updated_by)
select 
tm.ins_id,m.mbr_id,case when m.Mbr_Status =1 then 'Y' else 'N' end as new_benefits,
tm.MEMBEREFFDT ,tm.MEMBERTERMDT ,LOBD_ID,tm.PRODUCT,tm.PLAN,tm.SBSB_ID,'FLMCD000',tm.LOBD_ID,'N' risk_flag, tm.fileId,
now() created_date,now() updated_date,'sarath' created_by,'sarath' updated_by
from temp_membership_cap_report_1 tm  
  join membership m on tm.lastname =m.Mbr_LastName  and tm.firstname =m.Mbr_FirstName and tm.dob = m.Mbr_DOB
  left join membership_insurance mi on mi.SRC_SYS_MBR_NBR =  tm.SBSB_ID 	and effective_strt_dt = tm.MEMBEREFFDT   and mi.active_ind='Y'  and mi.mbr_id=m.mbr_id							
  where   case when mi.mbr_id is not  null then effective_strt_dt is null else mi.mbr_id is   null end
  group by tm.SBSB_ID,tm.MEMBEREFFDT, tm.PRODUCT,tm.PLAN;
    
 update membership  m
 join ( select mi.mbr_id,tm.dob, tm.CAP_PAY_DT  from membership_insurance mi 
 join  temp_membership_cap_report_1 tm on     mi.SRC_SYS_MBR_NBR =  tm.SBSB_ID
 group by mi.mbr_id) tmp on tmp.mbr_id=m.mbr_id
  set m.mbr_dob =case when tmp.dob    > current_date   then  DATE_SUB( tmp.dob ,INTERVAL 100 YEAR)   else  tmp.dob  end   
 where tmp.CAP_PAY_DT > '2016-12-31';

   update membership_provider a 
    join (  select  mbr_id ,max(eff_start_date) eff_start_date from membership_provider a    group by mbr_id    having count(eff_start_date) > 1) b
    on a.mbr_id = b.mbr_id and a.eff_start_date  < b.eff_start_date
    set a.active_ind='N';
  
  
  insert into membership_provider (mbr_id,prvdr_id,file_id,eff_start_date,eff_end_date,created_date,updated_date,created_by,updated_by) 
select 
  mi.mbr_id, tm.prvdr_id,tm.fileid,tm.eff_start_date,tm.eff_end_date, now() created_date, now() updated_date, 'sarath' created_by,'sarath' updated_by
 from temp_membership_cap_report_1 tm
 join membership_insurance mi on mi.SRC_SYS_MBR_NBR = tm.SBSB_ID 
 left outer join membership_provider mp on mp.prvdr_id= tm.prvdr_id and mp.mbr_id=mi.mbr_id and mp.active_ind='Y' and mp.eff_start_date= tm.eff_start_date
 where case when mp.mbr_id is not null then case when  mp.prvdr_id is not null then mp.eff_start_date is null else  mp.prvdr_id is null end else mp.mbr_id is  null  end 
group by tm.SBSB_ID,tm.PCP_ID,tm.eff_start_date;


INSERT ignore into membership_cap_report 
( CAP_PAY_DT, CAP_PERIOD, PAYEE_ID, PAYEE_NAME, ins_id, prvdr_id, mbr_id,SRC_SYS_MEMBER_NBR, eff_start_date, MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
file_id, created_date, updated_date, created_by, updated_by )
 SELECT   CAP_PAY_DT ,  date_format(CAP_PERIOD,'%Y%m'), PAYEE_ID, PAYEE_NAME, tm.ins_id,
 rc.prvdr_id,  mi.mbr_id, MEMBER_ID,
MEMBEREFFDT,  MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
 tm.fileId ,tm.created_date,tm.updated_date,tm.created_by,tm.updated_by 
 from   temp_membership_cap_report tm
  left join (select distinct mbr_id, ins_id ,SRC_SYS_MBR_NBR from  membership_insurance mi) mi on  mi.SRC_SYS_MBR_NBR=tm.MEMBER_ID and mi.ins_id=tm.ins_id 
  left JOIN contract c on INSTR(c.PCP_PROVIDER_NBR , convert(tm.PCP_ID, unsigned))
  left JOIN reference_contract rc on  c.ref_contract_Id = rc.ref_contract_Id 
where rc.prvdr_id is not null and tm.PAYEE_ID is not null and tm.PAYEE_ID != '';


insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster,is_cap, file_id,created_date,updated_date,created_by,updated_by)
  select  cap.mbr_id, cap.ins_id, cap.prvdr_id, cap.cap_period, 'N', 'Y', :fileId,  now() , now() , 'sarath' , 'sarath'   
 from membership_cap_report cap 
 left join membership_activity_month mam on mam.activity_month=cap.cap_period and mam.mbr_id= cap.mbr_id and mam.prvdr_id=cap.prvdr_id 
 where  case when  mam.mbr_id is not null then  
				case when cap.prvdr_id is not null then   
				 mam.activity_month  is null
                 else
                  cap.prvdr_id is   null end   
          else mam.mbr_id is   null end
    and cap.mbr_id is not null and cap.cap_period <= :activityMonth
   group by cap.mbr_id, cap.ins_id, cap.prvdr_id, cap.cap_period
   having sum(cap.mm) > 0;
   
   
   update membership_activity_month mam
join 
(select ins_id, mbr_id ,prvdr_id,cap_period ,sum(mm)   from membership_cap_report  group by ins_id,mbr_id ,prvdr_id,cap_period having sum(mm) <= 0 )capMember  
on  mam.mbr_id=capMember.mbr_id  and mam.prvdr_id= capMember.prvdr_id and mam.activity_month=capMember.cap_period and mam.ins_id=capMember.ins_id
set is_cap='N';

update membership_activity_month mam
join 
(select  ins_id,mbr_id ,prvdr_id,cap_period ,sum(mm)   from membership_cap_report  group by ins_id,mbr_id ,prvdr_id,cap_period having sum(mm)  > 0 )capMember  
 on   mam.mbr_id=capMember.mbr_id and mam.prvdr_id= capMember.prvdr_id and mam.activity_month=capMember.cap_period and mam.ins_id=capMember.ins_id
set is_cap='Y';
 
