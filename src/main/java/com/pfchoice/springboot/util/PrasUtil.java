package com.pfchoice.springboot.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.controller.FileUploadContentController;

@Component
public class PrasUtil {
	
	public static final Logger logger = LoggerFactory.getLogger(FileUploadContentController.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	ConfigProperties configProperties;
	
	@Transactional
	public Integer executeSqlScript(String sql, Map<String, Object> parameters, boolean singleResult) {
		
		Query query =   em.createNativeQuery(sql);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
		    	   query.setParameter(entry.getKey(),entry.getValue());
		 }
		
		if(singleResult){
			return (Integer)query.getSingleResult();
		}else{
			return query.executeUpdate();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> executeStoredProcedure(String spName, Map<String, Object> parameters) {
		
		StoredProcedureQuery query = em.createNamedStoredProcedureQuery(spName);
		
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
	    	   query.setParameter(entry.getKey(),entry.getValue());
	    }
	
		query.execute();
		return (List<Object[]>)query.getResultList();
		
	}
	
	

	
	@Transactional
	public Integer executeSqlScript(final String entityClassName, final String queryType, Map<String, Object> parameters) {
		String sql  = getSQLQuery(entityClassName,queryType);
	    Query query =   em.createNativeQuery(sql);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
		    	   query.setParameter(entry.getKey(),entry.getValue());
		    	   logger.info("parameter key:" +entry.getKey() + " parameter value:"+entry.getValue() );
		 }
		logger.info("SQL query is " +sql);
		return query.executeUpdate();
	}
	
	/**
	 * @param entityClassName
	 * @param queryType
	 * @return
	 */
	public  <T> String getSQLQuery(final String entityClassName, final String queryType) {
		String insertQuery = null;
		
		 
		try {
			Path path = FileSystems.getDefault()
					.getPath(configProperties.getSqlDirectoryPath() + entityClassName + queryType + configProperties.getSqlQueryExtn());
			insertQuery = new String(Files.readAllBytes(path.toAbsolutePath()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.warn("exception " + e.getCause());
		}
		return insertQuery;
	}

}
