package com.pfchoice.springboot.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.pfchoice.springboot.util.XLSX2CSV;
import com.pfchoice.springboot.configuration.ConfigProperties;
import com.pfchoice.springboot.model.FileType;
import com.pfchoice.springboot.model.FileUpload;
import com.pfchoice.springboot.model.FileUploadContent;
import com.pfchoice.springboot.service.FileService;
import com.pfchoice.springboot.service.FileTypeService;
import com.pfchoice.springboot.service.FileUploadContentService;
import com.pfchoice.springboot.util.CustomErrorType;
import com.pfchoice.springboot.util.PrasUtil;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

@RestController
@RequestMapping("/api")
@SuppressWarnings({ "unchecked", "rawtypes" })
@SessionAttributes({ "username", "roleId", "userId", "roleName" })
public class FileUploadContentController implements ResourceLoaderAware {

	public static final Logger logger = LoggerFactory.getLogger(FileUploadContentController.class);

	private ResourceLoader resourceLoader;

	@Autowired
	FileUploadContentService fileUploadContentService; // Service which will do
														// all data
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
	public ResponseEntity<?> createFileUploadContent(@RequestBody FileUploadContent fileUploadContent,
			UriComponentsBuilder ucBuilder) {
		logger.info("Creating FileUploadContent : {}", fileUploadContent);

		if (fileUploadContentService.isFileUploadContentExists(fileUploadContent)) {
			logger.error("Unable to create. A FileUploadContent with name {} already exist",
					fileUploadContent.getFileName());
			return new ResponseEntity(new CustomErrorType("Unable to create. A FileUploadContent with name "
					+ fileUploadContent.getFileName() + " already exist."), HttpStatus.CONFLICT);
		}
		fileUploadContent.setCreatedBy("sarath");
		fileUploadContent.setUpdatedBy("sarath");
		fileUploadContentService.saveFileUploadContent(fileUploadContent);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(
				ucBuilder.path("/api/fileUploadContent/{id}").buildAndExpand(fileUploadContent.getId()).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	// ------------------- Update a FileUploadContent
	// ------------------------------------------------
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER" })
	@RequestMapping(value = "/fileUploadContent/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateFileUploadContent(@PathVariable("id") int id,
			@RequestBody FileUploadContent fileUploadContent) {
		logger.info("Updating FileUploadContent with id {}", id);

		FileUploadContent currentFileUploadContent = fileUploadContentService.findById(id);

		if (currentFileUploadContent == null) {
			logger.error("Unable to update. FileUploadContent with id {} not found.", id);
			return new ResponseEntity(
					new CustomErrorType("Unable to upate. FileUploadContent with id " + id + " not found."),
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
			return new ResponseEntity(
					new CustomErrorType("Unable to delete. FileUploadContent with id " + id + " not found."),
					HttpStatus.NOT_FOUND);
		}
		fileUploadContentService.deleteFileUploadContentById(id);
		return new ResponseEntity<FileUploadContent>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All
	// FileUploadContents-----------------------------
	@Secured({ "ROLE_ADMIN" })
	@RequestMapping(value = "/fileUploadContent/", method = RequestMethod.DELETE)
	public ResponseEntity<FileUploadContent> deleteAllFileUploadContents() {
		logger.info("Deleting All FileUploadContents");

		fileUploadContentService.deleteAllFileUploadContents();
		return new ResponseEntity<FileUploadContent>(HttpStatus.NO_CONTENT);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = { "/contractFileUpload/fileProcessing.do" }, method = RequestMethod.POST)
	public List<FileUpload> uploadFileProcessing(@RequestParam MultipartFile[] files) throws IOException {
		logger.info("started file processsing" );
		logger.info("fileUploadContent.length:" + files.length);
		List<FileUploadContent> fileUploadContenters = new ArrayList<>();
		List<FileUpload> fileUploaders = new ArrayList<>();

		for (MultipartFile fileUploadContent : files) {
			logger.info("fileUploadContent.getOriginalFilename() :" + fileUploadContent.getOriginalFilename());
			if ( !"".equals(fileUploadContent.getOriginalFilename())) {

				String fileName = fileUploadContent.getOriginalFilename();

				try {
					// String ext = FilenameUtils.getExtension(fileName);
					logger.info("fileName is : " + fileName);
					FileUploadContent fileUploadContenter = new FileUploadContent();
					fileUploadContenter.setFileName(fileName);
					fileUploadContenter.setContentType(fileUploadContent.getContentType());
					fileUploadContenter.setData(fileUploadContent.getBytes());
					fileUploadContentService.saveFileUploadContent(fileUploadContenter);

					FileUpload fileupload = new FileUpload();
					fileupload.setId(fileUploadContenter.getId());
					fileupload.setFileName(fileUploadContenter.getFileName());
					fileupload.setContentType(fileUploadContenter.getContentType());
					fileUploaders.add(fileupload);
					fileUploadContenters.add(fileUploadContenter);

				} catch (IOException e) {
					logger.warn(e.getCause().getMessage());
				}

			}

		}
		return fileUploaders;

	}

	
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = { "/fileUpload/consentFormFileProcessing.do" }, method = RequestMethod.POST)
	public List<FileUpload> uploadConsentFormFileProcessing(Model model, @RequestParam MultipartFile[] files) throws IOException {
		logger.info("started file processsing" + files.toString());
		logger.info("fileUploadContent.length:" + files.length);
		List<FileUploadContent> fileUploadContenters = new ArrayList<>();
		List<FileUpload> fileUploaders = new ArrayList<>();

		for (MultipartFile fileUploadContent : files) {
			logger.info("fileUploadContent.getOriginalFilename() :" + fileUploadContent.getOriginalFilename());
			if (fileUploadContent != null && !"".equals(fileUploadContent.getOriginalFilename())) {

				String fileName = fileUploadContent.getOriginalFilename();

				try {
					// String ext = FilenameUtils.getExtension(fileName);
					logger.info("fileName is : " + fileName);
					FileUploadContent fileUploadContenter = new FileUploadContent();
					fileUploadContenter.setFileName(fileName);
					fileUploadContenter.setContentType(fileUploadContent.getContentType());
					fileUploadContenter.setData(fileUploadContent.getBytes());
					fileUploadContentService.saveFileUploadContent(fileUploadContenter);
					
					FileUpload fileupload = new FileUpload();
					fileupload.setId(fileUploadContenter.getId());
					fileupload.setFileName(fileUploadContenter.getFileName());
					fileupload.setContentType(fileUploadContenter.getContentType());
					fileUploaders.add(fileupload);
					fileUploadContenters.add(fileUploadContenter);

				} catch (IOException e) {
					logger.warn(e.getCause().getMessage());
				}

			}
		}
		return fileUploaders;

	}
	
	@Secured({ "ROLE_ADMIN", "ROLE_AGENT", "ROLE_MANAGER", "ROLE_EVENT_COORDINATOR", "ROLE_CARE_COORDINATOR" })
	@RequestMapping(value = { "/fileUpload/fileProcessing.do" }, method = RequestMethod.POST)
	public void uploadFileProcessing(@ModelAttribute("username") String username,
			@RequestParam(required = true, value = "insId") Integer insId,
			@RequestParam(required = false, value = "fileTypeCode") Integer fileTypeId,
			@RequestParam(required = false, value = "activityMonth") Integer activityMonth,
			@RequestParam(required = false, value = "cap") Integer capReport, @RequestParam MultipartFile file,
			HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException, InvalidFormatException {
	
		logger.info("started file processsing" + file.getOriginalFilename());
		logger.info("insId" + insId);
		logger.info("fileTypeId" + fileTypeId);

		java.io.File sourceFile, newSourceFile = null;

		if (file != null && !"".equals(file.getOriginalFilename())) {

			String fileName = file.getOriginalFilename();
			String newfileName = fileName.substring(0, fileName.indexOf('.'));

			logger.info("fileName is : " + fileName);
			FileUploadContent fileUploadContenter = new FileUploadContent();
			fileUploadContenter.setFileName(fileName);
			fileUploadContenter.setContentType(file.getContentType());
			fileUploadContenter.setData(file.getBytes());
			fileUploadContentService.saveFileUploadContent(fileUploadContenter);

			String ext = FilenameUtils.getExtension(fileName);

			logger.info("configProperties.getFilesUploadDirectory()" + configProperties.getFilesUploadDirectory());
			FileUtils.writeByteArrayToFile(new java.io.File(configProperties.getFilesUploadDirectory() + fileName),
					file.getBytes());

			sourceFile = new java.io.File(configProperties.getFilesUploadDirectory() + fileName);
			sourceFile.createNewFile();
			System.out.println("sourceFile"+sourceFile);
			if (!"csv".equals(ext) && !"zip".equals(ext)) {
				newSourceFile = new java.io.File(configProperties.getFilesUploadDirectory() + newfileName + ".csv");
				
				newSourceFile.createNewFile();
				XLSX2CSV.xls(sourceFile, newSourceFile);
				if (sourceFile.exists()) {
					sourceFile.delete();
				}
				
			} else {
				newSourceFile = sourceFile;
			}

		}
		
		//req.setAttribute("fileName", newSourceFile.getAbsolutePath());
		String url = "/api/fileUploader/?fileName=" + newSourceFile.getAbsolutePath() + "&insId=" + insId
				+ "&fileTypeCode=" + fileTypeId + "&activityMonth=" + activityMonth + "&reportMonth=" + activityMonth;
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
	public ResponseEntity<?> loadFiles(@ModelAttribute("username") String username,
			@RequestParam(required = true, value = "insId") Integer insId,
			@RequestParam(required = true, value = "fileTypeCode") Integer fileTypeId,
			@RequestParam(required = false, value = "activityMonth") Integer activityMonth,
			@RequestParam(required = false, value = "reportMonth") Integer reportMonth,
			@RequestParam(value = "fileName", required = true) String fileName) throws IOException, InterruptedException, ExecutionException  {

		FileType  fileType = fileTypeService.findById(fileTypeId);
		 String foldername = fileName.substring(0, fileName.lastIndexOf('.'));
		List<Future<ResponseEntity<?>>> futures = new ArrayList<>();
		File[] files = new File[0];
		 if(activityMonth != null)
			{
			    if (fileType != null && fileType.getDescription().contains("Package") ) {
					logger.info("forwarding to claims Package");
					 File zipFolder = new File(foldername);
					 zipFolder.mkdir();
					prasUtil.unZip(fileName, foldername);
					 files = zipFolder.listFiles();
					for (File file : files) {
					    if (!file.isFile()) continue;
					 String  filename = foldername.replace("/", "\\")+"\\"+file.getName();
					 if(file.getName().contains("memberlevel")){
						   logger.info("forwarding to member Level");
						   FileType fileType3 = fileTypeService.findByDescriptionAndInsId("Membership Level", insId);
						   Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
									.asyncMbrLevelOrPrvdrAdjustFileUploadProcessing(username, insId, fileType3.getId(), activityMonth, reportMonth, filename);
							futures.add(future);
					   }  else   if( file.getName().contains("pharmacy")){
						   logger.info("forwarding to pharmacy"+file.getAbsolutePath());
						   FileType  fileType2 = fileTypeService.findByDescriptionAndInsId("Membership Claims Pharmacy", insId);
						   Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
									.asyncMbrClaimsFileUploadProcessing(username, insId, fileType2.getId(), activityMonth, reportMonth, filename);
							futures.add(future);
					   } else if(file.getName().contains("claims") ){
						   logger.info("forwarding to claims file.getAbsolutePath()"+file.getAbsolutePath());
						   FileType fileType1 = fileTypeService.findByDescriptionAndInsId("Membership Claim", insId);
						   Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
									.asyncMbrClaimsFileUploadProcessing(username, insId, fileType1.getId(), activityMonth, reportMonth, filename);
							futures.add(future);
					   } else if( file.getName().contains("adjust")){
						   logger.info("forwarding to adjust");
						   FileType fileType4 = fileTypeService.findByDescription("AMG Adjust");
						   Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
									.asyncMbrLevelOrPrvdrAdjustFileUploadProcessing(username, insId, fileType4.getId(), activityMonth, reportMonth, filename);
							futures.add(future);
					   } else if( file.getName().contains("mmr")){
						   logger.info("forwarding to mmr");
						   FileType fileType5 = fileTypeService.findByDescription("Simply Membership MMR");
						   Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
									.asyncMbrMedicalRiskAdjustFileUploadProcessing(username, insId, fileType5.getId(), activityMonth, reportMonth, filename);
							futures.add(future);
					   }
					     
					}
					
				}
			    else if (fileType != null && fileType.getDescription().contains("Claim") ) {
					logger.info("forwarding to claims");
					Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
							.asyncMbrClaimsFileUploadProcessing(username, insId, fileTypeId, activityMonth, reportMonth, fileName);
					futures.add(future);
				} else	if(fileType != null && (fileType.getDescription().contains("Level") || fileType.getDescription().contains("Adjust"))){
					logger.info("forwarding to member Level");
					Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
							.asyncMbrLevelOrPrvdrAdjustFileUploadProcessing(username, insId, fileTypeId, activityMonth, reportMonth, fileName);
					futures.add(future);
				}else {
					logger.info("forwarding to roster or cap");
					Future<ResponseEntity<?>> future = (Future<ResponseEntity<?>>) fileUploadContentService
					.asyncMbrRosterOrCapFileUploadProcessing(username, insId, fileTypeId, activityMonth, reportMonth, fileName);
					futures.add(future);
				}
			}
			else{	
				if (fileType != null && fileType.getDescription().contains("Hospitalization")) {
					logger.info("forwarding to hospitalization");
					//hospitalization
					//futures.add(future);
				}  
			}
			
		 Future<ResponseEntity<?>> finalFuture =null;
		 Integer index =0;
		
			while (index < futures.size() && futures.get(index) != null && true) {
				try {
					if (futures.get(index).isDone()) {
						finalFuture = futures.get(index);
						if(++index >= files.length )
							break;
					}
				} catch (Exception ex) {
					finalFuture = futures.get(index);
					if(++index >=files.length )
						break;
				} 
				System.out.println("FileUpload  processing is in progress. ");
				Thread.sleep(5000);
			}
			
			if(files.length >1){
				prasUtil.deleteFolder(foldername);
			}
			
		return finalFuture.get();
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Resource getResource(String location) {
		return resourceLoader.getResource(location);
	}

}