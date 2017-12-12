INSERT INTO hospital (
code,name,file_id,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT DISTINCT HOSP_ID, HOSP_NAME, :fileId, now(),now(),'sarath','sarath','Y' FROM 
csv2AmgHospitalization mh
LEFT OUTER JOIN hospital  h on  TRIM(LEADING '0' FROM h.code) = TRIM(LEADING '0' FROM mh.HOSP_ID )
WHERE h.HOS_ID  IS NULL
