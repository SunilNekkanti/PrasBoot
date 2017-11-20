LOAD DATA LOCAL INFILE :file INTO TABLE csv2table_amg_roster FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\r\n'   IGNORE 1 LINES SET dob =  case when POSITION(' 0:00' IN dob) > 0 then   
SUBSTR(dob,1, POSITION(' 0:00' IN dob))  else dob end
 