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
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity(name = "membership_claim_details")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipClaimDetails extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_claim_details_id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mbr_claim_id", referencedColumnName = "mbr_claim_id")
	private MembershipClaim mbrClaim;

	@Column(name = "claim_line_seq_nbr")
	private String claimLineseqNbr;

	@Column(name = "clm_line_adj_seq_nbr")
	private String clmLineAdjSeqNbr;

	@Temporal(TemporalType.DATE)
	@Column(name = "activity_date")
	protected Date activityDate;

	@Column(name = "activity_month")
	private Integer activityMonth;

	@Temporal(TemporalType.DATE)
	@Column(name = "claim_start_date")
	protected Date claimStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "claim_end_date")
	protected Date claimEndDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "paid_date")
	protected Date paidDate;

	@Column(name = "revenue_code")
	private String revenueCode;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "cpt_code", referencedColumnName = "cpt_id")
	private CPTMeasure cpt;

	@Column(name = "cpt_code_modifier1")
	private String cptCodeModifier1;

	@Column(name = "cpt_code_modifier2")
	private String cptCodeModifier2;

	@Column(name = "claim_status")
	private String claimStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id", referencedColumnName = "id")
	private PlaceOfService roomType;

	@Column(name = "risk_recon_cos_des")
	private String riskReconCosDes;

	@Column(name = "amount_paid")
	private Double amountPaid;

	@Column(name = "allow_amt")
	private Double allowAmt;

	@Column(name = "co_insurance")
	private Double coInsurance;

	@Column(name = "co_pay")
	private Double coPay;

	@Column(name = "deductible")
	private Double deductible;

	@Column(name = "cob_paid_amount")
	private Double cobPaidAmount;

	@Column(name = "processing_status")
	private String processingStatus;

	@Column(name = "pharmacy_name")
	private String pharmacyName;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "npos")
	private String npos;

	@Column(name = "risk_id")
	private String riskId;

	@Temporal(TemporalType.DATE)
	@Column(name = "runn_date")
	protected Date runnDate;

	@Column(name = "ndc")
	private String ndc;

	@Column(name = "mony")
	private String mony;

	@Column(name = "drug_label_name")
	private String drugLabelName;

	@Column(name = "drug_version")
	private String drugVersion;

	@Column(name = "pharmacy")
	private String pharmacy;

	@Column(name = "membership_claims")
	private Double membershipClaims;

	@Column(name = "psychare")
	private String psychare;

	@Column(name = "simple_county")
	private String simpleCounty;

	@Column(name = "triangles")
	private String triangles;

	@Column(name = "cover")
	private String cover;

	@Column(name = "file_id")
	private Integer fileId;

	/**
	 * 
	 */
	public MembershipClaimDetails() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipClaimDetails(final Integer id) {
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
	 * @return the mbrClaim
	 */
	public MembershipClaim getMbrClaim() {
		return mbrClaim;
	}

	/**
	 * @param mbrClaim
	 *            the mbrClaim to set
	 */
	public void setMbrClaim(MembershipClaim mbrClaim) {
		this.mbrClaim = mbrClaim;
	}

	/**
	 * @return the claimLineseqNbr
	 */
	public String getClaimLineseqNbr() {
		return claimLineseqNbr;
	}

	/**
	 * @param claimLineseqNbr
	 *            the claimLineseqNbr to set
	 */
	public void setClaimLineseqNbr(String claimLineseqNbr) {
		this.claimLineseqNbr = claimLineseqNbr;
	}

	/**
	 * @return the clmLineAdjSeqNbr
	 */
	public String getClmLineAdjSeqNbr() {
		return clmLineAdjSeqNbr;
	}

	/**
	 * @param clmLineAdjSeqNbr
	 *            the clmLineAdjSeqNbr to set
	 */
	public void setClmLineAdjSeqNbr(String clmLineAdjSeqNbr) {
		this.clmLineAdjSeqNbr = clmLineAdjSeqNbr;
	}

	/**
	 * @return the activityDate
	 */
	public Date getActivityDate() {
		return activityDate;
	}

	/**
	 * @param activityDate
	 *            the activityDate to set
	 */
	public void setActivityDate(Date activityDate) {
		this.activityDate = activityDate;
	}

	/**
	 * @return the activityMonth
	 */
	public Integer getActivityMonth() {
		return activityMonth;
	}

	/**
	 * @param activityMonth
	 *            the activityMonth to set
	 */
	public void setActivityMonth(Integer activityMonth) {
		this.activityMonth = activityMonth;
	}

	/**
	 * @return the claimStartDate
	 */
	public Date getClaimStartDate() {
		return claimStartDate;
	}

	/**
	 * @param claimStartDate
	 *            the claimStartDate to set
	 */
	public void setClaimStartDate(Date claimStartDate) {
		this.claimStartDate = claimStartDate;
	}

	/**
	 * @return the claimEndDate
	 */
	public Date getClaimEndDate() {
		return claimEndDate;
	}

	/**
	 * @param claimEndDate
	 *            the claimEndDate to set
	 */
	public void setClaimEndDate(Date claimEndDate) {
		this.claimEndDate = claimEndDate;
	}

	/**
	 * @return the paidDate
	 */
	public Date getPaidDate() {
		return paidDate;
	}

	/**
	 * @param paidDate
	 *            the paidDate to set
	 */
	public void setPaidDate(Date paidDate) {
		this.paidDate = paidDate;
	}

	/**
	 * @return the revenueCode
	 */
	public String getRevenueCode() {
		return revenueCode;
	}

	/**
	 * @param revenueCode
	 *            the revenueCode to set
	 */
	public void setRevenueCode(String revenueCode) {
		this.revenueCode = revenueCode;
	}

	/**
	 * @return the cpt
	 */
	public CPTMeasure getCpt() {
		return cpt;
	}

	/**
	 * @param cpt
	 *            the cpt to set
	 */
	public void setCpt(CPTMeasure cpt) {
		this.cpt = cpt;
	}

	/**
	 * @return the cptCodeModifier1
	 */
	public String getCptCodeModifier1() {
		return cptCodeModifier1;
	}

	/**
	 * @param cptCodeModifier1
	 *            the cptCodeModifier1 to set
	 */
	public void setCptCodeModifier1(String cptCodeModifier1) {
		this.cptCodeModifier1 = cptCodeModifier1;
	}

	/**
	 * @return the cptCodeModifier2
	 */
	public String getCptCodeModifier2() {
		return cptCodeModifier2;
	}

	/**
	 * @param cptCodeModifier2
	 *            the cptCodeModifier2 to set
	 */
	public void setCptCodeModifier2(String cptCodeModifier2) {
		this.cptCodeModifier2 = cptCodeModifier2;
	}

	/**
	 * @return the claimStatus
	 */
	public String getClaimStatus() {
		return claimStatus;
	}

	/**
	 * @param claimStatus
	 *            the claimStatus to set
	 */
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	/**
	 * @return the roomType
	 */
	public PlaceOfService getRoomType() {
		return roomType;
	}

	/**
	 * @param roomType
	 *            the roomType to set
	 */
	public void setRoomType(PlaceOfService roomType) {
		this.roomType = roomType;
	}

	/**
	 * @return the riskReconCosDes
	 */
	public String getRiskReconCosDes() {
		return riskReconCosDes;
	}

	/**
	 * @param riskReconCosDes
	 *            the riskReconCosDes to set
	 */
	public void setRiskReconCosDes(String riskReconCosDes) {
		this.riskReconCosDes = riskReconCosDes;
	}

	/**
	 * @return the amountPaid
	 */
	public Double getAmountPaid() {
		return amountPaid;
	}

	/**
	 * @param amountPaid
	 *            the amountPaid to set
	 */
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	/**
	 * @return the allowAmt
	 */
	public Double getAllowAmt() {
		return allowAmt;
	}

	/**
	 * @param allowAmt
	 *            the allowAmt to set
	 */
	public void setAllowAmt(Double allowAmt) {
		this.allowAmt = allowAmt;
	}

	/**
	 * @return the coInsurance
	 */
	public Double getCoInsurance() {
		return coInsurance;
	}

	/**
	 * @param coInsurance
	 *            the coInsurance to set
	 */
	public void setCoInsurance(Double coInsurance) {
		this.coInsurance = coInsurance;
	}

	/**
	 * @return the coPay
	 */
	public Double getCoPay() {
		return coPay;
	}

	/**
	 * @param coPay
	 *            the coPay to set
	 */
	public void setCoPay(Double coPay) {
		this.coPay = coPay;
	}

	/**
	 * @return the deductible
	 */
	public Double getDeductible() {
		return deductible;
	}

	/**
	 * @param deductible
	 *            the deductible to set
	 */
	public void setDeductible(Double deductible) {
		this.deductible = deductible;
	}

	/**
	 * @return the cobPaidAmount
	 */
	public Double getCobPaidAmount() {
		return cobPaidAmount;
	}

	/**
	 * @param cobPaidAmount
	 *            the cobPaidAmount to set
	 */
	public void setCobPaidAmount(Double cobPaidAmount) {
		this.cobPaidAmount = cobPaidAmount;
	}

	/**
	 * @return the processingStatus
	 */
	public String getProcessingStatus() {
		return processingStatus;
	}

	/**
	 * @param processingStatus
	 *            the processingStatus to set
	 */
	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}

	/**
	 * @return the pharmacyName
	 */
	public String getPharmacyName() {
		return pharmacyName;
	}

	/**
	 * @param pharmacyName
	 *            the pharmacyName to set
	 */
	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the npos
	 */
	public String getNpos() {
		return npos;
	}

	/**
	 * @param npos
	 *            the npos to set
	 */
	public void setNpos(String npos) {
		this.npos = npos;
	}

	/**
	 * @return the riskId
	 */
	public String getRiskId() {
		return riskId;
	}

	/**
	 * @param riskId
	 *            the riskId to set
	 */
	public void setRiskId(String riskId) {
		this.riskId = riskId;
	}

	/**
	 * @return the runnDate
	 */
	public Date getRunnDate() {
		return runnDate;
	}

	/**
	 * @param runnDate
	 *            the runnDate to set
	 */
	public void setRunnDate(Date runnDate) {
		this.runnDate = runnDate;
	}

	/**
	 * @return the ndc
	 */
	public String getNdc() {
		return ndc;
	}

	/**
	 * @param ndc
	 *            the ndc to set
	 */
	public void setNdc(String ndc) {
		this.ndc = ndc;
	}

	/**
	 * @return the pharmacy
	 */
	public String getPharmacy() {
		return pharmacy;
	}

	/**
	 * @param pharmacy
	 *            the pharmacy to set
	 */
	public void setPharmacy(String pharmacy) {
		this.pharmacy = pharmacy;
	}

	/**
	 * @return the membershipClaims
	 */
	public Double getMembershipClaims() {
		return membershipClaims;
	}

	/**
	 * @param membershipClaims
	 *            the membershipClaims to set
	 */
	public void setMembershipClaims(Double membershipClaims) {
		this.membershipClaims = membershipClaims;
	}

	/**
	 * @return the psychare
	 */
	public String getPsychare() {
		return psychare;
	}

	/**
	 * @param psychare
	 *            the psychare to set
	 */
	public void setPsychare(String psychare) {
		this.psychare = psychare;
	}

	/**
	 * @return the simpleCounty
	 */
	public String getSimpleCounty() {
		return simpleCounty;
	}

	/**
	 * @param simpleCounty
	 *            the simpleCounty to set
	 */
	public void setSimpleCounty(String simpleCounty) {
		this.simpleCounty = simpleCounty;
	}

	/**
	 * @return the triangles
	 */
	public String getTriangles() {
		return triangles;
	}

	/**
	 * @param triangles
	 *            the triangles to set
	 */
	public void setTriangles(String triangles) {
		this.triangles = triangles;
	}

	/**
	 * @return the cover
	 */
	public String getCover() {
		return cover;
	}

	/**
	 * @param cover
	 *            the cover to set
	 */
	public void setCover(String cover) {
		this.cover = cover;
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
	public void setFileId(final Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the mony
	 */
	public String getMony() {
		return mony;
	}

	/**
	 * @param mony
	 *            the mony to set
	 */
	public void setMony(String mony) {
		this.mony = mony;
	}

	/**
	 * @return the drugLabelName
	 */
	public String getDrugLabelName() {
		return drugLabelName;
	}

	/**
	 * @param drugLabelName
	 *            the drugLabelName to set
	 */
	public void setDrugLabelName(String drugLabelName) {
		this.drugLabelName = drugLabelName;
	}

	/**
	 * @return the drugVersion
	 */
	public String getDrugVersion() {
		return drugVersion;
	}

	/**
	 * @param drugVersion
	 *            the drugVersion to set
	 */
	public void setDrugVersion(String drugVersion) {
		this.drugVersion = drugVersion;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipClaimDetails)) {
			return false;
		}
		MembershipClaimDetails other = (MembershipClaimDetails) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipClaimDetails[ id=" + id + " ]";
	}

}
