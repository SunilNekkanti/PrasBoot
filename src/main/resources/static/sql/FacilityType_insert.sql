INSERT IGNORE INTO LU_FACILITY_TYPE (
code,description,shortName,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT DISTINCT  facility_type_code, facility_type_desc, facility_type_desc,
 now(),now(),'sarath','sarath','Y' 
FROM  csv2Table_Amg_Claim csv2AmgClaim
LEFT OUTER JOIN lu_facility_type  ft on  TRIM(LEADING '0' FROM csv2AmgClaim.FACILITY_TYPE_CODE)  =    ft.code
where ft.code is null