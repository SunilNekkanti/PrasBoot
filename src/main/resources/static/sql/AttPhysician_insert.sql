INSERT INTO ATT_PHY (
code,name,file_id,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT DISTINCT case when ATT_NAME like '%Not Assigned%' then 'NA' else ATT_PHY end, ATT_NAME,
:fileId, now(),now(),'sarath','sarath','Y' 
FROM 
csv2AmgHospitalization mh
LEFT OUTER JOIN att_phy  att on TRIM(LEADING '0' FROM att.code)  =  case when mh.ATT_NAME like '%Not Assigned%' then 'NA' else  TRIM(LEADING '0' FROM mh.ATT_PHY)  end
WHERE att.code  IS NULL
 