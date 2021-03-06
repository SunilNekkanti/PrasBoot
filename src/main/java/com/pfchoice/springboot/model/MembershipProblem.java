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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pfchoice.springboot.util.JsonDateDeserializer;
import com.pfchoice.springboot.util.JsonDateSerializer;

/**
 *
 * @author SarathGandluri
 */
@Entity
@Table(name = "membership_problems")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipProblem extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_pbm_id", nullable = false)
	private Integer id;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", nullable = false, referencedColumnName = "mbr_id")
	@Where(clause = "active_ind ='Y'")
	private Membership mbr;

	@OneToOne(fetch = FetchType.LAZY)
	@NotNull(message = "Select ICD")
	@JoinColumn(name = "icd_id", nullable = false, referencedColumnName = "icd_id")
	@Where(clause = "active_ind ='Y'")
	private ICDMeasure icdMeasure;

	@Column(name = "start_date", nullable = false)
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;

	@Column(name = "resolved_date", nullable = true)
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date resolvedDate;

	@Column(name = "file_id")
	private Integer fileId;

	/**
	 * 
	 */
	public MembershipProblem() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipProblem(final Integer id) {
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
	 * @return the icdMeasure
	 */
	public ICDMeasure getIcdMeasure() {
		return icdMeasure;
	}

	/**
	 * @param icdMeasure the icdMeasure to set
	 */
	public void setIcdMeasure(ICDMeasure icdMeasure) {
		this.icdMeasure = icdMeasure;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the resolvedDate
	 */
	public Date getResolvedDate() {
		return resolvedDate;
	}

	/**
	 * @param resolvedDate
	 *            the resolvedDate to set
	 */
	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
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
		if (!(object instanceof MembershipProblem)) {
			return false;
		}
		MembershipProblem other = (MembershipProblem) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipProblem[ id=" + id + " ]";
	}

}
