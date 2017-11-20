select count(SRC_SYS_MEMBER_NBR) from csv2table_amg_claim csv2AmgClaim
 join membership_insurance mi  on  SRC_SYS_MEMBER_NBR =  SRC_SYS_MBR_NBR;