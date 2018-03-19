package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "simply_member_level_summary")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipLevelSummary extends RecordDetails implements Serializable, FieldHandled {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_level_id", nullable = false)
	private Integer id;


	@Column(name = "report_month")
	private Integer reportMonth;
	
	@Column(name = "activityMonth")
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
	
	@Column(nullable = true, name = "premium")
	private Double premium;
	
	//@Column(nullable = true, name = "Funding")
	@Formula("premium * 0.82")
	private Double funding;

	
	@Column(nullable = true, name = "INST")
	private Double inst;
	
	@Column(nullable = true, name = "PROF")
	private Double prof;
	
	@Column(nullable = true, name = "PHARM")
	private Double pharm;
	
	@Column(nullable = true, name = "OTC")
	private Double otc;
	
	
	@Column(nullable = true, name = "PCPCap")
	private Double pcpCap;
	
	
	@Column(nullable = true, name = "speccap")
	private Double speccap;
	
	@Column(nullable = true, name = "ibnr")
	private Double ibnr;
	
	@Column(nullable = true, name = "MSOAdmin")
	private Double msoAdmin;
	
	@Column(nullable = true, name = "Surpluspct")
	private Double surpluspct;
	
	@Column(nullable = true, name = "Deficitpct")
	private Double deficitpct;
	
	@Column(nullable = true, name = "Contribution")
	private Double contribution;
	
	@Column(nullable = true, name = "StoplossRate")
	private Double stoplossRate;
	
	@Column(nullable = true, name = "StoplossCredit")
	private Double stoplossCredit;
	
	@Column(nullable = true, name = "Adjustments")
	private Double adjustments;
	
	@Column(nullable = true, name = "Reserve")
	private Double reserve;

	@Column(name = "CountyCode")
	private String countyCode;

	@Column(name = "RiskEffDate")
	@Temporal(TemporalType.DATE)
	private Date riskEffDate;
	
//	@Column(name = "exp")
	@Formula("(inst +  prof + pharm + otc + pcpCap + speccap +ibnr + stoplossRate + adjustments - stoplossCredit)")
	private Double exp;
	
	//@Column(name = "mlr")
	@Formula("(inst +  prof + pharm + otc + pcpCap + speccap +ibnr + stoplossRate + adjustments - stoplossCredit)*0.82/funding*100")
	private Double mlr;
	
	@Transient
	private Double avgmlr;
	
	
	@JsonIgnore
	private FieldHandler fieldHandler;

	/**
	 * 
	 */
	public MembershipLevelSummary() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipLevelSummary(final Integer id) {
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
	 * @return the countyCode
	 */
	public String getCountyCode() {
		return countyCode;
	}

	/**
	 * @param countyCode
	 *            the countyCode to set
	 */
	public void setCountyCode(final String countyCode) {
		this.countyCode = countyCode;
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
	 * @return the premium
	 */
	public Double getPremium() {
		return premium;
	}

	/**
	 * @param premium the premium to set
	 */
	public void setPremium(Double premium) {
		this.premium = premium;
	}

	/**
	 * @return the funding
	 */
	public Double getFunding() {
		return funding;
	}

	/**
	 * @param funding the funding to set
	 */
	public void setFunding(Double funding) {
		this.funding = funding;
	}

	/**
	 * @return the inst
	 */
	public Double getInst() {
		return inst;
	}

	/**
	 * @param inst the inst to set
	 */
	public void setInst(Double inst) {
		this.inst = inst;
	}

	/**
	 * @return the prof
	 */
	public Double getProf() {
		return prof;
	}

	/**
	 * @param prof the prof to set
	 */
	public void setProf(Double prof) {
		this.prof = prof;
	}

	/**
	 * @return the phar
	 */
	public Double getPharm() {
		return pharm;
	}

	/**
	 * @param phar the phar to set
	 */
	public void setPhar(Double pharm) {
		this.pharm = pharm;
	}

	/**
	 * @return the otc
	 */
	public Double getOtc() {
		return otc;
	}

	/**
	 * @param otc the otc to set
	 */
	public void setOtc(Double otc) {
		this.otc = otc;
	}

	/**
	 * @return the pcpCap
	 */
	public Double getPcpCap() {
		return pcpCap;
	}

	/**
	 * @param pcpCap the pcpCap to set
	 */
	public void setPcpCap(Double pcpCap) {
		this.pcpCap = pcpCap;
	}

	/**
	 * @return the speccap
	 */
	public Double getSpeccap() {
		return speccap;
	}

	/**
	 * @param speccap the speccap to set
	 */
	public void setSpeccap(Double speccap) {
		this.speccap = speccap;
	}

	/**
	 * @return the ibnr
	 */
	public Double getIbnr() {
		return ibnr;
	}

	/**
	 * @param ibnr the ibnr to set
	 */
	public void setIbnr(Double ibnr) {
		this.ibnr = ibnr;
	}

	/**
	 * @return the msoAdmin
	 */
	public Double getMsoAdmin() {
		return msoAdmin;
	}

	/**
	 * @param msoAdmin the msoAdmin to set
	 */
	public void setMsoAdmin(Double msoAdmin) {
		this.msoAdmin = msoAdmin;
	}

	/**
	 * @return the surpluspct
	 */
	public Double getSurpluspct() {
		return surpluspct;
	}

	/**
	 * @param surpluspct the surpluspct to set
	 */
	public void setSurpluspct(Double surpluspct) {
		this.surpluspct = surpluspct;
	}

	/**
	 * @return the deficitpct
	 */
	public Double getDeficitpct() {
		return deficitpct;
	}

	/**
	 * @param deficitpct the deficitpct to set
	 */
	public void setDeficitpct(Double deficitpct) {
		this.deficitpct = deficitpct;
	}

	/**
	 * @return the contribution
	 */
	public Double getContribution() {
		return contribution;
	}

	/**
	 * @param contribution the contribution to set
	 */
	public void setContribution(Double contribution) {
		this.contribution = contribution;
	}

	/**
	 * @return the stoplossRate
	 */
	public Double getStoplossRate() {
		return stoplossRate;
	}

	/**
	 * @param stoplossRate the stoplossRate to set
	 */
	public void setStoplossRate(Double stoplossRate) {
		this.stoplossRate = stoplossRate;
	}

	/**
	 * @return the stoplossCredit
	 */
	public Double getStoplossCredit() {
		return stoplossCredit;
	}

	/**
	 * @param stoplossCredit the stoplossCredit to set
	 */
	public void setStoplossCredit(Double stoplossCredit) {
		this.stoplossCredit = stoplossCredit;
	}

	/**
	 * @return the adjustments
	 */
	public Double getAdjustments() {
		return adjustments;
	}

	/**
	 * @param adjustments the adjustments to set
	 */
	public void setAdjustments(Double adjustments) {
		this.adjustments = adjustments;
	}

	/**
	 * @return the reserve
	 */
	public Double getReserve() {
		return reserve;
	}

	/**
	 * @param reserve the reserve to set
	 */
	public void setReserve(Double reserve) {
		this.reserve = reserve;
	}

	/**
	 * @return the riskEffDate
	 */
	public Date getRiskEffDate() {
		return riskEffDate;
	}

	/**
	 * @param riskEffDate the riskEffDate to set
	 */
	public void setRiskEffDate(Date riskEffDate) {
		this.riskEffDate = riskEffDate;
	}

	/**
	 * @return the activityMonth
	 */
	public Integer getActivityMonth() {
		return activityMonth;
	}

	/**
	 * @param activityMonth the activityMonth to set
	 */
	public void setActivityMonth(Integer activityMonth) {
		this.activityMonth = activityMonth;
	}

	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}

	/**
	 * @return the exp
	 */
	public Double getExp() {
		return exp;
	}

	/**
	 * @param exp the exp to set
	 */
	public void setExp(Double exp) {
		this.exp = exp;
	}

	/**
	 * @return the mlr
	 */
	public Double getMlr() {
		return mlr;
	}

	/**
	 * @param mlr the mlr to set
	 */
	public void setMlr(Double mlr) {
		this.mlr = mlr;
	}

	/**
	 * @return the avgmlr
	 */
	public Double getAvgmlr() {
		return avgmlr;
	}

	/**
	 * @param avgmlr the avgmlr to set
	 */
	public void setAvgmlr(Double avgmlr) {
		this.avgmlr = avgmlr;
	}

	/**
	 * @param pharm the pharm to set
	 */
	public void setPharm(Double pharm) {
		this.pharm = pharm;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipLevelSummary)) {
			return false;
		}
		MembershipLevelSummary other = (MembershipLevelSummary) object;
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
