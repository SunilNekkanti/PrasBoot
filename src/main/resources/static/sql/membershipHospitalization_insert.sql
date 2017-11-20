
INSERT INTO membership_hospitalization
(
hos_id,mbr_id,prvdr_id,ins_id,report,plan_desc,authnum,admit_date,prior_admits,exp_dc_date,file_id,
created_date,updated_date,created_by,updated_by,active_ind
)

SELECT
  h.hos_id,  mi.mbr_id, mp.prvdr_id, mi.ins_id, csv2mh.report,csv2mh.plan_desc, csv2mh.authnum,
   STRING_TO_DATE(csv2mh.admit_date), csv2mh.prior_admits,
   STRING_TO_DATE(csv2mh.exp_dc_dt), 
  :fileId, now(),now(),'sarath','sarath','Y'
 FROM csv2AmgHospitalization csv2mh
 JOIN membership_insurance mi on mi.SRC_SYS_MBR_NBR  =  csv2mh.MEMBER_ID
 JOIN membership_provider mp on mp.mbr_id  =  mi.mbr_id
 JOIN hospital h on h.code = csv2mh.hosp_id  
LEFT OUTER JOIN membership_hospitalization mh on mh.mbr_id =  mi.mbr_id
WHERE 
mh.mbr_id is null
