INSERT INTO membership_problems 
(mbr_id,icd_id,report_month,start_date,resolved_date,file_id,created_date,updated_date,created_by,updated_by,active_ind )
select   t.mbr_id,  SUBSTRING_INDEX(SUBSTRING_INDEX(t.diagnosisICDs, ',', sub0.aNum), ',', -1)icd_id,:activityMonth,  min(activity_date) , null resolvedDate, :fileId,now(),now(),:username,:username,'Y'
from
(
select mbr_id,MedicaidNumber, activity_date, concat_ws(',',group_concat(distinct diag1),group_concat(distinct diag2),group_concat(distinct diag3),group_concat(distinct diag4)) diagnosis,
concat_ws(',',group_concat(distinct icd1.icdcode),group_concat(distinct icd2.icdcode),group_concat(distinct icd3.icdcode),group_concat(distinct icd4.icdcode)) diagicdcodes,
concat_ws(',',group_concat(distinct icd1.icd_id),group_concat(distinct icd2.icd_id),group_concat(distinct icd3.icd_id),group_concat(distinct icd4.icd_id))  diagnosisICDs
 from (
select  :insId,m.mbr_id,MedicaidNumber, activity_date,    diag1 ,  diag2, diag3, diag4  
from csv2table_simply_claim
join membership m on m.SRC_SYS_MBR_NBR= MedicaidNumber
where product_label ='Medicare'
group by MedicaidNumber, activity_date
) a
 join (select replace(code,'.','')icdcode,icd_id  from icd_measure ) icd1 on icd1.icdcode= diag1
left join (select replace(code,'.','')icdcode,icd_id  from icd_measure ) icd2 on icd2.icdcode= diag2
 left join (select replace(code,'.','')icdcode,icd_id  from icd_measure ) icd3 on icd3.icdcode= diag3
 left  join (select replace(code,'.','')icdcode,icd_id  from icd_measure ) icd4 on icd4.icdcode= diag4
 group by MedicaidNumber, activity_date
 ) t
 INNER JOIN
(
    SELECT 1 + units.i + tens.i * 10 AS aNum, units.i + tens.i * 10 AS aSubscript
    FROM (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) units
    CROSS JOIN (SELECT 0 AS i UNION SELECT 1 UNION SELECT 2 UNION SELECT 3  ) tens
) sub0
  ON (1 + LENGTH(t.diagnosisICDs) - LENGTH(REPLACE(t.diagnosisICDs, ',', ''))) >= sub0.aNum
  
LEFT JOIN membership_problems mp on mp.mbr_id = t.mbr_id and mp.icd_id = SUBSTRING_INDEX(SUBSTRING_INDEX(t.diagnosisICDs, ',', sub0.aNum), ',', -1)
where case when mp.mbr_id is not null  then  case when mp.icd_id is not  null  then t.activity_date >  mp.resolved_date    else   mp.icd_id is  null  end       else  mp.mbr_id  is null end
group by  t.mbr_id, SUBSTRING_INDEX(SUBSTRING_INDEX(t.diagnosisICDs, ',', sub0.aNum), ',', -1) ;


 INSERT INTO membership_hedis_problems (
		 mbr_id,pbm_id,start_date,resolved_date,created_date,updated_date,
		  created_by,updated_by,active_ind,file_id
		  )
  SELECT  m.mbr_id, p.pbm_id, min(activity_date) start_date, null resolved_date,
 now() created_date, now() updated_date,:username created_by ,:username updated_by,'Y', :fileId  
   FROM csv2table_simply_claim claims 
join membership m on m.SRC_SYS_MBR_NBR = claims.MedicaidNumber
JOIN problems p ON p.ins_id=:insId
 JOIN problems_icd  pbmicd ON p.pbm_id = pbmicd.pbm_id
  JOIN (select replace(code,'.','') icdcode, icd_id from icd_measure ) icd ON icd.icd_id = pbmicd.icd_id AND   (icdcode =diag1 or icdcode =diag2 or icdcode =diag3 or icdcode =diag4 ) 
 LEFT OUTER JOIN membership_hedis_problems mhp ON mhp.mbr_id= m.mbr_id and mhp.pbm_id =p.pbm_id   
 where  product_label='Medicare'
 and case when mhp.mbr_id is not null then  
                                         case when mhp.pbm_id  is not null then   
                                                                               case when mhp.resolved_date   is not null then activity_date >  mhp.resolved_date is not null  
                                                                                else  1=1  end 
 																	        else  mhp.pbm_id is null end
 									 else mhp.mbr_id is null end
 group by m.mbr_id, p.pbm_id
 order by m.mbr_id,activity_date ;
