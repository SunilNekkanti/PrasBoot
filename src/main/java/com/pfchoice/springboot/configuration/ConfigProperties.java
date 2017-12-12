package com.pfchoice.springboot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("envProperties")
public class ConfigProperties {

	private String filesUploadDirectory;
	private String sqlDirectoryPath;
	private String queryTypeFetch;
	private String queryTypeStopLoss;
	private String queryTypeInsert;
	private String queryTypeBHInsert;
	private String queryTypeLoad;
	private String queryTypeBHLoad;
	private String queryTypeUpdate;
	private String queryTypeBHUpdate;
	private String queryTypeUnload;
	private String queryTypeCount;
	private String sqlQueryExtn;
	private String sqlFollowupTypeHedis;
	private String followupTypeHospitalization;
	private String followupTypeClaim;
	private String fileTypeAMGMBRHospitalization;
	private String fileTypeAMGMBRClaim;
	private String fileTypeBHMBRClaim;
	private String fileTypeAMGMBRRoster;
	private String fileTypeAMGCapReport;
	private String fileTypeBHMBRRoster;
	private Integer filterByProcessingDate;
	private Integer filterByHospitalizationDate;
	private Integer All;
	private Integer claim;
	private Integer hospitalization;
	private Integer acceptableClaim;
	private Integer unacceptableClaim;
	private String queryTypeInsertLevel2;

	/**
	 * @return the filesUploadDirectory
	 */
	public String getFilesUploadDirectory() {
		return filesUploadDirectory;
	}

	/**
	 * @param filesUploadDirectory
	 *            the filesUploadDirectory to set
	 */
	public void setFilesUploadDirectory(String filesUploadDirectory) {
		this.filesUploadDirectory = filesUploadDirectory;
	}

	/**
	 * @return the sqlDirectoryPath
	 */
	public String getSqlDirectoryPath() {
		return sqlDirectoryPath;
	}

	/**
	 * @param sqlDirectoryPath
	 *            the sqlDirectoryPath to set
	 */
	public void setSqlDirectoryPath(String sqlDirectoryPath) {
		this.sqlDirectoryPath = sqlDirectoryPath;
	}

	/**
	 * @return the queryTypeFetch
	 */
	public String getQueryTypeFetch() {
		return queryTypeFetch;
	}

	/**
	 * @param queryTypeFetch
	 *            the queryTypeFetch to set
	 */
	public void setQueryTypeFetch(String queryTypeFetch) {
		this.queryTypeFetch = queryTypeFetch;
	}

	/**
	 * @return the queryTypeStopLoss
	 */
	public String getQueryTypeStopLoss() {
		return queryTypeStopLoss;
	}

	/**
	 * @param queryTypeStopLoss
	 *            the queryTypeStopLoss to set
	 */
	public void setQueryTypeStopLoss(String queryTypeStopLoss) {
		this.queryTypeStopLoss = queryTypeStopLoss;
	}

	/**
	 * @return the queryTypeInsert
	 */
	public String getQueryTypeInsert() {
		return queryTypeInsert;
	}

	/**
	 * @param queryTypeInsert
	 *            the queryTypeInsert to set
	 */
	public void setQueryTypeInsert(String queryTypeInsert) {
		this.queryTypeInsert = queryTypeInsert;
	}

	/**
	 * @return the queryTypeBHInsert
	 */
	public String getQueryTypeBHInsert() {
		return queryTypeBHInsert;
	}

	/**
	 * @param queryTypeBHInsert
	 *            the queryTypeBHInsert to set
	 */
	public void setQueryTypeBHInsert(String queryTypeBHInsert) {
		this.queryTypeBHInsert = queryTypeBHInsert;
	}

	/**
	 * @return the queryTypeLoad
	 */
	public String getQueryTypeLoad() {
		return queryTypeLoad;
	}

	/**
	 * @param queryTypeLoad
	 *            the queryTypeLoad to set
	 */
	public void setQueryTypeLoad(String queryTypeLoad) {
		this.queryTypeLoad = queryTypeLoad;
	}

	/**
	 * @return the queryTypeBHLoad
	 */
	public String getQueryTypeBHLoad() {
		return queryTypeBHLoad;
	}

	/**
	 * @param queryTypeBHLoad
	 *            the queryTypeBHLoad to set
	 */
	public void setQueryTypeBHLoad(String queryTypeBHLoad) {
		this.queryTypeBHLoad = queryTypeBHLoad;
	}

