package com.pfchoice.springboot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.controller.FileUploadContentController;

@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PrasUtil {

	public static final Logger logger = LoggerFactory.getLogger(FileUploadContentController.class);

	@PersistenceContext
	private EntityManager em;

	@Autowired
	ConfigProperties configProperties;
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Transactional
	public Integer executeSqlScript(String sql, Map<String, Object> parameters, boolean singleResult) {

		Query query = em.createNativeQuery(sql);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		if (singleResult) {
			return (Integer) query.getSingleResult();
		} else {
			return query.executeUpdate();
		}
	}

	@Transactional
	public  List<Object[]> executeStoredProcedure(String spName, Map<String, Object> parameters) {

		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(spName);
		
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}

		query.execute();
		return (List<Object[]>) query.getResultList();

	}

	
	@Transactional
	public Integer executeSqlScript(final String entityClassName, final String queryType,
			Map<String, Object> parameters) {
		String sql = getSQLQuery(entityClassName, queryType);
		Query query = em.createNativeQuery(sql);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
			logger.info("parameter key:" + entry.getKey() + " parameter value:" + entry.getValue());
		}
		return query.executeUpdate();
	}

	/**
	 * @param entityClassName
	 * @param queryType
	 * @return
	 */
	public <T> String getSQLQuery(final String entityClassName, final String queryType) {
		String insertQuery = null;

		try {
			Path path = FileSystems.getDefault().getPath(configProperties.getSqlDirectoryPath() + entityClassName
					+ queryType + configProperties.getSqlQueryExtn());
			insertQuery = new String(Files.readAllBytes(path.toAbsolutePath()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.warn("exception " + e.getCause());
		}
		return insertQuery;
	}

	
	/**
	 * @param entityClassName
	 * @param queryType
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public <T> Integer executeSQLQuery(JpaSpecificationExecutor<?> repository,final String insuranceCode,  final Map<String, Object>  params, final String queryType) throws IOException, InterruptedException {
		
		Class clazz = repository.getClass().getInterfaces()[0];
		DefaultRepositoryMetadata drm = new DefaultRepositoryMetadata(clazz);
		Class<?> domainType = drm.getDomainType();
		final String entityClassName  = domainType.getSimpleName();

		Integer noOfRecordsLoaded = executeSQLQuery(entityClassName,insuranceCode, params, queryType );
		logger.info("insertedData " + noOfRecordsLoaded + " records into " + entityClassName);
	
		return noOfRecordsLoaded;
	}
	
	/**
	 * @param entityClassName
	 * @param queryType
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public  <T> Integer executeSQLQuery(String  entityClassName, final String insuranceCode, final Map<String, Object>  params,  final String queryType) throws IOException, InterruptedException {
			Resource insertIntoTable = getResource("classpath:static/sql/" + entityClassName +insuranceCode + queryType + configProperties.getSqlQueryExtn());
			String sqlQuery = StreamUtils.copyToString(insertIntoTable.getInputStream(), StandardCharsets.UTF_8);
		
			Integer noOfRecordsLoaded = executeSqlScript(sqlQuery, params, false);
			logger.info("insertedData " + noOfRecordsLoaded + " records into " + entityClassName);
			return noOfRecordsLoaded;
	}
	
	 public void unZip(String zipFile, String outputFolder) throws IOException{

	     byte[] buffer = new byte[1024];

	    	//create output directory is not exists
	    	File folder = new File(outputFolder);
	    	if(!folder.exists()){
	    		folder.mkdir();
	    	}

	    	//get the zip file content
	    	ZipInputStream zis =
	    		new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();

	    	while(ze!=null){

	    	   String fileName = ze.getName();
	           File newFile = new File(outputFolder + File.separator + fileName);

	           logger.info("file unzip : "+ newFile.getAbsoluteFile());

	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParent()).mkdirs();

	            FileOutputStream fos = new FileOutputStream(newFile);

	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	       		fos.write(buffer, 0, len);
	            }

	            fos.close();
	            ze = zis.getNextEntry();
	    	}

	        zis.closeEntry();
	    	zis.close();

	    	logger.info("Done");

	 }
	 
	 public void deleteFolder(String directoryPath) throws IOException{
		 Path dirPath = Paths.get(directoryPath);
		 Files.walk( dirPath )
		      .map( Path::toFile )
		      .sorted( Comparator.comparing( File::isDirectory ) ) 
		      .forEach( File::delete );
	 }
	 
	 
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Resource getResource(String location) {
		return resourceLoader.getResource(location);
	}
	
	

}
