package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.StoredProcedureParameter;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pfchoice.springboot.model.converter.ICDMeasureListConverter;

/**
 *
 * @author sarath
 */
@Entity(name = "membership_claims")
@FilterDef(
	    name = "reportMonthFilter", 
	    parameters = @ParamDef(name = "reportMonth", type = "int")
	)
	@Filter(
	    name = "reportMonthFilter", 
	    condition = "reportMonth  = :reportMonth"
	)
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "new_clm_report",  procedureName = "NEW_CLM_REPORT", parameters = {
				@StoredProcedureParameter(name = "tableName", type = String.class),
				@StoredProcedureParameter(name = "insId", type = Integer.class),
				@StoredProcedureParameter(name = "prvdrId", type = String.class),
				@StoredProcedureParameter(name = "mbrId", type = Integer.class),
				@StoredProcedureParameter(name = "rptMonth", type = Integer.class),
				@StoredProcedureParameter(name = "activityMonth", type = Integer.class),
				@StoredProcedureParameter(name = "claimType", type = String.class),
				@StoredProcedureParameter(name = "category", type = String.class),
				@StoredProcedureParameter(name = "roster", type = String.class),
				@StoredProcedureParameter(name = "cap", type = String.class),
				@StoredProcedureParameter(name = "levelNo", type = Integer.class),
				@StoredProcedureParameter(name = "maxReportMonth", type = Integer.class) }) })
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipClaim extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_claim_id", nullable = false)
	private Integer id;

	@Column(name = "claim_id_number")
	private String claimNumber;

	@Column(name = "report_month")
	private Integer report_month;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id")
	private Membership mbr;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id")
	@Where(clause = "active_ind ='Y'")
	private Provider prvdr;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ins_id", referencedColumnName = "insurance_id")
	@Where(clause = "active_ind ='Y'")
	private Insurance ins;

	@Column(name = "claim_type")
	private String claimType;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "facility_type_code", referencedColumnName = "code")
	@Where(clause = "active_ind ='Y'")
	private FacilityType facilityType;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@Where(clause = "active_ind ='Y'")
	@JoinColumn(name = "bill_type_code", referencedColumnName = "code")
	private BillType billType;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@Where(clause = "active_ind ='Y'")
	@JoinColumn(name = "frequency_type_code", referencedColumnName = "code")
	private FrequencyType frequencyType;

	@Column(name = "bill_type")
	private String billTypec;

	@Column(name = "dischargestatus")
	private String dischargeStatus;

	@Column(name = "memEnrollId")
	private String MemEnrollId;

	@Column(name = "diagnoses")
	private String diagnosis;

	@Column(name = "product_label")
	private String productLabel;

	@Column(name = "product_lvl1")
	private String productLvl1;

	@Column(name = "product_lvl2")
	private String productLvl2;

	@Column(name = "product_lvl3")
	private String productLvl3;

	@Column(name = "product_lvl4")
	private String productLvl4;

	@Column(name = "product_lvl5")
	private String productLvl5;

	@Column(name = "product_lvl6")
	private String productLvl6;

	@Column(name = "product_lvl7")
	private String productLvl7;

	@Column(name = "market_lvl1")
	private String marketLvl1;

	@Column(name = "market_lvl2")
	private String marketLvl2;

	@Column(name = "market_lvl3")
	private String marketLvl3;

	@Column(name = "market_lvl4")
	private String marketLvl4;

	@Column(name = "market_lvl5")
	private String marketLvl5;

	@Column(name = "market_lvl6")
	private String marketLvl6;

	@Column(name = "market_lvl7")
	private String marketLvl7;

	@Column(name = "market_lvl8")
	private String marketLvl8;

	@Column(name = "tin")
	private String tin;

	@Column(name = "dx_type_cd")
	private String dxTypeCode;

	@Column(name = "proc_type_cd")
	private String procTypeCode;

	@Column(name = "file_id")
	private Integer fileId;

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "mbrClaim", fetch = FetchType.LAZY)
	private List<MembershipClaimDetails> mbrClaimDetailsList;

	@Fetch(FetchMode.SELECT)
	@Column(name = "diagnoses", insertable = false, updatable = false)
	@Convert(converter = ICDMeasureListConverter.class)
	private List<ICDMeasure> icdCodesList;

	/**
	 * 
	 */
	public MembershipClaim() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipClaim(final Integer id) {
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
	 * @return the claimNumber
	 */
	public String getClaimNumber() {
		return claimNumber;
	}

	/**
	 * @param claimNumber
	 *            the claimNumber to set
	 */
	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}

	/**
	 * @return the mbr
	 */
	public Membership getMbr() {
		return mbr;
	}

	/**
	 * @param mbr
	 *            the mbr to set
	 */
	public void setMbr(Membership mbr) {
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
	 * @return the claimType
	 */
	public String getClaimType() {
		return claimType;
	}

	/**
	 * @param claimType
	 *            the claimType to set
	 */
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	/**
	 * @return the frequencyType
	 */
	public FrequencyType getFrequencyType() {
		return frequencyType;
	}

	/**
	 * @param frequencyType
	 *            the frequencyType to set
	 */
	public void setFrequencyType(FrequencyType frequencyType) {
		this.frequencyType = frequencyType;
	}

	/**
	 * @return the billType
	 */
	public String getBillTypec() {
		return billTypec;
	}

	/**
	 * @param billType
	 *            the billType to set
	 */
	public void setBillTypec(String billTypec) {
		this.billTypec = billTypec;
	}

	/**
	 * @return the dischargeStatus
	 */
	public String getDischargeStatus() {
		return dischargeStatus;
	}

	/**
	 * @param dischargeStatus
	 *            the dischargeStatus to set
	 */
	public void setDischargeStatus(String dischargeStatus) {
		this.dischargeStatus = dischargeStatus;
	}

	/**
	 * @return the memEnrollId
	 */
	public String getMemEnrollId() {
		return MemEnrollId;
	}

	/**
	 * @param memEnrollId
	 *            the memEnrollId to set
	 */
	public void setMemEnrollId(String memEnrollId) {
		MemEnrollId = memEnrollId;
	}

	/**
	 * @return the diagnosis
	 */
	public String getDiagnosis() {
		return diagnosis;
	}

	/**
	 * @param diagnosis
	 *            the diagnosis to set
	 */
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	/**
	 * @return the productLabel
	 */
	public String getProductLabel() {
		return productLabel;
	}

	/**
	 * @param productLabel
	 *            the productLabel to set
	 */
	public void setProductLabel(String productLabel) {
		this.productLabel = productLabel;
	}

	/**
	 * @return the productLvl1
	 */
	public String getProductLvl1() {
		return productLvl1;
	}

	/**
	 * @param productLvl1
	 *            the productLvl1 to set
	 */
	public void setProductLvl1(String productLvl1) {
		this.productLvl1 = productLvl1;
	}

	/**
	 * @return the productLvl2
	 */
	public String getProductLvl2() {
		return productLvl2;
	}

	/**
	 * @param productLvl2
	 *            the productLvl2 to set
	 */
	public void setProductLvl2(String productLvl2) {
		this.productLvl2 = productLvl2;
	}

	/**
	 * @return the productLvl3
	 */
	public String getProductLvl3() {
		return productLvl3;
	}

	/**
	 * @param productLvl3
	 *            the productLvl3 to set
	 */
	public void setProductLvl3(String productLvl3) {
		this.productLvl3 = productLvl3;
	}

	/**
	 * @return the productLvl4
	 */
	public String getProductLvl4() {
		return productLvl4;
	}

	/**
	 * @param productLvl4
	 *            the productLvl4 to set
	 */
	public void setProductLvl4(String productLvl4) {
		this.productLvl4 = productLvl4;
	}

	/**
	 * @return the productLvl5
	 */
	public String getProductLvl5() {
		return productLvl5;
	}

	/**
	 * @param productLvl5
	 *            the productLvl5 to set
	 */
	public void setProductLvl5(String productLvl5) {
		this.productLvl5 = productLvl5;
	}

	/**
	 * @return the productLvl6
	 */
	public String getProductLvl6() {
		return productLvl6;
	}

	/**
	 * @param productLvl6
	 *            the productLvl6 to set
	 */
	public void setProductLvl6(String productLvl6) {
		this.productLvl6 = productLvl6;
	}

	/**
	 * @return the productLvl7
	 */
	public String getProductLvl7() {
		return productLvl7;
	}

	/**
	 * @param productLvl7
	 *            the productLvl7 to set
	 */
	public void setProductLvl7(String productLvl7) {
		this.productLvl7 = productLvl7;
	}

	/**
	 * @return the marketLvl1
	 */
	public String getMarketLvl1() {
		return marketLvl1;
	}

	/**
	 * @param marketLvl1
	 *            the marketLvl1 to set
	 */
	public void setMarketLvl1(String marketLvl1) {
		this.marketLvl1 = marketLvl1;
	}

	/**
	 * @return the marketLvl2
	 */
	public String getMarketLvl2() {
		return marketLvl2;
	}

	/**
	 * @param marketLvl2
	 *            the marketLvl2 to set
	 */
	public void setMarketLvl2(String marketLvl2) {
		this.marketLvl2 = marketLvl2;
	}

	/**
	 * @return the marketLvl3
	 */
	public String getMarketLvl3() {
		return marketLvl3;
	}

	/**
	 * @param marketLvl3
	 *            the marketLvl3 to set
	 */
	public void setMarketLvl3(String marketLvl3) {
		this.marketLvl3 = marketLvl3;
	}

	/**
	 * @return the marketLvl4
	 */
	public String getMarketLvl4() {
		return marketLvl4;
	}

	/**
	 * @param marketLvl4
	 *            the marketLvl4 to set
	 */
	public void setMarketLvl4(String marketLvl4) {
		this.marketLvl4 = marketLvl4;
	}

	/**
	 * @return the marketLvl5
	 */
	public String getMarketLvl5() {
		return marketLvl5;
	}

	/**
	 * @param marketLvl5
	 *            the marketLvl5 to set
	 */
	public void setMarketLvl5(String marketLvl5) {
		this.marketLvl5 = marketLvl5;
	}

	/**
	 * @return the marketLvl6
	 */
	public String getMarketLvl6() {
		return marketLvl6;
	}

	/**
	 * @param marketLvl6
	 *            the marketLvl6 to set
	 */
	public void setMarketLvl6(String marketLvl6) {
		this.marketLvl6 = marketLvl6;
	}

	/**
	 * @return the marketLvl7
	 */
	public String getMarketLvl7() {
		return marketLvl7;
	}

	/**
	 * @param marketLvl7
	 *            the marketLvl7 to set
	 */
	public void setMarketLvl7(String marketLvl7) {
		this.marketLvl7 = marketLvl7;
	}

	/**
	 * @return the marketLvl8
	 */
	public String getMarketLvl8() {
		return marketLvl8;
	}

	/**
	 * @param marketLvl8
	 *            the marketLvl8 to set
	 */
	public void setMarketLvl8(String marketLvl8) {
		this.marketLvl8 = marketLvl8;
	}

	/**
	 * @return the tin
	 */
	public String getTin() {
		return tin;
	}

	/**
	 * @param tin
	 *            the tin to set
	 */
	public void setTin(String tin) {
		this.tin = tin;
	}

	/**
	 * @return the dxTypeCode
	 */
	public String getDxTypeCode() {
		return dxTypeCode;
	}

	/**
	 * @param dxTypeCode
	 *            the dxTypeCode to set
	 */
	public void setDxTypeCode(String dxTypeCode) {
		this.dxTypeCode = dxTypeCode;
	}

	/**
	 * @return the procTypeCode
	 */
	public String getProcTypeCode() {
		return procTypeCode;
	}

	/**
	 * @param procTypeCode
	 *            the procTypeCode to set
	 */
	public void setProcTypeCode(String procTypeCode) {
		this.procTypeCode = procTypeCode;
	}

	/**
	 * @return the fileId
	 */
	public Integer getFileId() {
		return fileId;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the mbrClaimDetailsList
	 */
	public List<MembershipClaimDetails> getMbrClaimDetailsList() {
		return mbrClaimDetailsList;
	}

	/**
	 * @param mbrClaimDetailsList
	 *            the mbrClaimDetailsList to set
	 */
	public void setMbrClaimDetailsList(List<MembershipClaimDetails> mbrClaimDetailsList) {
		this.mbrClaimDetailsList = mbrClaimDetailsList;
	}

	/**
	 * @return the facilityType
	 */
	public FacilityType getFacilityType() {
		return facilityType;
	}

	/**
	 * @param facilityType
	 *            the facilityType to set
	 */
	public void setFacilityType(FacilityType facilityType) {
		this.facilityType = facilityType;
	}

	/**
	 * @return the billType
	 */
	public BillType getBillType() {
		return billType;
	}

	/**
	 * @param billType
	 *            the billType to set
	 */
	public void setBillType(BillType billType) {
		this.billType = billType;
	}

	/**
	 * @return the icdCodesList
	 */
	public List<ICDMeasure> getIcdCodesList() {
		return icdCodesList;
	}

	/**
	 * @param icdCodesList
	 *            the icdCodesList to set
	 */
	public void setIcdCodesList(List<ICDMeasure> icdCodesList) {
		this.icdCodesList = icdCodesList;
	}

	/**
	 * @return the report_month
	 */
	public Integer getReport_month() {
		return report_month;
	}

	/**
	 * @param report_month the report_month to set
	 */
	public void setReport_month(Integer report_month) {
		this.report_month = report_month;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipClaim)) {
			return false;
		}
		MembershipClaim other = (MembershipClaim) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipClaim[ id=" + id + " ]";
	}

}