	/**
	 * @return the queryTypeUpdate
	 */
	public String getQueryTypeUpdate() {
		return queryTypeUpdate;
	}

	/**
	 * @param queryTypeUpdate
	 *            the queryTypeUpdate to set
	 */
	public void setQueryTypeUpdate(String queryTypeUpdate) {
		this.queryTypeUpdate = queryTypeUpdate;
	}

	/**
	 * @return the queryTypeBHUpdate
	 */
	public String getQueryTypeBHUpdate() {
		return queryTypeBHUpdate;
	}

	/**
	 * @param queryTypeBHUpdate
	 *            the queryTypeBHUpdate to set
	 */
	public void setQueryTypeBHUpdate(String queryTypeBHUpdate) {
		this.queryTypeBHUpdate = queryTypeBHUpdate;
	}

	/**
	 * @return the queryTypeUnload
	 */
	public String getQueryTypeUnload() {
		return queryTypeUnload;
	}

	/**
	 * @param queryTypeUnload
	 *            the queryTypeUnload to set
	 */
	public void setQueryTypeUnload(String queryTypeUnload) {
		this.queryTypeUnload = queryTypeUnload;
	}

	/**
	 * @return the queryTypeCount
	 */
	public String getQueryTypeCount() {
		return queryTypeCount;
	}

	/**
	 * @param queryTypeCount
	 *            the queryTypeCount to set
	 */
	public void setQueryTypeCount(String queryTypeCount) {
		this.queryTypeCount = queryTypeCount;
	}

	/**
	 * @return the sqlQueryExtn
	 */
	public String getSqlQueryExtn() {
		return sqlQueryExtn;
	}

	/**
	 * @param sqlQueryExtn
	 *            the sqlQueryExtn to set
	 */
	public void setSqlQueryExtn(String sqlQueryExtn) {
		this.sqlQueryExtn = sqlQueryExtn;
	}

	/**
	 * @return the sqlFollowupTypeHedis
	 */
	public String getSqlFollowupTypeHedis() {
		return sqlFollowupTypeHedis;
	}

	/**
	 * @param sqlFollowupTypeHedis
	 *            the sqlFollowupTypeHedis to set
	 */
	public void setSqlFollowupTypeHedis(String sqlFollowupTypeHedis) {
		this.sqlFollowupTypeHedis = sqlFollowupTypeHedis;
	}

	/**
	 * @return the followupTypeHospitalization
	 */
	public String getFollowupTypeHospitalization() {
		return followupTypeHospitalization;
	}

	/**
	 * @param followupTypeHospitalization
	 *            the followupTypeHospitalization to set
	 */
	public void setFollowupTypeHospitalization(String followupTypeHospitalization) {
		this.followupTypeHospitalization = followupTypeHospitalization;
	}

	/**
	 * @return the followupTypeClaim
	 */
	public String getFollowupTypeClaim() {
		return followupTypeClaim;
	}

	/**
	 * @param followupTypeClaim
	 *            the followupTypeClaim to set
	 */
	public void setFollowupTypeClaim(String followupTypeClaim) {
		this.followupTypeClaim = followupTypeClaim;
	}

	/**
	 * @return the fileTypeAMGMBRHospitalization
	 */
	public String getFileTypeAMGMBRHospitalization() {
		return fileTypeAMGMBRHospitalization;
	}

	/**
	 * @param fileTypeAMGMBRHospitalization
	 *            the fileTypeAMGMBRHospitalization to set
	 */
	public void setFileTypeAMGMBRHospitalization(String fileTypeAMGMBRHospitalization) {
		this.fileTypeAMGMBRHospitalization = fileTypeAMGMBRHospitalization;
	}

	/**
	 * @return the fileTypeAMGMBRClaim
	 */
	public String getFileTypeAMGMBRClaim() {
		return fileTypeAMGMBRClaim;
	}

	/**
	 * @param fileTypeAMGMBRClaim
	 *            the fileTypeAMGMBRClaim to set
	 */
	public void setFileTypeAMGMBRClaim(String fileTypeAMGMBRClaim) {
		this.fileTypeAMGMBRClaim = fileTypeAMGMBRClaim;
	}

	/**
	 * @return the fileTypeBHMBRClaim
	 */
	public String getFileTypeBHMBRClaim() {
		return fileTypeBHMBRClaim;
	}

