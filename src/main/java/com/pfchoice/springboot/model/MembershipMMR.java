package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.ParamDef;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "simply_mmr")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@FilterDef(name="reportMonthFilter", defaultCondition="FIND_IN_SET(report_Month,:reportMonths)" , parameters = { @ParamDef(name = "reportMonths", type = "text") })
public class MembershipMMR extends RecordDetails implements Serializable, FieldHandled {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "simply_mmr_id", nullable = false)
	private Integer id;


	@Column(name = "report_month")
	private Integer reportMonth;
	
	@Formula("date_format(Activity_Date,'%y%m')")
	private Integer activityMonth;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ins_id", referencedColumnName = "insurance_id")
	private Insurance ins;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id")
	private Provider prvdr;
	
    @JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id")
	private Membership mbr;
	
    @Column(nullable = true, name = "Risk_Adjuster_Factor_A")
	private Double riskAdjusterFactorA;

    @Column(nullable = true, name = "Risk_Adjuster_Factor_B")
	private Double riskAdjusterFactorB;
    
	@JsonIgnore
	private FieldHandler fieldHandler;

	/**
	 * 
	 */
	public MembershipMMR() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipMMR(final Integer id) {
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
	 * @return the reportMonth
	 */
	public Integer getReportMonth() {
		return reportMonth;
	}

	/**
	 * @param reportMonth the reportMonth to set
	 */
	public void setReportMonth(Integer reportMonth) {
		this.reportMonth = reportMonth;
	}

	/**
	 * @return the ins
	 */
	public Insurance getIns() {
		return ins;
	}

	/**
	 * @param ins the ins to set
	 */
	public void setIns(Insurance ins) {
		this.ins = ins;
	}

	/**
	 * @return the prvdr
	 */
	public Provider getPrvdr() {
		return prvdr;
	}

	/**
	 * @param prvdr the prvdr to set
	 */
	public void setPrvdr(Provider prvdr) {
		this.prvdr = prvdr;
	}

	/**
	 * @return the mbr
	 */
	public Membership getMbr() {
		return mbr;
	}

	/**
	 * @param mbr the mbr to set
	 */
	public void setMbr(Membership mbr) {
		this.mbr = mbr;
	}

	/**
	 * @return the activityMonth
	 */
	public Integer getActivityMonth() {
		return activityMonth;
	}

	/**
	 * @return the riskAdjusterFactorA
	 */
	public Double getRiskAdjusterFactorA() {
		return riskAdjusterFactorA;
	}

	/**
	 * @param riskAdjusterFactorA the riskAdjusterFactorA to set
	 */
	public void setRiskAdjusterFactorA(Double riskAdjusterFactorA) {
		this.riskAdjusterFactorA = riskAdjusterFactorA;
	}

	/**
	 * @return the riskAdjusterFactorB
	 */
	public Double getRiskAdjusterFactorB() {
		return riskAdjusterFactorB;
	}

	/**
	 * @param riskAdjusterFactorB the riskAdjusterFactorB to set
	 */
	public void setRiskAdjusterFactorB(Double riskAdjusterFactorB) {
		this.riskAdjusterFactorB = riskAdjusterFactorB;
	}

	/**
	 * @param activityMonth the activityMonth to set
	 */
	public void setActivityMonth(Integer activityMonth) {
		this.activityMonth = activityMonth;
	}

	/**
	 * @return the fieldHandler
	 */
	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}

	/**
	 * @param fieldHandler the fieldHandler to set
	 */
	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipMMR)) {
			return false;
		}
		MembershipMMR other = (MembershipMMR) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Membership[ id=" + id + "]";
	}

}
