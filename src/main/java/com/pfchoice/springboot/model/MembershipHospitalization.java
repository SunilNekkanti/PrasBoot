package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity(name = "membership_hospitalization")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipHospitalization extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;
  
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_hos_id", nullable = false)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name = "process_date", nullable = false)
	protected Date processingDate;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id")
	private MembershipDTO mbr;

	@OneToOne(fetch = FetchType.LAZY)
	@Where(clause = "active_ind ='Y'")
	@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id")
	private Provider prvdr;

	@OneToOne(fetch = FetchType.LAZY)
	@Where(clause = "active_ind ='Y'")
	@JoinColumn(name = "ins_id", referencedColumnName = "insurance_id")
	private Insurance ins;
 
	@Column(name = "Line_of_Business")
	private String lineOfBusiness;
  
	@Column(name = "Product")
	private String product;

	@Column(name = "Home_Plan_Parent_Co")
	private String homePlanParentCo;

	@Column(name = "Home_Plan_Name")
	protected String homePlanName;

	@Column(name = "Hot_Spotter_Chronic")
	protected String hotSpotterChronic;
	
	@Column(name = "months_as_Hot_Spotter_Chronic")
	protected String monthsAsHotSpotterChronic;

	@Column(name = "Hot_Spotter_Readmission")
	protected String hotSpotterReadmission;
	
	@Column(name = "Risk_Drivers")
	protected String riskDrivers;
	
	@Column(name = "Inpatient_Authorization")
	protected String inpatientAuthorization;
	
	@Column(name = "New_Attribution")
	protected String newAttribution;
	
	@Column(name = "Provider_Specialty")
	protected String providerSpecialty;
	
	@Column(name = "Organization")
	protected String organization;
	
	@Column(name = "Organization_Tin")
	protected String organizationTin;
	
	@Column(name = "Prospective_Risk_Score")
	protected String prospectiveRiskScore;
	
	@Column(name = "Condition_based_Opportunities")
	protected String conditionBasedOpportunities;
	
	@Column(name = "CDOI")
	protected String cdoi;
	
	@Column(name = "CDOI_High_Priority")
	protected String cdoiHighPriority;
	
	@Column(name = "Need_Annual_Visit")
	protected String needAnnualVisit;
	
	@Column(name = "Last_Annual_Visit_Date")
	protected String lastAnnualVisitDate;
	
	@Column(name = "Health_Assessment")
	protected String healthAssessment;
	
	@Column(name = "ER_Visits")
	protected String erVisits;
	
	@Column(name = "ER_No_of_Visits")
	protected String erNoOfVisits;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ER_Last_Visit_Date")
	protected Date erLastVisitDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "ER_Visit_Date")
	protected Date erVisitDate;
	
	@Column(name = "Day_of_Week")
	protected String dayOfWeek;
	
	@Column(name = "Facility_Name")
	protected String facilityName;
	
	@Column(name = "Potentially_Avoidable_ER_Visit")
	protected String potentiallyAvoidableERVisit;
	
	@Column(name = "Primary_Diagnosis")
	protected String primaryDiagnosis;
	
	@Column(name = "Secondary_Diagnosis_1")
	protected String secondaryDiagnosis1;
	
	@Column(name = "Secondary_Diagnosis_2")
	protected String secondaryDiagnosis2;
	
	@Column(name = "Secondary_Diagnosis_3")
	protected String secondaryDiagnosis3;
	
	@Column(name = "file_id")
	private Integer fileId;


	/**
	 * 
	 */
	public MembershipHospitalization() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipHospitalization(final Integer id) {
		super();
		this.id = id;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @return the mbr
	 */
	public MembershipDTO getMbr() {
		return mbr;
	}

	/**
	 * @param mbr
	 *            the mbr to set
	 */
	public void setMbr(MembershipDTO mbr) {
		this.mbr = mbr;
	}

	/**
	 * @return the prvdr
	 */
	public Provider getPrvdr() {
		return prvdr;
	}

	/**
	 * @param prvdr
	 *            the prvdr to set
	 */
	public void setPrvdr(Provider prvdr) {
		this.prvdr = prvdr;
	}

	/**
	 * @return the ins
	 */
	public Insurance getIns() {
		return ins;
	}

	/**
	 * @param ins
	 *            the ins to set
	 */
	public void setIns(Insurance ins) {
		this.ins = ins;
	}

	/**
	 * @return the processingDate
	 */
	public Date getProcessingDate() {
		return processingDate;
	}

	/**
	 * @param processingDate the processingDate to set
	 */
	public void setProcessingDate(Date processingDate) {
		this.processingDate = processingDate;
	}

	/**
	 * @return the lineOfBusiness
	 */
	public String getLineOfBusiness() {
		return lineOfBusiness;
	}

	/**
	 * @param lineOfBusiness the lineOfBusiness to set
	 */
	public void setLineOfBusiness(String lineOfBusiness) {
		this.lineOfBusiness = lineOfBusiness;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the homePlanParentCo
	 */
	public String getHomePlanParentCo() {
		return homePlanParentCo;
	}

	/**
	 * @param homePlanParentCo the homePlanParentCo to set
	 */
	public void setHomePlanParentCo(String homePlanParentCo) {
		this.homePlanParentCo = homePlanParentCo;
	}

	/**
	 * @return the homePlanName
	 */
	public String getHomePlanName() {
		return homePlanName;
	}

	/**
	 * @param homePlanName the homePlanName to set
	 */
	public void setHomePlanName(String homePlanName) {
		this.homePlanName = homePlanName;
	}

	/**
	 * @return the hotSpotterChronic
	 */
	public String getHotSpotterChronic() {
		return hotSpotterChronic;
	}

	/**
	 * @param hotSpotterChronic the hotSpotterChronic to set
	 */
	public void setHotSpotterChronic(String hotSpotterChronic) {
		this.hotSpotterChronic = hotSpotterChronic;
	}

	/**
	 * @return the monthsAsHotSpotterChronic
	 */
	public String getMonthsAsHotSpotterChronic() {
		return monthsAsHotSpotterChronic;
	}

	/**
	 * @param monthsAsHotSpotterChronic the monthsAsHotSpotterChronic to set
	 */
	public void setMonthsAsHotSpotterChronic(String monthsAsHotSpotterChronic) {
		this.monthsAsHotSpotterChronic = monthsAsHotSpotterChronic;
	}

	/**
	 * @return the hotSpotterReadmission
	 */
	public String getHotSpotterReadmission() {
		return hotSpotterReadmission;
	}

	/**
	 * @param hotSpotterReadmission the hotSpotterReadmission to set
	 */
	public void setHotSpotterReadmission(String hotSpotterReadmission) {
		this.hotSpotterReadmission = hotSpotterReadmission;
	}

	/**
	 * @return the riskDrivers
	 */
	public String getRiskDrivers() {
		return riskDrivers;
	}

	/**
	 * @param riskDrivers the riskDrivers to set
	 */
	public void setRiskDrivers(String riskDrivers) {
		this.riskDrivers = riskDrivers;
	}

	/**
	 * @return the inpatientAuthorization
	 */
	public String getInpatientAuthorization() {
		return inpatientAuthorization;
	}

	/**
	 * @param inpatientAuthorization the inpatientAuthorization to set
	 */
	public void setInpatientAuthorization(String inpatientAuthorization) {
		this.inpatientAuthorization = inpatientAuthorization;
	}

	/**
	 * @return the newAttribution
	 */
	public String getNewAttribution() {
		return newAttribution;
	}

	/**
	 * @param newAttribution the newAttribution to set
	 */
	public void setNewAttribution(String newAttribution) {
		this.newAttribution = newAttribution;
	}

	/**
	 * @return the providerSpecialty
	 */
	public String getProviderSpecialty() {
		return providerSpecialty;
	}

	/**
	 * @param providerSpecialty the providerSpecialty to set
	 */
	public void setProviderSpecialty(String providerSpecialty) {
		this.providerSpecialty = providerSpecialty;
	}

	/**
	 * @return the organization
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * @param organization the organization to set
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * @return the organizationTin
	 */
	public String getOrganizationTin() {
		return organizationTin;
	}

	/**
	 * @param organizationTin the organizationTin to set
	 */
	public void setOrganizationTin(String organizationTin) {
		this.organizationTin = organizationTin;
	}

	/**
	 * @return the prospectiveRiskScore
	 */
	public String getProspectiveRiskScore() {
		return prospectiveRiskScore;
	}

	/**
	 * @param prospectiveRiskScore the prospectiveRiskScore to set
	 */
	public void setProspectiveRiskScore(String prospectiveRiskScore) {
		this.prospectiveRiskScore = prospectiveRiskScore;
	}

	/**
	 * @return the conditionBasedOpportunities
	 */
	public String getConditionBasedOpportunities() {
		return conditionBasedOpportunities;
	}

	/**
	 * @param conditionBasedOpportunities the conditionBasedOpportunities to set
	 */
	public void setConditionBasedOpportunities(String conditionBasedOpportunities) {
		this.conditionBasedOpportunities = conditionBasedOpportunities;
	}

	/**
	 * @return the cdoi
	 */
	public String getCdoi() {
		return cdoi;
	}

	/**
	 * @param cdoi the cdoi to set
	 */
	public void setCdoi(String cdoi) {
		this.cdoi = cdoi;
	}

	/**
	 * @return the cdoiHighPriority
	 */
	public String getCdoiHighPriority() {
		return cdoiHighPriority;
	}

	/**
	 * @param cdoiHighPriority the cdoiHighPriority to set
	 */
	public void setCdoiHighPriority(String cdoiHighPriority) {
		this.cdoiHighPriority = cdoiHighPriority;
	}

	/**
	 * @return the needAnnualVisit
	 */
	public String getNeedAnnualVisit() {
		return needAnnualVisit;
	}

	/**
	 * @param needAnnualVisit the needAnnualVisit to set
	 */
	public void setNeedAnnualVisit(String needAnnualVisit) {
		this.needAnnualVisit = needAnnualVisit;
	}

	/**
	 * @return the lastAnnualVisitDate
	 */
	public String getLastAnnualVisitDate() {
		return lastAnnualVisitDate;
	}

	/**
	 * @param lastAnnualVisitDate the lastAnnualVisitDate to set
	 */
	public void setLastAnnualVisitDate(String lastAnnualVisitDate) {
		this.lastAnnualVisitDate = lastAnnualVisitDate;
	}

	/**
	 * @return the healthAssessment
	 */
	public String getHealthAssessment() {
		return healthAssessment;
	}

	/**
	 * @param healthAssessment the healthAssessment to set
	 */
	public void setHealthAssessment(String healthAssessment) {
		this.healthAssessment = healthAssessment;
	}

	/**
	 * @return the erVisits
	 */
	public String getErVisits() {
		return erVisits;
	}

	/**
	 * @param erVisits the erVisits to set
	 */
	public void setErVisits(String erVisits) {
		this.erVisits = erVisits;
	}

	/**
	 * @return the erNoOfVisits
	 */
	public String getErNoOfVisits() {
		return erNoOfVisits;
	}

	/**
	 * @param erNoOfVisits the erNoOfVisits to set
	 */
	public void setErNoOfVisits(String erNoOfVisits) {
		this.erNoOfVisits = erNoOfVisits;
	}

	/**
	 * @return the erLastVisitDate
	 */
	public Date getErLastVisitDate() {
		return erLastVisitDate;
	}

	/**
	 * @param erLastVisitDate the erLastVisitDate to set
	 */
	public void setErLastVisitDate(Date erLastVisitDate) {
		this.erLastVisitDate = erLastVisitDate;
	}

	/**
	 * @return the erVisitDate
	 */
	public Date getErVisitDate() {
		return erVisitDate;
	}

	/**
	 * @param erVisitDate the erVisitDate to set
	 */
	public void setErVisitDate(Date erVisitDate) {
		this.erVisitDate = erVisitDate;
	}

	/**
	 * @return the dayOfWeek
	 */
	public String getDayOfWeek() {
		return dayOfWeek;
	}

	/**
	 * @param dayOfWeek the dayOfWeek to set
	 */
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	/**
	 * @return the facilityName
	 */
	public String getFacilityName() {
		return facilityName;
	}

	/**
	 * @param facilityName the facilityName to set
	 */
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	/**
	 * @return the potentiallyAvoidableERVisit
	 */
	public String getPotentiallyAvoidableERVisit() {
		return potentiallyAvoidableERVisit;
	}

	/**
	 * @param potentiallyAvoidableERVisit the potentiallyAvoidableERVisit to set
	 */
	public void setPotentiallyAvoidableERVisit(String potentiallyAvoidableERVisit) {
		this.potentiallyAvoidableERVisit = potentiallyAvoidableERVisit;
	}

	/**
	 * @return the primaryDiagnosis
	 */
	public String getPrimaryDiagnosis() {
		return primaryDiagnosis;
	}

	/**
	 * @param primaryDiagnosis the primaryDiagnosis to set
	 */
	public void setPrimaryDiagnosis(String primaryDiagnosis) {
		this.primaryDiagnosis = primaryDiagnosis;
	}

	/**
	 * @return the secondaryDiagnosis1
	 */
	public String getSecondaryDiagnosis1() {
		return secondaryDiagnosis1;
	}

	/**
	 * @param secondaryDiagnosis1 the secondaryDiagnosis1 to set
	 */
	public void setSecondaryDiagnosis1(String secondaryDiagnosis1) {
		this.secondaryDiagnosis1 = secondaryDiagnosis1;
	}

	/**
	 * @return the secondaryDiagnosis2
	 */
	public String getSecondaryDiagnosis2() {
		return secondaryDiagnosis2;
	}

	/**
	 * @param secondaryDiagnosis2 the secondaryDiagnosis2 to set
	 */
	public void setSecondaryDiagnosis2(String secondaryDiagnosis2) {
		this.secondaryDiagnosis2 = secondaryDiagnosis2;
	}

	/**
	 * @return the secondaryDiagnosis3
	 */
	public String getSecondaryDiagnosis3() {
		return secondaryDiagnosis3;
	}

	/**
	 * @param secondaryDiagnosis3 the secondaryDiagnosis3 to set
	 */
	public void setSecondaryDiagnosis3(String secondaryDiagnosis3) {
		this.secondaryDiagnosis3 = secondaryDiagnosis3;
	}

	/**
	 * @return the fileId
	 */
	public Integer getFileId() {
		return fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipHospitalization)) {
			return false;
		}
		MembershipHospitalization other = (MembershipHospitalization) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipHospitalization[ id=" + id + " ]";
	}

}
