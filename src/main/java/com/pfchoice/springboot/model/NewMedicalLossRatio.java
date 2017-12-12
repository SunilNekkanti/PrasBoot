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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "new_medical_loss_ratio")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class NewMedicalLossRatio extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "medical_loss_ratio_id", nullable = false)
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

	@Column(name = "patients")
	private BigDecimal patients;

	@Column(nullable = true, name = "fund")
	private BigDecimal fund;

	@Column(nullable = true, name = "prof")
	private BigDecimal prof;

	@Column(nullable = true, name = "inst")
	private BigDecimal inst;

	@Column(nullable = true, name = "pharmacy")
	private BigDecimal pharmacy;

	@Column(name = "file_id", nullable = false)
	private Integer fileId;

	@Column(nullable = true, name = "ibnr")
	private BigDecimal ibnr;

	@Column(nullable = true, name = "pcpcap")
	private BigDecimal pcpCap;

	@Column(nullable = true, name = "speccap")
	private BigDecimal specCap;

	@Column(nullable = true, name = "stoplossExp")
	private BigDecimal stopLossExp;

	@Column(nullable = true, name = "stoplossCredit")
	private BigDecimal stopLossCredit;

	@Column(nullable = true, name = "Adjust")
	private BigDecimal adjust;

	@Column(nullable = true, name = "TotalExp")
	private BigDecimal totalExp;

	@Column(nullable = true, name = "balance")
	private BigDecimal balance;

	@Column(nullable = true, name = "unwanted_claims")
	private BigDecimal unwantedClaims;

	@Column(nullable = true, name = "stop_loss")
	private BigDecimal stopLoss;

	@Column(nullable = true, name = "mlr")
	private BigDecimal mlr;

	@Column(nullable = true, name = "qmlr")
	private BigDecimal qmlr;

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

	public NewMedicalLossRatio(final Integer reportMonth, final Integer activityMonth, final BigDecimal patients,
			final BigDecimal fund, final BigDecimal prof, final BigDecimal inst, final BigDecimal pharmacy,
			final BigDecimal ibnr, final BigDecimal pcpCap, final BigDecimal specCap, final BigDecimal stopLossExp,
			final BigDecimal stopLossCredit, final BigDecimal adjust, final BigDecimal totalExp,
			final BigDecimal balance, final BigDecimal unwantedClaims, final BigDecimal stopLoss, final BigDecimal mlr,
			final BigDecimal qmlr) {
		super();
		this.reportMonth = reportMonth;
		this.activityMonth = activityMonth;
		this.patients = patients;
		this.fund = fund;
		this.prof = prof;
		this.inst = inst;
		this.pharmacy = pharmacy;
		this.ibnr = ibnr;
		this.pcpCap = pcpCap;
		this.specCap = specCap;
		this.stopLossExp = stopLossExp;
		this.stopLossCredit = stopLossCredit;
		this.adjust = adjust;
		this.totalExp = totalExp;
		this.balance = balance;
		this.unwantedClaims = unwantedClaims;
		this.stopLoss = stopLoss;
		this.mlr = mlr;
		this.qmlr = qmlr;
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

	public BigDecimal getPatients() {
		return patients;
	}

	public void setPatients(BigDecimal patients) {
		this.patients = patients;
	}

	public BigDecimal getFund() {
		return fund;
	}

	public void setFund(BigDecimal fund) {
		this.fund = fund;
	}

	public BigDecimal getProf() {
		return prof;
	}

	public void setProf(BigDecimal prof) {
		this.prof = prof;
	}

	public BigDecimal getInst() {
		return inst;
	}

	public void setInst(BigDecimal inst) {
		this.inst = inst;
	}

	public BigDecimal getPharmacy() {
		return pharmacy;
	}

	public void setPharmacy(BigDecimal pharmacy) {
		this.pharmacy = pharmacy;
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
	 * @return the stopLossExp
	 */
	public BigDecimal getStopLossExp() {
		return stopLossExp;
	}

	/**
	 * @param stopLossExp
	 *            the stopLossExp to set
	 */
	public void setStopLossExp(BigDecimal stopLossExp) {
		this.stopLossExp = stopLossExp;
	}

	/**
	 * @return the stopLossCredit
	 */
	public BigDecimal getStopLossCredit() {
		return stopLossCredit;
	}

	/**
	 * @param stopLossCredit
	 *            the stopLossCredit to set
	 */
	public void setStopLossCredit(BigDecimal stopLossCredit) {
		this.stopLossCredit = stopLossCredit;
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
	 * @return the unwantedClaims
	 */
	public BigDecimal getUnwantedClaims() {
		return unwantedClaims;
	}

	/**
	 * @param unwantedClaims
	 *            the unwantedClaims to set
	 */
	public void setUnwantedClaims(BigDecimal unwantedClaims) {
		this.unwantedClaims = unwantedClaims;
	}

	/**
	 * @return the stopLoss
	 */
	public BigDecimal getStopLoss() {
		return stopLoss;
	}

	/**
	 * @param stopLoss
	 *            the stopLoss to set
	 */
	public void setStopLoss(BigDecimal stopLoss) {
		this.stopLoss = stopLoss;
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
