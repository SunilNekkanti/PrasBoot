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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "new_medical_loss_ratio")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@FilterDef(
	    name = "reportMonthFilter", 
	    parameters = @ParamDef(name = "reportMonths", type = "text")
	)
	@Filter(
	    name = "reportMonthFilter", 
	    condition = "report_Month  in (:reportMonths)"
	)
public class NewMedicalLossRatio extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "medical_loss_ratio_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ins_id", nullable = false, referencedColumnName = "insurance_id", unique = true)
	private Insurance ins;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prvdr_id", nullable = false, referencedColumnName = "prvdr_id", unique = true)
	private Provider prvdr;

	@Column(nullable = true, name = "report_month")
	private Integer reportMonth;

	@Column(name = "activity_Month")
	private Integer activityMonth;

	@Column(name = "file_id", nullable = false)
	private Integer fileId;
	
	@Column(nullable = true, name = "amg_funding")
	private BigDecimal funding;
	
	@Column(nullable = true, name = "mbr_cnt")
	private BigDecimal mbrCnt;


	@Column(nullable = true, name = "prof_claims")
	private BigDecimal profClaims;

	@Column(nullable = true, name = "inst_claims")
	private BigDecimal instClaims;

	@Column(nullable = true, name = "phar_claims")
	private BigDecimal pharClaims;

	@Column(nullable = true, name = "unwanted_claims")
	private BigDecimal unwantedClaims;

	@Column(nullable = true, name = "sl_credit_claims")
	private BigDecimal slCreditClaims;
	
	@Column(nullable = true, name = "amg_mbr_cnt")
	private BigDecimal amgMbrCnt;


	@Column(nullable = true, name = "amg_prof")
	private BigDecimal amgProf;

	@Column(nullable = true, name = "amg_inst")
	private BigDecimal amgInst;

	@Column(nullable = true, name = "amg_phar")
	private BigDecimal amgPhar;
	
	@Column(nullable = true, name = "amg_ibnr_tot")
	private BigDecimal ibnr;

	@Column(nullable = true, name = "amg_pcp_cap")
	private BigDecimal pcpCap;

	@Column(nullable = true, name = "amg_spec_cap")
	private BigDecimal specCap;

	@Column(nullable = true, name = "amg_sl_exp")
	private BigDecimal amgSLExp;

	@Column(nullable = true, name = "amg_sl_credit")
	private BigDecimal amgSLCredit;
	
	@Column(nullable = true, name = "amg_vab_adj")
	private BigDecimal amgVabAdjust;
	
	@Column(nullable = true, name = "amg_dental_cap")
	private BigDecimal dentalCap;
	
	@Column(nullable = true, name = "amg_trans_cap")
	private BigDecimal transCap;
	
	@Column(nullable = true, name = "amg_vision_cap")
	private BigDecimal visCap;
	
	@Column(nullable = true, name = "amg_ibnr_inst")
	private BigDecimal ibnrInst;

	@Column(nullable = true, name = "amg_ibnr_prof")
	private BigDecimal ibnrProf;
	
	@Column(nullable = true, name = "amg_adj")
	private BigDecimal adjust;

	@Column(nullable = true, name = "amg_totalExp")
	private BigDecimal totalExp;
	
	@Column(nullable = true, name = "totalExp_claims")
	private BigDecimal totalExpClaims;

	@Column(nullable = true, name = "balance")
	private BigDecimal balance;

	@Column(nullable = true, name = "mlr")
	private BigDecimal mlr;

	@Column(nullable = true, name = "qmlr")
	private BigDecimal qmlr;

	@Column(nullable = true, name = "balance_claims")
	private BigDecimal balanceClaims;

	@Column(nullable = true, name = "mlr_claims")
	private BigDecimal mlrClaims;

	@Column(nullable = true, name = "qmlr_claims")
	private BigDecimal qmlrClaims;

	
	/**
	 * 
	 */
	public NewMedicalLossRatio() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public NewMedicalLossRatio(final Integer id) {
		super();
		this.id = id;
	}

	public NewMedicalLossRatio( final Integer reportMonth, 
			final Integer activityMonth, 
			final BigDecimal mbrCnt,
			final BigDecimal profClaims,
			final BigDecimal instClaims, 
			final BigDecimal pharClaims,
			final BigDecimal unwantedClaims,
			final BigDecimal slCreditClaims,
			final BigDecimal amgMbrCnt,
			final BigDecimal funding,
			final BigDecimal amgProf,
			final BigDecimal amgInst, 
			final BigDecimal amgPhar,
			final BigDecimal ibnr, 
			final BigDecimal pcpCap,
			final BigDecimal specCap,
			final BigDecimal dentalCap,
			final BigDecimal transCap, 
			final BigDecimal visCap,
			final BigDecimal stopLossExp,
			final BigDecimal stopLossCredit,
			final BigDecimal amgVabAdjust,
			final BigDecimal adjust,
			final BigDecimal ibnrInst,
			final BigDecimal ibnrProf,
			final BigDecimal totalExp,
			final BigDecimal totalExpClaims,
			final BigDecimal balance, 
			final BigDecimal mlr,
			final BigDecimal qmlr,
			final BigDecimal balanceClaims, 
			final BigDecimal mlrClaims,
			final BigDecimal qmlrClaims) {
		this.reportMonth = reportMonth;
		this.activityMonth = activityMonth;
		this.mbrCnt = mbrCnt;
		this.profClaims = profClaims;
		this.instClaims = instClaims;
		this.pharClaims = pharClaims;
		this.unwantedClaims = unwantedClaims;
		this.slCreditClaims = slCreditClaims;
		this.amgMbrCnt = amgMbrCnt;
		this.funding = funding;
		this.amgProf = amgProf;
		this.amgInst = amgInst;
		this.amgPhar = amgPhar;
		this.ibnr = ibnr;
		this.pcpCap = pcpCap;
		this.specCap = specCap;
		this.dentalCap = dentalCap;
		this.transCap = transCap;
		this.visCap = visCap;
		this.amgSLExp = stopLossExp;
		this.amgSLCredit = stopLossCredit;
		this.amgVabAdjust = amgVabAdjust;
		this.adjust = adjust;
		this.ibnrInst = ibnrInst;
		this.ibnrProf = ibnrProf;
		this.totalExp = totalExp;
		this.totalExpClaims = totalExpClaims;
		this.balance = balance;
		this.mlr = mlr;
		this.qmlr = qmlr;
		this.balanceClaims = balanceClaims;
		this.mlrClaims = mlrClaims;
		this.qmlrClaims = qmlrClaims;
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

	public Insurance getIns() {
		return ins;
	}

	public void setIns(Insurance ins) {
		this.ins = ins;
	}

	public Provider getPrvdr() {
		return prvdr;
	}

	public void setPrvdr(Provider prvdr) {
		this.prvdr = prvdr;
	}

	public Integer getReportMonth() {
		return reportMonth;
	}

	public void setReportMonth(Integer reportMonth) {
		this.reportMonth = reportMonth;
	}

	public Integer getActivityMonth() {
		return activityMonth;
	}

	public void setActivityMonth(Integer activityMonth) {
		this.activityMonth = activityMonth;
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
	 * @return the ibnr
	 */
	public BigDecimal getIbnr() {
		return ibnr;
	}

	/**
	 * @param ibnr
	 *            the ibnr to set
	 */
	public void setIbnr(BigDecimal ibnr) {
		this.ibnr = ibnr;
	}

	/**
	 * @return the pcpCap
	 */
	public BigDecimal getPcpCap() {
		return pcpCap;
	}

	/**
	 * @param pcpCap
	 *            the pcpCap to set
	 */
	public void setPcpCap(BigDecimal pcpCap) {
		this.pcpCap = pcpCap;
	}

	/**
	 * @return the specCap
	 */
	public BigDecimal getSpecCap() {
		return specCap;
	}

	/**
	 * @param specCap
	 *            the specCap to set
	 */
	public void setSpecCap(BigDecimal specCap) {
		this.specCap = specCap;
	}

	/**
	 * @return the adjust
	 */
	public BigDecimal getAdjust() {
		return adjust;
	}

	/**
	 * @param adjust
	 *            the adjust to set
	 */
	public void setAdjust(BigDecimal adjust) {
		this.adjust = adjust;
	}

	/**
	 * @return the totalExp
	 */
	public BigDecimal getTotalExp() {
		return totalExp;
	}

	/**
	 * @param totalExp
	 *            the totalExp to set
	 */
	public void setTotalExp(BigDecimal totalExp) {
		this.totalExp = totalExp;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the mlr
	 */
	public BigDecimal getMlr() {
		return mlr;
	}

	/**
	 * @param mlr
	 *            the mlr to set
	 */
	public void setMlr(BigDecimal mlr) {
		this.mlr = mlr;
	}

	/**
	 * @return the qmlr
	 */
	public BigDecimal getQmlr() {
		return qmlr;
	}

	/**
	 * @param qmlr
	 *            the qmlr to set
	 */
	public void setQmlr(BigDecimal qmlr) {
		this.qmlr = qmlr;
	}

	/**
	 * @return the funding
	 */
	public BigDecimal getFunding() {
		return funding;
	}

	/**
	 * @param funding the funding to set
	 */
	public void setFunding(BigDecimal funding) {
		this.funding = funding;
	}

	/**
	 * @return the amgMbrCnt
	 */
	public BigDecimal getAmgMbrCnt() {
		return amgMbrCnt;
	}

	/**
	 * @param amgMbrCnt the amgMbrCnt to set
	 */
	public void setAmgMbrCnt(BigDecimal amgMbrCnt) {
		this.amgMbrCnt = amgMbrCnt;
	}

	/**
	 * @return the amgProf
	 */
	public BigDecimal getAmgProf() {
		return amgProf;
	}

	/**
	 * @param amgProf the amgProf to set
	 */
	public void setAmgProf(BigDecimal amgProf) {
		this.amgProf = amgProf;
	}

	/**
	 * @return the amgInst
	 */
	public BigDecimal getAmgInst() {
		return amgInst;
	}

	/**
	 * @param amgInst the amgInst to set
	 */
	public void setAmgInst(BigDecimal amgInst) {
		this.amgInst = amgInst;
	}

	/**
	 * @return the amgPhar
	 */
	public BigDecimal getAmgPhar() {
		return amgPhar;
	}

	/**
	 * @param amgPhar the amgPhar to set
	 */
	public void setAmgPhar(BigDecimal amgPhar) {
		this.amgPhar = amgPhar;
	}

	/**
	 * @return the amgSLExp
	 */
	public BigDecimal getAmgSLExp() {
		return amgSLExp;
	}

	/**
	 * @param amgSLExp the amgSLExp to set
	 */
	public void setAmgSLExp(BigDecimal amgSLExp) {
		this.amgSLExp = amgSLExp;
	}

	/**
	 * @return the amgSLCredit
	 */
	public BigDecimal getAmgSLCredit() {
		return amgSLCredit;
	}

	/**
	 * @param amgSLCredit the amgSLCredit to set
	 */
	public void setAmgSLCredit(BigDecimal amgSLCredit) {
		this.amgSLCredit = amgSLCredit;
	}

	/**
	 * @return the amgVabAdjust
	 */
	public BigDecimal getAmgVabAdjust() {
		return amgVabAdjust;
	}

	/**
	 * @param amgVabAdjust the amgVabAdjust to set
	 */
	public void setAmgVabAdjust(BigDecimal amgVabAdjust) {
		this.amgVabAdjust = amgVabAdjust;
	}

	/**
	 * @return the dentalCap
	 */
	public BigDecimal getDentalCap() {
		return dentalCap;
	}

	/**
	 * @param dentalCap the dentalCap to set
	 */
	public void setDentalCap(BigDecimal dentalCap) {
		this.dentalCap = dentalCap;
	}
	
	

	/**
	 * @return the mbrCnt
	 */
	public BigDecimal getMbrCnt() {
		return mbrCnt;
	}

	/**
	 * @param mbrCnt the mbrCnt to set
	 */
	public void setMbrCnt(BigDecimal mbrCnt) {
		this.mbrCnt = mbrCnt;
	}

	/**
	 * @return the profClaims
	 */
	public BigDecimal getProfClaims() {
		return profClaims;
	}

	/**
	 * @param profClaims the profClaims to set
	 */
	public void setProfClaims(BigDecimal profClaims) {
		this.profClaims = profClaims;
	}

	/**
	 * @return the instClaims
	 */
	public BigDecimal getInstClaims() {
		return instClaims;
	}

	/**
	 * @param instClaims the instClaims to set
	 */
	public void setInstClaims(BigDecimal instClaims) {
		this.instClaims = instClaims;
	}

	/**
	 * @return the pharClaims
	 */
	public BigDecimal getPharClaims() {
		return pharClaims;
	}

	/**
	 * @param pharClaims the pharClaims to set
	 */
	public void setPharClaims(BigDecimal pharClaims) {
		this.pharClaims = pharClaims;
	}

	/**
	 * @return the unwantedClaims
	 */
	public BigDecimal getUnwantedClaims() {
		return unwantedClaims;
	}

	/**
	 * @param unwantedClaims the unwantedClaims to set
	 */
	public void setUnwantedClaims(BigDecimal unwantedClaims) {
		this.unwantedClaims = unwantedClaims;
	}

	/**
	 * @return the slCreditClaims
	 */
	public BigDecimal getSlCreditClaims() {
		return slCreditClaims;
	}

	/**
	 * @param slCreditClaims the slCreditClaims to set
	 */
	public void setSlCreditClaims(BigDecimal slCreditClaims) {
		this.slCreditClaims = slCreditClaims;
	}

	/**
	 * @return the balanceClaims
	 */
	public BigDecimal getBalanceClaims() {
		return balanceClaims;
	}

	/**
	 * @param balanceClaims the balanceClaims to set
	 */
	public void setBalanceClaims(BigDecimal balanceClaims) {
		this.balanceClaims = balanceClaims;
	}

	/**
	 * @return the mlrClaims
	 */
	public BigDecimal getMlrClaims() {
		return mlrClaims;
	}

	/**
	 * @param mlrClaims the mlrClaims to set
	 */
	public void setMlrClaims(BigDecimal mlrClaims) {
		this.mlrClaims = mlrClaims;
	}

	/**
	 * @return the qmlrClaims
	 */
	public BigDecimal getQmlrClaims() {
		return qmlrClaims;
	}

	/**
	 * @param qmlrClaims the qmlrClaims to set
	 */
	public void setQmlrClaims(BigDecimal qmlrClaims) {
		this.qmlrClaims = qmlrClaims;
	}

	/**
	 * @return the transCap
	 */
	public BigDecimal getTransCap() {
		return transCap;
	}

	/**
	 * @param transCap the transCap to set
	 */
	public void setTransCap(BigDecimal transCap) {
		this.transCap = transCap;
	}

	/**
	 * @return the visCap
	 */
	public BigDecimal getVisCap() {
		return visCap;
	}

	/**
	 * @param visCap the visCap to set
	 */
	public void setVisCap(BigDecimal visCap) {
		this.visCap = visCap;
	}

	/**
	 * @return the ibnrInst
	 */
	public BigDecimal getIbnrInst() {
		return ibnrInst;
	}

	/**
	 * @param ibnrInst the ibnrInst to set
	 */
	public void setIbnrInst(BigDecimal ibnrInst) {
		this.ibnrInst = ibnrInst;
	}

	/**
	 * @return the ibnrProf
	 */
	public BigDecimal getIbnrProf() {
		return ibnrProf;
	}

	/**
	 * @param ibnrProf the ibnrProf to set
	 */
	public void setIbnrProf(BigDecimal ibnrProf) {
		this.ibnrProf = ibnrProf;
	}

	/**
	 * @return the totalExpClaims
	 */
	public BigDecimal getTotalExpClaims() {
		return totalExpClaims;
	}

	/**
	 * @param totalExpClaims the totalExpClaims to set
	 */
	public void setTotalExpClaims(BigDecimal totalExpClaims) {
		this.totalExpClaims = totalExpClaims;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof NewMedicalLossRatio)) {
			return false;
		}
		NewMedicalLossRatio other = (NewMedicalLossRatio) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.NewMedicalLossRatio[ id=" + id + " ]";
	}

}
