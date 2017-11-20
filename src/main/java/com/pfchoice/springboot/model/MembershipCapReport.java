package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 *
 * @author sarath
 */
@Entity
@Table(name = "membership_cap_report")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipCapReport extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_cap_rpt_id", nullable = false)
	private Integer id;

	
	@Column(name = "cap_pay_dt")
	@Temporal(TemporalType.DATE)
	private Date capPayDate;

	
	@Column(name = "cap_period", nullable = false)
	private Integer capPeriod;
	
	
	@Column(name = "payee_id", nullable = false)
	private String payeeId;
	
	
	@Column(name = "payee_name", nullable = false)
	private String payeeName;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ins_id", nullable = false, referencedColumnName = "insurance_id")
	private Insurance insId;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "prvdr_id", nullable = false, referencedColumnName = "prvdr_id")
	private Provider prvdr;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mbr_id", nullable = false, referencedColumnName = "mbr_id")
	private Membership mbr;
	
	
	@Column(name = "eff_start_date")
	@Temporal(TemporalType.DATE)
	private Date effStartDate; 
	
	
	@Column(name = "mbr_risk_pop", nullable = false)
	private String mbrRiskPop;
	
	
	@Column(name = "cap_key", nullable = false)
	private String capKey;
	
	
	@Column(name = "rate_tier", nullable = false)
	private String rateTier;
	
	
	@Column(name = "rec_type", nullable = false)
	private String recType;
	
	
	@Column(name = "fund_rate", nullable = false)
	private String fundRate;
	
	
	@Column(name = "fund_amt", nullable = false)
	private String fundAmt;
	
	
	@Column(name = "mm", nullable = false)
	private String mm;
	
	
	@Column(name = "fund_name", nullable = false)
	private String fundName;
	
	
	@Column(name = "lobd_id", nullable = false)
	private String lobdId;
	
	
	@Column(name = "product_desc", nullable = false)
	private String productDesc;
 
	
	@Column(name = "file_id", nullable = false)
	private Integer fileId;

	
	/**
	 * 
	 */
	public MembershipCapReport() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public MembershipCapReport(final Integer id) {
		super();
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	public Date getCapPayDate() {
		return capPayDate;
	}

	public void setCapPayDate(Date capPayDate) {
		this.capPayDate = capPayDate;
	}

	public Integer getCapPeriod() {
		return capPeriod;
	}

	public void setCapPeriod(Integer capPeriod) {
		this.capPeriod = capPeriod;
	}

	public String getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public Insurance getInsId() {
		return insId;
	}

	public void setInsId(Insurance insId) {
		this.insId = insId;
	}

	public Provider getPrvdr() {
		return prvdr;
	}

	public void setPrvdr(Provider prvdr) {
		this.prvdr = prvdr;
	}

	public Membership getMbr() {
		return mbr;
	}

	public void setMbr(Membership mbr) {
		this.mbr = mbr;
	}

	public Date getEffStartDate() {
		return effStartDate;
	}

	public void setEffStartDate(Date effStartDate) {
		this.effStartDate = effStartDate;
	}

	public String getMbrRiskPop() {
		return mbrRiskPop;
	}

	public void setMbrRiskPop(String mbrRiskPop) {
		this.mbrRiskPop = mbrRiskPop;
	}

	public String getCapKey() {
		return capKey;
	}

	public void setCapKey(String capKey) {
		this.capKey = capKey;
	}

	public String getRateTier() {
		return rateTier;
	}

	public void setRateTier(String rateTier) {
		this.rateTier = rateTier;
	}

	public String getRecType() {
		return recType;
	}

	public void setRecType(String recType) {
		this.recType = recType;
	}

	public String getFundRate() {
		return fundRate;
	}

	public void setFundRate(String fundRate) {
		this.fundRate = fundRate;
	}

	public String getFundAmt() {
		return fundAmt;
	}

	public void setFundAmt(String fundAmt) {
		this.fundAmt = fundAmt;
	}

	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getLobdId() {
		return lobdId;
	}

	public void setLobdId(String lobdId) {
		this.lobdId = lobdId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipCapReport)) {
			return false;
		}
		MembershipCapReport other = (MembershipCapReport) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipCapReport[ id=" + id + " ]";
	}

}
