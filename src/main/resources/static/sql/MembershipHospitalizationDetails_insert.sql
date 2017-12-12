INSERT INTO membership_hospitalization_details
(
mbr_hos_id,att_phy_id,room_type_code,adm_dx,exp_dc_dt,auth_days,cm_pri_user,disease_cohort,comorbidities,
file_id,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT
 mh.mbr_hos_id, ap.att_phy_id, csv2mh.room_type, csv2mh.adm_dx,  
  STRING_TO_DATE(csv2mh.exp_dc_dt),  csv2mh.auth_days, 
 csv2mh.cm_pri_user,  csv2mh.disease_cohort,  csv2mh.comorbidities,
  :fileId, now(),now(),'sarath','sarath','Y'
FROM csv2AmgHospitalization csv2mh
JOIN membership_insurance mi on mi.SRC_SYS_MBR_NBR  =  csv2mh.MEMBER_ID
LEFT OUTER JOIN att_phy ap on ap.code  = case when csv2mh.ATT_NAME like '%Not Assigned%' then 'NA' else  csv2mh.att_phy end
JOIN membership_provider mp on mp.mbr_id  =  mi.mbr_id
JOIN hospital h on h.code = csv2mh.hosp_id  
JOIN membership_hospitalization mh on mh.mbr_id =  mi.mbr_id
