select  count(MedicaidNumber) from csv2table_bh_claim1 csv2BhClaim1
 join membership m  on csv2BhClaim1.MedicaidNumber = m.mbr_medicaidNO;