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
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToOne;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pfchoice.springboot.util.JsonDateDeserializer;
import com.pfchoice.springboot.util.JsonDateSerializer;

/**
 *
 * @author sarath
 */
@Entity(name = "membership_hedis_measure")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "hedis_report", procedureName = "HEDIS_REPORT", parameters = {
				@StoredProcedureParameter(name = "reportMonth", type = Integer.class),
				@StoredProcedureParameter(name = "insId", type = Integer.class),
				@StoredProcedureParameter(name = "prvdrId", type = Integer.class),
				@StoredProcedureParameter(name = "hedisRuleList", type = String.class),
				@StoredProcedureParameter(name = "roster", type = String.class),
				@StoredProcedureParameter(name = "cap", type = String.class),
				@StoredProcedureParameter(name = "sstatus", type = String.class),
				@StoredProcedureParameter(name = "sSearch", type = String.class),
				@StoredProcedureParameter(name = "startDate", type = String.class),
				@StoredProcedureParameter(name = "endDate", type = String.class),
				@StoredProcedureParameter(name = "pageSize", type = Integer.class),
				@StoredProcedureParameter(name = "pageNo", type = Integer.class), }) })
public class MembershipHedisMeasure extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_hedis_msr_Id", nullable = false)
	private Integer id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mbr_id", nullable = false, referencedColumnName = "mbr_id")
	@Where(clause = "active_ind ='Y'")
	private Membership mbr;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hedis_msr_rule_id", nullable = false, referencedColumnName = "hedis_msr_rule_id")
	@Where(clause = "active_ind ='Y'")
	private HedisMeasureRule hedisMeasureRule;

	@Temporal(TemporalType.DATE)
	@Column(name = "due_date")
	private Date dueDate;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "date_of_service")
	private Date dos;

	@Column(name = "follow_up_ind")
	private Character followUpInd;

	/**
	 * 
	 */
	public MembershipHedisMeasure() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipHedisMeasure(final Integer id) {
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
	public void setMbr(final Membership mbr) {
		this.mbr = mbr;
	}

	/**
	 * @return the hedisMeasureRule
	 */
	public HedisMeasureRule getHedisMeasureRule() {
		return hedisMeasureRule;
	}

	/**
	 * @param hedisMeasureRule
	 *            the hedisMeasureRule to set
	 */
	public void setHedisMeasureRule(final HedisMeasureRule hedisMeasureRule) {
		this.hedisMeasureRule = hedisMeasureRule;
	}

	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the dos
	 */
	public Date getDos() {
		return dos;
	}

	/**
	 * @param dos
	 *            the dos to set
	 */
	public void setDos(final Date dos) {
		this.dos = dos;
	}

	/**
	 * @return the followUpInd
	 */
	public Character getFollowUpInd() {
		return followUpInd;
	}

	/**
	 * @param followUpInd
	 *            the followUpInd to set
	 */
	public void setFollowUpInd(final Character followUpInd) {
		this.followUpInd = followUpInd;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipHedisMeasure)) {
			return false;
		}
		MembershipHedisMeasure other = (MembershipHedisMeasure) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MemberhsipHedisMeasure[ id=" + id + " ]";
	}

}
