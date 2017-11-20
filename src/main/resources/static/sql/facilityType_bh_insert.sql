INSERT IGNORE INTO LU_FACILITY_TYPE (
code,description,shortName,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT DISTINCT  FacilityCode, FacilityType, FacilityType,
now(),now(),'sarath','sarath','Y' 
FROM  csv2Table_BH_Claim csv2BhClaim
LEFT OUTER JOIN lu_facility_type  ft on  TRIM(LEADING '0' FROM csv2BhClaim.FacilityCode)  =    ft.code
where ft.code is null and csv2BhClaim.FacilityCode <> ''