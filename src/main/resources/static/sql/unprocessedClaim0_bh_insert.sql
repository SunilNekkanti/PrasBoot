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
  Uniuqe, 
  case when MOS <> '' then cast(concat(MOS,'-01') as date) else NULL end MOS, 
  case when MOS <> '' then cast(concat(MOP,'-01') as date) else NULL end MOP, 
  STRING_TO_DATE(MOS2)  MOS2, 
   STRING_TO_DATE(MOP2)  MOP2, 
  ClaimId, ClaimLine, MemQnxtId, MemFullName, PlanId,
  Location, FacilityCode, FacilityType, BillClassCode, BillClasification, FrequencyCode, Frequency, POS, 
  STRING_TO_DATE(ClaimStartDate) ClaimStartDate, 
   STRING_TO_DATE(ClaimEndDate)  ClaimEndDate,
   STRING_TO_DATE(PaidDate)  PaidDate,
  NULL Admit, NULL Discharge, NULL admithour, RevCode, ServCode, ServCodeDesc, ClaimDetailStatus, AmountPaid, ProvId, 
  ProvFullName, ProvSpecialty, Mony, DrugLabelName, 
   STRING_TO_DATE(MemDOB)  MemDOB, 
  MemAge, MemPCPFullName, MemPCPSpecialty, MedicaidId, MemEnrollId,
  PCPAffiliationId, ProvAffiliationId, GroupID, GroupName, csv2BhClaim.Diagnoses, NULL diag2, NULL diag3, NULL diag4, NULL prindiag, NULL Diag1_ICDVersion,
  NULL Diag2_ICDVersion, NULL Diag3_ICDVersion, NULL Diag4_ICDVersion, NULL product, NULL product_label, AllowAmt, Copay, NULL deductible, NULL cobamt, NULL contractpaid,
  NULL ineligibleamt, PharmacyName, Quantity, NPOS, RiskId, 
  STRING_TO_DATE(RunnDate)RunnDate, 
  NDC, PHARMACY, Claims, Psychare, Simple_County,
  NULL CountyCode, Triangles, Cover, NULL `Mod`, NULL Gender, NULL ProgramID, NULL ContractID, NULL ServProvName, NULL ServProvID,
  NULL ServProvSpecialty, NULL ServProvSpecialtyDesc, NULL patientstatus, NULL IPA, NULL SubIPA, NULL HICN, NULL formtype, NULL ReferralID,NULL BillType, NULL ClaimType,
  NULL claim_cat, now(), now(), 'sarath', 'sarath', csv2table_bh_claim_id
from csv2table_bh_claim csv2BhClaim
 left join (select mbr_medicaidNO from  membership) m  on csv2BhClaim.medicaidId = m.mbr_medicaidNO
 where m.mbr_medicaidNO is null;