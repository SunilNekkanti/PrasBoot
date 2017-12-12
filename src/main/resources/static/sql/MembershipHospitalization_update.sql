UPDATE membership_hospitalization mh
JOIN membership_insurance mi on  mh.mbr_id =  mi.mbr_id
JOIN csv2AmgHospitalization csv2mh on mi.SRC_SYS_MBR_NBR  =  csv2mh.MEMBER_ID
set  mh.exp_dc_date =   STRING_TO_DATE(csv2mh.exp_dc_dt),
mh.file_id = :fileId

