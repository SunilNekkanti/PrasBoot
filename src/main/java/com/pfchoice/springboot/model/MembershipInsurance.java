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

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "membership_insurance")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipInsurance extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_ins_id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ins_id", nullable = false, referencedColumnName = "insurance_id")
	@Where(clause = "active_ind ='Y'")
	private Insurance insId;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mbr_id", nullable = false, referencedColumnName = "mbr_id")
	@Where(clause = "active_ind ='Y'")
	private Membership mbr;

	@Column(name = "New_Medicare_Bene_Medicaid_Flag")
	private char newBenifits;

	@Column(name = "activitydate")
	@Temporal(TemporalType.DATE)
	private Date activityDate;

	@Column(name = "activityMonth")
	private Integer activityMonth;

	@Temporal(TemporalType.DATE)
	@Column(name = "effective_strt_dt")
	private Date effStartDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "effecctive_end_dt")
	private Date effEndDate;

	@Column(name = "product")
	private String product;

	@Column(name = "product_label")
	private String productLabel;

	@Column(name = "planID")
	private String planId;

	@Column(name = "groupp")
	private String group;

	@Column(name = "class")
	private String clazz;

	@Column(name = "SRC_SYS_MBR_NBR")
	private String srcSysMbrNbr;

	@Column(name = "risk_flag")
	private char riskFlag;

	@Column(name = "file_id")
	private Integer fileId;

	/**
	 * 
	 */
	public MembershipInsurance() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipInsurance(final Integer id) {
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
	 * @return the insId
	 */
	public Insurance getInsId() {
		return insId;
	}

	/**
	 * @param insId
	 *            the insId to set
	 */
	public void setInsId(final Insurance insId) {
		this.insId = insId;
	}

	/**
	 * @return the mbrId
	 */
	public Membership getMbr() {
		return mbr;
	}

	/**
	 * @param mbrId
	 *            the mbrId to set
	 */
	public void setMbr(final Membership mbr) {
		this.mbr = mbr;
	}

	/**
	 * @return the newBenifits
	 */
	public char getNewBenifits() {
		return newBenifits;
	}

	/**
	 * @param newBenifits
	 *            the newBenifits to set
	 */
	public void setNewBenifits(final char newBenifits) {
		this.newBenifits = newBenifits;
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
	public void setActivityDate(final Date activityDate) {
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
	public void setActivityMonth(final Integer activityMonth) {
		this.activityMonth = activityMonth;
	}

	/**
	 * @return the effStartDate
	 */
	public Date getEffStartDate() {
		return effStartDate;
	}

	/**
	 * @param effStartDate
	 *            the effStartDate to set
	 */
	public void setEffStartDate(final Date effStartDate) {
		this.effStartDate = effStartDate;
	}

	/**
	 * @return the effEndDate
	 */
	public Date getEffEndDate() {
		return effEndDate;
	}

	/**
	 * @param effEndDate
	 *            the effEndDate to set
	 */
	public void setEffEndDate(final Date effEndDate) {
		this.effEndDate = effEndDate;
	}

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product
	 *            the product to set
	 */
	public void setProduct(final String product) {
		this.product = product;
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
	public void setProductLabel(final String productLabel) {
		this.productLabel = productLabel;
	}

	/**
	 * @return the planId
	 */
	public String getPlanId() {
		return planId;
	}

	/**
	 * @param planId
	 *            the planId to set
	 */
	public void setPlanId(final String planId) {
		this.planId = planId;
	}

	/**
	 * @return
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public Integer getFileId() {
		return fileId;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the srcSysMbrNbr
	 */
	public String getSrcSysMbrNbr() {
		return srcSysMbrNbr;
	}

	/**
	 * @param srcSysMbrNbr
	 *            the srcSysMbrNbr to set
	 */
	public void setSrcSysMbrNbr(final String srcSysMbrNbr) {
		this.srcSysMbrNbr = srcSysMbrNbr;
	}

	/**
	 * @return the riskFlag
	 */
	public char getRiskFlag() {
		return riskFlag;
	}

	/**
	 * @param riskFlag
	 *            the riskFlag to set
	 */
	public void setRiskFlag(final char riskFlag) {
		this.riskFlag = riskFlag;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipInsurance)) {
			return false;
		}
		MembershipInsurance other = (MembershipInsurance) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipInsurance[ id=" + id + " ]";
	}

}