	/**
	 * @param fileTypeBHMBRClaim
	 *            the fileTypeBHMBRClaim to set
	 */
	public void setFileTypeBHMBRClaim(String fileTypeBHMBRClaim) {
		this.fileTypeBHMBRClaim = fileTypeBHMBRClaim;
	}

	/**
	 * @return the fileTypeAMGMBRRoster
	 */
	public String getFileTypeAMGMBRRoster() {
		return fileTypeAMGMBRRoster;
	}

	/**
	 * @param fileTypeAMGMBRRoster
	 *            the fileTypeAMGMBRRoster to set
	 */
	public void setFileTypeAMGMBRRoster(String fileTypeAMGMBRRoster) {
		this.fileTypeAMGMBRRoster = fileTypeAMGMBRRoster;
	}

	/**
	 * @return the fileTypeAMGCapReport
	 */
	public String getFileTypeAMGCapReport() {
		return fileTypeAMGCapReport;
	}

	/**
	 * @param fileTypeAMGCapReport
	 *            the fileTypeAMGCapReport to set
	 */
	public void setFileTypeAMGCapReport(String fileTypeAMGCapReport) {
		this.fileTypeAMGCapReport = fileTypeAMGCapReport;
	}

	/**
	 * @return the fileTypeBHMBRRoster
	 */
	public String getFileTypeBHMBRRoster() {
		return fileTypeBHMBRRoster;
	}

	/**
	 * @param fileTypeBHMBRRoster
	 *            the fileTypeBHMBRRoster to set
	 */
	public void setFileTypeBHMBRRoster(String fileTypeBHMBRRoster) {
		this.fileTypeBHMBRRoster = fileTypeBHMBRRoster;
	}

	/**
	 * @return the filterByProcessingDate
	 */
	public Integer getFilterByProcessingDate() {
		return filterByProcessingDate;
	}

	/**
	 * @param filterByProcessingDate
	 *            the filterByProcessingDate to set
	 */
	public void setFilterByProcessingDate(Integer filterByProcessingDate) {
		this.filterByProcessingDate = filterByProcessingDate;
	}

	/**
	 * @return the filterByHospitalizationDate
	 */
	public Integer getFilterByHospitalizationDate() {
		return filterByHospitalizationDate;
	}

	/**
	 * @param filterByHospitalizationDate
	 *            the filterByHospitalizationDate to set
	 */
	public void setFilterByHospitalizationDate(Integer filterByHospitalizationDate) {
		this.filterByHospitalizationDate = filterByHospitalizationDate;
	}

	/**
	 * @return the all
	 */
	public Integer getAll() {
		return All;
	}

	/**
	 * @param all
	 *            the all to set
	 */
	public void setAll(Integer all) {
		All = all;
	}

	/**
	 * @return the claim
	 */
	public Integer getClaim() {
		return claim;
	}

	/**
	 * @param claim
	 *            the claim to set
	 */
	public void setClaim(Integer claim) {
		this.claim = claim;
	}

	/**
	 * @return the hospitalization
	 */
	public Integer getHospitalization() {
		return hospitalization;
	}

	/**
	 * @param hospitalization
	 *            the hospitalization to set
	 */
	public void setHospitalization(Integer hospitalization) {
		this.hospitalization = hospitalization;
	}

	/**
	 * @return the acceptableClaim
	 */
	public Integer getAcceptableClaim() {
		return acceptableClaim;
	}

	/**
	 * @param acceptableClaim
	 *            the acceptableClaim to set
	 */
	public void setAcceptableClaim(Integer acceptableClaim) {
		this.acceptableClaim = acceptableClaim;
	}

	/**
	 * @return the unacceptableClaim
	 */
	public Integer getUnacceptableClaim() {
		return unacceptableClaim;
	}

	/**
	 * @param unacceptableClaim
	 *            the unacceptableClaim to set
	 */
	public void setUnacceptableClaim(Integer unacceptableClaim) {
		this.unacceptableClaim = unacceptableClaim;
	}

	/**
	 * @return the queryTypeInsertLevel2
	 */
	public String getQueryTypeInsertLevel2() {
		return queryTypeInsertLevel2;
	}

	/**
	 * @param queryTypeInsertLevel2
	 *            the queryTypeInsertLevel2 to set
	 */
	public void setQueryTypeInsertLevel2(String queryTypeInsertLevel2) {
		this.queryTypeInsertLevel2 = queryTypeInsertLevel2;
	}

}