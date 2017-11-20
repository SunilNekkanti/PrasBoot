package com.pfchoice.springboot.controller;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.util.XLSX2CSV;
import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.File;
import com.pfchoice.springboot.model.FileType;
import com.pfchoice.springboot.model.FileUploadContent;
import com.pfchoice.springboot.model.Membership;
import com.pfchoice.springboot.service.FileService;
import com.pfchoice.springboot.service.FileTypeService;
import com.pfchoice.springboot.service.FileUploadContentService;
import com.pfchoice.springboot.util.CustomErrorType;
import com.pfchoice.springboot.util.PrasUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class FileUploadContentController implements ResourceLoaderAware {

	public static final Logger logger = LoggerFactory.getLogger(FileUploadContentController.class);

	private ResourceLoader resourceLoader;

	@Autowired
	FileUploadContentService fileUploadContentService; // Service which will do all data
											// retrieval/manipulation work

	@Autowired
	FileService fileService;
	
	@Autowired
	FileTypeService fileTypeService;
	
	@Autowired
	ConfigProperties configProperties;
	
	@Autowired
	PrasUtil prasUtil;
	

	// -------------------Retrieve All
	// FileUploadContents---------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR", "ROLE_MANAGER" })
	@RequestMapping(value = "/fileUploadContent/", method = RequestMethod.GET)
	public ResponseEntity<List<FileUploadContent>> listAllFileUploadContents() {

		List<FileUploadContent> fileUploadContents = fileUploadContentService.findAllFileUploadContents();
		if (fileUploadContents.isEmpty()) {
			System.out.println("no fileUploadContents");
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<List<FileUploadContent>>(fileUploadContents, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// FileUploadContent------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER" })
	@RequestMapping(value = "/fileUploadContent/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFileUploadContent(@PathVariable("id") int id) {
		logger.info("Fetching FileUploadContent with id {}", id);
		FileUploadContent fileUploadContent = fileUploadContentService.findById(id);
		if (fileUploadContent == null) {
			logger.error("FileUploadContent with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("FileUploadContent with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<FileUploadContent>(fileUploadContent, HttpStatus.OK);
	}

	// -------------------Create a
	// FileUploadContent-------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_EVENT_COORDINATOR" })
	@RequestMapping(value = "/fileUploadContent/", method = RequestMethod.POST)
	public ResponseEntity<?> createFileUploadContent(@RequestBody FileUploadContent fileUploadContent, UriComponentsBuilder ucBuilder) {
		logger.info("Creating FileUploadContent : {}", fileUploadContent);

		if (fileUploadContentService.isFileUploadContentExists(fileUploadContent)) {
			logger.error("Unable to create. A FileUploadContent with name {} already exist", fileUploadContent.getFileName());
			return new ResponseEntity(
					new CustomErrorType(
							"Unable to create. A FileUploadContent with name " + fileUploadContent.getFileName() + " already exist."),
					HttpStatus.CONFLICT);
		}
		fileUploadContent.setCreatedBy("sarath");
		fileUploadContent.setUpdatedBy("sarath");
		fileUploadContentService.saveFileUploadContent(fileUploadContent);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/fileUploadContent/{id}").buildAndExpand(fileUploadContent.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a FileUploadContent
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER" })
	@RequestMapping(value = "/fileUploadContent/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFileUploadContent(@PathVariable("id") int id, @RequestBody FileUploadContent fileUploadContent) {
		logger.info("Updating FileUploadContent with id {}", id);

		FileUploadContent currentFileUploadContent = fileUploadContentService.findById(id);

		if (currentFileUploadContent == null) {
			logger.error("Unable to update. FileUploadContent with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to upate. FileUploadContent with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}

		fileUploadContentService.updateFileUploadContent(currentFileUploadContent);
		return new ResponseEntity<FileUploadContent>(currentFileUploadContent, HttpStatus.OK);
	}

	// ------------------- Delete a
	// FileUploadContent-----------------------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/fileUploadContent/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFileUploadContent(@PathVariable("id") int id) {
		logger.info("Fetching & Deleting FileUploadContent with id {}", id);

		FileUploadContent fileUploadContent = fileUploadContentService.findById(id);
		if (fileUploadContent == null) {
			logger.error("Unable to delete. FileUploadContent with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("Unable to delete. FileUploadContent with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		fileUploadContentService.deleteFileUploadContentById(id);
		return new ResponseEntity<FileUploadContent>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All FileUploadContents-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/fileUploadContent/", method = RequestMethod.DELETE)
	public ResponseEntity<FileUploadContent> deleteAllFileUploadContents() {
		logger.info("Deleting All FileUploadContents");

		fileUploadContentService.deleteAllFileUploadContents();
		return new ResponseEntity<FileUploadContent>(HttpStatus.NO_CONTENT);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = { "/fileUpload/fileProcessing.do" }, method = RequestMethod.POST)
	public void uploadFileProcessing(
			    @ModelAttribute("username") String username,
			    @RequestParam(required = true, value = "insId") Integer insId,
				@RequestParam(required = false, value = "claimOrHospital") Integer claimOrHospital,
				@RequestParam(required = false, value = "fileTypeCode") Integer fileTypeId,
				@RequestParam(required = false, value = "activityMonth") Integer activityMonth,
				@RequestParam(required = false, value = "pharmacyClaim") Integer pharmacyClaim,
				@RequestParam(required = false, value = "cap") Integer capReport,
			    @RequestParam MultipartFile file,
			    HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		logger.info("started file processsing" + file.getOriginalFilename());
		
		java.io.File sourceFile, newSourceFile = null;
		
			if (file != null && !"".equals(file.getOriginalFilename())) {

				String fileName = file.getOriginalFilename();
				String newfileName = fileName.substring(0, fileName.indexOf('.'));
				
				try {
					// String ext = FilenameUtils.getExtension(fileName);
					logger.info("fileName is : " + fileName);
					FileUploadContent fileUploadContenter = new FileUploadContent();
					fileUploadContenter.setFileName(fileName);
					fileUploadContenter.setContentType(file.getContentType());
					fileUploadContenter.setData(file.getBytes());
					fileUploadContentService.saveFileUploadContent(fileUploadContenter);
					
                    String ext = FilenameUtils.getExtension(fileName);
					
                    logger.info("configProperties.getFilesUploadDirectory()"+configProperties.getFilesUploadDirectory());
					FileUtils.writeByteArrayToFile(new java.io.File(configProperties.getFilesUploadDirectory() + fileName),
							file.getBytes());

					sourceFile = new java.io.File(configProperties.getFilesUploadDirectory() + fileName);
					sourceFile.createNewFile();
					if(!"csv".equals(ext)){
						newSourceFile = new java.io.File(configProperties.getFilesUploadDirectory() + newfileName + ".csv");
						newSourceFile.createNewFile();
						XLSX2CSV.xls(sourceFile, newSourceFile);
						if (sourceFile.exists()) {
		 					sourceFile.delete();
						}
					}	
					else{
						newSourceFile = sourceFile;
					}
				} catch (InvalidFormatException|IOException   e) {
					logger.warn(e.getCause().getMessage());
				} 

			}
			
		    req.setAttribute("fileName", newSourceFile.getAbsolutePath());
			String url ="/api/fileUploader/?fileName=" +newSourceFile.getAbsolutePath() + "&insId=" + insId + "&fileTypeCode=" + fileTypeId + "&activityMonth=" + activityMonth+ "&reportMonth=" + activityMonth;
		    req.getRequestDispatcher(url).forward(req, res);
	}

	// -------------------Retrieve FileUploadContented data
	// ------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = "/fileUploaded/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileUploadContentContents(@PathVariable("id") int id) {
		FileUploadContent fileUploadContent = fileUploadContentService.findById(id);
		if (fileUploadContent == null) {
			logger.error("FileUploadContent with id {} not found.", id);
			return new ResponseEntity(new CustomErrorType("FileUploadContent with id " + id + " not found"),
					HttpStatus.NOT_FOUND);
		}

		byte[] contents = fileUploadContent.getData();
		HttpHeaders headers = new HttpHeaders();
		String filename = fileUploadContent.getFileName();
		headers.setContentDispositionFormData("inline", filename);
		headers.setContentType(MediaType.parseMediaType(fileUploadContent.getContentType()));

		return new ResponseEntity<byte[]>(contents, headers, HttpStatus.OK);
	}
	
	
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/fileUploader/")
	public ResponseEntity<?> loadMembershipRoster( 
					@ModelAttribute("username") String username,
					@RequestParam(required = true, value = "insId") Integer insId,
					@RequestParam(required = true, value = "fileTypeCode") Integer fileTypeId,
					@RequestParam(required = true, value = "activityMonth") Integer activityMonth,
					@RequestParam(required = true, value = "reportMonth") Integer reportMonth,
					@RequestParam(value = "fileName", required = true) String fileName) throws IOException {

		
		Map<String,Object> params = new HashMap<>();
		
		FileType fileType = fileTypeService.findById(fileTypeId);
		String tableName = fileType.getTablesName();
		String fileTypeDescription = fileType.getDescription();
		String entityClassName = fileType.getEntityClassName();
		String queryTypeLoad = configProperties.getQueryTypeLoad();
		String queryTypeInsert = configProperties.getQueryTypeInsert();
		
		logger.info("tableName "+tableName);
		String sqlQuery = "select BigToInt(count(*)> 0) from "+tableName;
		logger.info("sqlQuery "+sqlQuery);
		Integer dataExists = prasUtil.executeSqlScript(sqlQuery, params, true);
		logger.info("dataExists "+dataExists);
		
		if (dataExists > 0) {
			logger.info("Previous file processing is incomplete ");
			CustomErrorType errorMessage = new CustomErrorType("Previous file processing is incomplete");
			 
			 return new ResponseEntity<CustomErrorType>(errorMessage,  HttpStatus.NO_CONTENT);
		} else {
			Integer fileId = 0;
			try {
				File fileRecord = new File();
				fileRecord.setFileName(fileName);
				fileRecord.setFileType(fileType);
				fileRecord.setCreatedBy(username);
				fileRecord.setUpdatedBy(username);
				 fileService.saveFile(fileRecord);

				fileId = fileRecord.getId();

			} catch (Exception e) {
				logger.warn(e.getCause().getMessage());
				logger.info("Similar file already processed in past");
				CustomErrorType errorMessage = new CustomErrorType("Similar file already processed in past");
				return new ResponseEntity<CustomErrorType>(errorMessage,  HttpStatus.NO_CONTENT);
			}

			
			Resource loadFromCSVTable = getResource("classpath:static/sql/"+entityClassName+queryTypeLoad+configProperties.getSqlQueryExtn());
			sqlQuery = new String( Files.readAllBytes(Paths.get(loadFromCSVTable.getURI())),StandardCharsets.UTF_8);
			
			params.clear();
			params.put("file", fileName);
			Integer loadedData = prasUtil.executeSqlScript(sqlQuery , params, false);

			if (loadedData < 1) {
				CustomErrorType errorMessage = new CustomErrorType("ZERO records to process");
				return new ResponseEntity<CustomErrorType>(errorMessage,  HttpStatus.NO_CONTENT);
			}

			logger.info("loaded  "+ loadedData+"  records into table " +tableName);
			logger.info("processing "+ fileTypeDescription+" data" + new Date());
			
			params.clear();
			params.put("insId",insId);
			params.put("fileId",fileId);
			params.put("activityMonth",activityMonth);
			
			Resource insertIntoTable = getResource("classpath:static/sql/"+entityClassName+queryTypeInsert+configProperties.getSqlQueryExtn());
			sqlQuery = new String( Files.readAllBytes(Paths.get(insertIntoTable.getURI())),StandardCharsets.UTF_8);
			
			Integer noOfRecordsLoaded = prasUtil.executeSqlScript(sqlQuery, params, false);
			logger.info("insertedData " + noOfRecordsLoaded+" records into "+entityClassName);
			
			sqlQuery = "truncate table "+ tableName;
			 prasUtil.executeSqlScript(sqlQuery, params, true);
			logger.info("cleared CSV Table " +tableName );
 
			logger.info("processed "+ fileTypeDescription+" data " + new Date());

			return  new ResponseEntity<Membership>(HttpStatus.NO_CONTENT);
		}
	}
	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Resource getResource(String location){
		return resourceLoader.getResource(location);
	}

}