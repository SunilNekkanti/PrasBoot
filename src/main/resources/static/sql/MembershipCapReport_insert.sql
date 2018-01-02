drop table if exists temp_membership_cap_report ;
select cast(concat(:activityMonth,'01') as date) into  @activityDate ;

create temporary table temp_membership_cap_report  as
select  
 STRING_TO_DATE(CAP_PAY_DT) CAP_PAY_DT ,  STRING_TO_DATE(CAP_PERIOD) CAP_PERIOD, PAYEE_ID, PAYEE_NAME, PCP_ID,   MEMBER_ID,  STRING_TO_DATE(YMD_FROM) MEMBEREFFDT , 
 MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
:fileId fileId,
:insId ins_id,
now() created_date,
now() updated_date,
:username created_by,
:username updated_by
  from  csv2table_cap_report c2m ;

alter table temp_membership_cap_report add key MEMBER_ID(MEMBER_ID), add key PAYEE_ID(PAYEE_ID);

drop table if exists temp_membership_cap_report_1 ;
create temporary table temp_membership_cap_report_1 
select  LAST_NAME as lastname,  FIRST_NAME as firstname,ifnull(lg.gender_id,3) sex, NULL county,
ifnull(STRING_TO_DATE(mbr_dob),  DATE_SUB(STRING_TO_DATE(CAP_PERIOD), interval floor(ifnull(MBR_AGE,0) *365.25) DAY)) dob,  2 as status, NULL, 
STRING_TO_DATE(YMD_FROM) MEMBEREFFDT,   LAST_DAY(STRING_TO_DATE(CAP_PERIOD)) MEMBERTERMDT,
greatest( STRING_TO_DATE(CAP_PERIOD),STRING_TO_DATE(YMD_FROM))  eff_start_date, -- PRVDReff_start_date
 least(
DATE_ADD(greatest( STRING_TO_DATE(CAP_PERIOD),DATE_sub(STRING_TO_DATE(YMD_FROM), interval 1 day)),interval round(MM * DAY(LAST_DAY( STRING_TO_DATE(CAP_PERIOD)))) DAY) , LAST_DAY( STRING_TO_DATE(CAP_PERIOD)))  eff_end_date, -- PRVDReff_end_date
NULL PRPRNPI , MEMBER_ID SBSB_ID, null phone, null address1, null address2, null city, null zip, 
null groupp, null class, PRODUCT_DESC PRODUCT, null PLAN, null state,
  CAP_PAY_DT ,  CAP_PERIOD, PAYEE_ID, PAYEE_NAME, PCP_ID,   MEMBER_ID,   
 MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
:fileId fileId,:insId ins_id, rc.prvdr_id prvdr_id,now() created_date,now() updated_date,:username created_by,:username updated_by
from  csv2table_cap_report c2m
left join lu_gender lg on lg.code = c2m.MBR_SEX
  left JOIN contract c on FIND_IN_SET( c2m.PCP_ID,c.PCP_PROVIDER_NBR)
  left JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id 
where rc.prvdr_id is not null and c2m.PAYEE_ID is not null and c2m.PAYEE_ID != '' 
group by SBSB_ID,prvdr_id,CAP_PERIOD,eff_start_date  ;


alter table temp_membership_cap_report_1 add key lastname(lastname), add key firstname(firstname), add key dob(dob), add key eff_start_date(eff_start_date);

insert ignore into membership (  Mbr_LastName,Mbr_FirstName,Mbr_GenderID,Mbr_CountyCode,Mbr_DOB,Mbr_Status,SRC_SYS_MBR_NBR,Mbr_MedicaidNo,file_id,created_date,updated_date,created_by,updated_by)
select a.* from  
      (
	select lastname,firstname, sex,county, 
	case when tm.dob    > current_date   then  DATE_SUB( tm.dob ,INTERVAL 100 YEAR)   else  tm.dob  end  dob ,
	tm.status,SBSB_ID,null , tm.fileId ,tm.created_date,tm.updated_date,tm.created_by,tm.updated_by   from  temp_membership_cap_report_1 tm
	 group by tm.SBSB_ID  having max(MEMBEREFFDT) 
     ) a 
	 LEFT OUTER JOIN membership m on   m.SRC_SYS_MBR_NBR=a.SBSB_ID 
	 where m.mbr_id is null ;

 update membership  m
 join  temp_membership_cap_report_1 tm on     m.SRC_SYS_MBR_NBR =  tm.SBSB_ID
  set m.mbr_dob =case when tm.dob    > current_date   then  DATE_SUB( tm.dob ,INTERVAL 100 YEAR)   else  tm.dob  end ;


 insert  into membership_insurance 
