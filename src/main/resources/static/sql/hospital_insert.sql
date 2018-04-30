INSERT INTO hospital (
hos_id,name,file_id,created_date,updated_date,created_by,updated_by,active_ind
)
SELECT DISTINCT null, Facility_Name, :fileId, now() created_date,now()updated_date,:username created_by,:username updated_by,'Y' activeInd FROM  
csv2table_amg_hospitalization mh
LEFT OUTER JOIN hospital  h on  h.name=mh.Facility_Name
WHERE h.name  IS NULL;