LOAD DATA LOCAL INFILE :file INTO TABLE {0}  FIELDS TERMINATED BY  {1} ENCLOSED BY {2} LINES TERMINATED BY {3}   IGNORE 1 LINES set CLAIMNUMBER = convert(CLAIMNUMBER,decimal(35,0)),
SRC_SYS_MEMBER_NBR =convert(SRC_SYS_MEMBER_NBR,unsigned)
