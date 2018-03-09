package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity(name = "membership_raf_scores")
/*@FilterDef(
	    name = "reportMonthFilter", 
	    parameters = @ParamDef(name = "reportMonth", type = "int")
	)
	@Filter(
	    name = "reportMonthFilter", 
	    condition = "reportMonth  = :reportMonth"
	)*/
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipRafScore extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_raf_score_id", nullable = false)
	private Integer id;


	@Column(name = "report_month")
	private Integer reportMonth;
	
	@Column(name = "raf_period")
	private String rafPeriod;
	
	@Column(name = "activity_months")
	private String activityMonths;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id")
	private Membership mbr;

	@Column(name = "raf_score")
	private BigDecimal rafScore;
	
	@Column(name = "file_id")
	private Integer fileId;
	
	
	/**
	 * 
	 */
	public MembershipRafScore() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipRafScore(final Integer id) {
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
	 * @return the rafPeriod
	 */
	public String getRafPeriod() {
		return rafPeriod;
	}

	/**
	 * @param rafPeriod the rafPeriod to set
	 */
	public void setRafPeriod(String rafPeriod) {
		this.rafPeriod = rafPeriod;
	}

	/**
	 * @return the rafScore
	 */
	public BigDecimal getRafScore() {
		return rafScore;
	}

	/**
	 * @param rafScore the rafScore to set
	 */
	public void setRafScore(BigDecimal rafScore) {
		this.rafScore = rafScore;
	}

	/**
	 * @return the report_month
	 */
	public Integer getReportMonth() {
		return reportMonth;
	}

	/**
	 * @param report_month the report_month to set
	 */
	public void setReportMonth(Integer reportMonth) {
		this.reportMonth = reportMonth;
	}
	
	/**
	 * @return the activityMonths
	 */
	public String getActivityMonths() {
		return activityMonths;
	}

	/**
	 * @param activityMonths the activityMonths to set
	 */
	public void setActivityMonths(String activityMonths) {
		this.activityMonths = activityMonths;
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
		if (!(object instanceof MembershipRafScore)) {
			return false;
		}
		MembershipRafScore other = (MembershipRafScore) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipRafScore[ id=" + id + " ]";
	}

}
