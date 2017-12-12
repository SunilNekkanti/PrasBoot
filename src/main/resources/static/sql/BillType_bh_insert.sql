INSERT IGNORE INTO LU_BILL_TYPE (
code,description,shortName,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT DISTINCT  BillClassCode, BillClasification, BillClasification,
now(),now(),'sarath','sarath','Y' 
FROM  csv2Table_BH_Claim csv2BhClaim
LEFT OUTER JOIN lu_bill_type  bt on  TRIM(LEADING '0' FROM csv2BhClaim.BillClassCode)  =    bt.code
where bt.code is null and csv2BhClaim.BillClassCode <> ''