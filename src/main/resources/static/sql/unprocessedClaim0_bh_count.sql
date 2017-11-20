select count(medicaidId) from csv2table_bh_claim csv2BhClaim
 join membership m  on csv2BhClaim.medicaidId = m.mbr_medicaidNO;