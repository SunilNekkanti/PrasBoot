LOAD DATA LOCAL INFILE :file ignore INTO TABLE csv2table_cap_report FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n'   IGNORE 1 LINES SET MEMBER_ID=convert(MEMBER_ID, unsigned),
CAP_PAY_DT=STRING_TO_DATE(CAP_PAY_DT)  ,  CAP_PERIOD= STRING_TO_DATE(CAP_PERIOD) ,PCP_ID =convert(PCP_ID, unsigned)
 