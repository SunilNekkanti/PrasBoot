INSERT INTO unprocessed_bh_claims
 (
 file_id, remarks,
 Uniuqe,MOS, MOP, MOS2, MOP2, ClaimId,ClaimLine,MemQnxtId,MemFullName,PlanId,
 Location,FacilityCode,FacilityType, BillClassCode,BillClasification,FrequencyCode,Frequency,POS,ClaimStartDate,ClaimEndDate,
 PaidDate,Admit,Discharge, admithour,RevCode,ServCode,ServCodeDesc,ClaimDetailStatus,AmountPaid,ProvId,
 ProvFullName,ProvSpecialty,Mony,DrugLabelName,MemDOB,MemAge,MemPCPFullName,MemPCPSpecialty,MedicaidId,MemEnrollId,
 PCPAffiliationId, ProvAffiliationId, GroupID, GroupName, Diagnoses, diag2, diag3, diag4, prindiag, Diag1_ICDVersion, 
 Diag2_ICDVersion, Diag3_ICDVersion, Diag4_ICDVersion, product, product_label, AllowAmt, Copay, deductible, cobamt,contractpaid,
 ineligibleamt, PharmacyName,Quantity,NPOS,RiskId,RunnDate,NDC,PHARMACY,Claims, Psychare,Simple_County, 
 CountyCode,Triangles,Cover,`Mod`,Gender,ProgramID,ContractID, ServProvName,ServProvID,ServProvSpecialty, 
 ServProvSpecialtyDesc,patientstatus,IPA,SubIPA,HICN, formtype,ReferralID, BillType,ClaimType,
 claim_cat, created_date, updated_date, created_by, updated_by, csv2table_bh_claim_id
 ) 
 select  
  :fileId,'Membership Not belong to us',
    NULL Uniuqe, 
  STRING_TO_DATE(activity_date) activity_date, 
  NULL MOP, NULL MOS2, NULL MOP2, ClaimNum, claimline, QNXTMemberID, MemberName, PlanID,
  NULL Location, NULL FacilityCode, NULL FacilityType, NULL BillClassCode, NULL BillClasification, NULL FrequencyCode, NULL Frequency, POS, 
  STRING_TO_DATE(ServiceStart) ServiceStart, 
   STRING_TO_DATE(ServiceEnd) ServiceEnd,
  STRING_TO_DATE(csv2BhClaim1.paiddate)  paiddate,
  STRING_TO_DATE(Admit)  Admit,
   STRING_TO_DATE(Discharge) Discharge,
 admithour, revcode, servcode, NULL ServCodeDesc, ParStatus, Paid, PCPID,
 PCPName, NULL ProvSpecialty, NULL Mony, drg, 
  STRING_TO_DATE(memdob) memdob, 
 NULL MemAge, PCPName, NULL MemPCPSpecialty, MedicaidNumber, EnrollID, 
 NULL PCPAffiliationId, NULL ProvAffiliationId, NULL GroupID, NULL GroupName, diag1 , diag2,  diag3, diag4, prindiag, Diag1_ICDVersion,
 Diag2_ICDVersion, Diag3_ICDVersion, Diag4_ICDVersion, product,  product_label, NULL AllowAmt, Copay,  deductible,  cobamt,  contractpaid, 
 ineligibleamt, NULL PharmacyName, Qty, NULL NPOS, risk, NULL RunnDate, NULL NDC, NULL PHARMACY, Billed, fromfile, memcounty,
 CountyCode, NULL Triangles, NULL Cover, NULL `Mod`,  Gender,  ProgramID,  ContractID,  ServProvName,  ServProvID,
 ServProvSpecialty, ServProvSpecialtyDesc,  patientstatus,  IPA,  SubIPA,  HICN,  formtype,  ReferralID,  BillType,  ClaimType, 
 claim_cat, now(), now(), 'sarath', 'sarath', csv2table_bh_claim_id
from csv2table_bh_claim1 csv2BhClaim1
 left join (select mbr_medicaidNO from membership) m  on csv2BhClaim1.MedicaidNumber = m.mbr_medicaidNO
  where m.mbr_medicaidNO is NULL;