(
ins_id,mbr_id,New_Medicare_Bene_Medicaid_Flag,effective_strt_dt,effecctive_end_dt,
product,product_label,planID,SRC_SYS_MBR_NBR,groupp,class,risk_flag,file_id,created_date,updated_date,created_by,updated_by)
select  
tm.ins_id,m.mbr_id,case when m.Mbr_Status =1 then 'Y' else 'N' end as new_benefits,
tm.MEMBEREFFDT ,tm.MEMBERTERMDT ,LOBD_ID,tm.PRODUCT,tm.PLAN,tm.SBSB_ID,'FLMCD000',tm.LOBD_ID,'N' risk_flag, tm.fileId,
now() created_date,now() updated_date,:username created_by,:username updated_by
from temp_membership_cap_report_1 tm  
 join  membership m on m.SRC_SYS_MBR_NBR  =tm.SBSB_ID
  left join membership_insurance mi on m.mbr_id =mi.mbr_id and mi.ins_id=:insId   and    tm.eff_start_date >= mi.effective_strt_dt and tm.eff_end_date <= ifnull(mi.effecctive_end_dt,'2099-12-31')  
  where   case when mi.mbr_id is not  null then effective_strt_dt is null else mi.mbr_id is   null end 
  group by tm.SBSB_ID,tm.MEMBEREFFDT, tm.PRODUCT,tm.PLAN;
    
       
     
  
  insert into membership_provider (mbr_id,prvdr_id,file_id,eff_start_date,eff_end_date,created_date,updated_date,created_by,updated_by) 
select 
  m.mbr_id, tm.prvdr_id,tm.fileid,tm.eff_start_date,tm.eff_end_date, now() created_date, now() updated_date, :username created_by,:username updated_by
 from temp_membership_cap_report_1 tm
  join  membership m on m.SRC_SYS_MBR_NBR = tm.SBSB_ID 
 left outer join membership_provider mp on mp.prvdr_id= tm.prvdr_id and mp.mbr_id=m.mbr_id and mp.active_ind='Y' and  tm.eff_start_date >= mp.eff_start_date and tm.eff_end_date <= ifnull(mp.eff_end_date,'2099-12-31')
 where case when mp.mbr_id is not null then case when  mp.prvdr_id is not null then mp.eff_start_date is null else  mp.prvdr_id is null end else mp.mbr_id is  null  end 
group by tm.SBSB_ID,tm.PCP_ID,tm.eff_start_date;

  

 update membership_provider   set active_ind='N' where eff_end_date < @activityDate;
 
  
  INSERT ignore into membership_cap_report 
( CAP_PAY_DT, CAP_PERIOD, PAYEE_ID, PAYEE_NAME, ins_id, prvdr_id, mbr_id,SRC_SYS_MEMBER_NBR, eff_start_date, MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
file_id, created_date, updated_date, created_by, updated_by )
 SELECT   CAP_PAY_DT ,  date_format(CAP_PERIOD,'%Y%m'), PAYEE_ID, PAYEE_NAME, tm.ins_id,
 rc.prvdr_id,  m.mbr_id, MEMBER_ID,
MEMBEREFFDT,  MBR_RISK_POP, CAP_KEY, RATE_TIER, REC_TYPE, FUND_RATE, FUND_AMT, MM, FUND_NAME, LOBD_ID, PRODUCT_DESC,
 tm.fileId ,tm.created_date,tm.updated_date,tm.created_by,tm.updated_by 
 from   temp_membership_cap_report tm
  left join   membership  m on  m.SRC_SYS_MBR_NBR=tm.MEMBER_ID 
  left JOIN contract c on FIND_IN_SET(tm.PCP_ID ,c.PCP_PROVIDER_NBR) and @activityDate between c.start_date and c.end_date
  left JOIN reference_contracts rc on  c.contract_Id = rc.contract_Id 
where rc.prvdr_id is not null and tm.PAYEE_ID is not null and tm.PAYEE_ID != '';

 

insert into membership_activity_month (mbr_id,ins_id,prvdr_id,activity_month,is_roster,is_cap, file_id,created_date,updated_date,created_by,updated_by)
  select  cap.mbr_id, cap.ins_id, cap.prvdr_id, cap.cap_period, 'N', 'Y', :fileId,  now() , now() , :username , :username  
 from membership_cap_report cap 
 left join membership_activity_month mam on mam.activity_month=cap.cap_period and mam.mbr_id= cap.mbr_id and mam.prvdr_id=cap.prvdr_id 
 where  cap.mbr_id is not null and case when  mam.mbr_id is not null then  
				case when cap.prvdr_id is not null then   
				 mam.activity_month  is null
                 else
                  cap.prvdr_id is   null end   
          else mam.mbr_id is   null end
    and cap.cap_period = :activityMonth
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
 

drop table if exists temp_membership_cap_report  ;
drop table if exists temp_membership_cap_report_1  ;
     

