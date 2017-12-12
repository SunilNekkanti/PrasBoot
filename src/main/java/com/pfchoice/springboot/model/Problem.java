package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author SarathGandluri
 */
@Entity
@Table(name = "problems")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Problem extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "pbm_id", nullable = false)
	private Integer id;

	@Column(name = "description")
	private String description;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ins_id", referencedColumnName = "Insurance_Id")
	@Where(clause = "active_ind ='Y'")
	private Insurance insId;

	@Column(name = "effective_year")
	private Integer effectiveYear;

	@Fetch(FetchMode.SELECT)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "problems_icd", joinColumns = {
			@JoinColumn(name = "pbm_Id", referencedColumnName = "pbm_Id") }, inverseJoinColumns = {
					@JoinColumn(name = "icd_id", referencedColumnName = "icd_id") })
	@Where(clause = "active_ind ='Y'")
	private Set<ICDMeasure> icdCodes;

	/**
	 * 
	 */
	public Problem() {
		super();
	}

	/**
	 * @param id
	 */
	public Problem(final Integer id) {
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	public void setInsId(Insurance insId) {
		this.insId = insId;
	}

	/**
	 * @return the effectiveYear
	 */
	public Integer getEffectiveYear() {
		return effectiveYear;
	}

	/**
	 * @param effectiveYear
	 *            the effectiveYear to set
	 */
	public void setEffectiveYear(Integer effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	/**
	 * @return the icdCodes
	 */
	public Set<ICDMeasure> getIcdCodes() {
		return icdCodes;
	}

	/**
	 * @param icdCodes
	 *            the icdCodes to set
	 */
	public void setIcdCodes(Set<ICDMeasure> icdCodes) {
		this.icdCodes = icdCodes;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Problem)) {
			return false;
		}
		Problem other = (Problem) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Problem[ id=" + id + " ]";
	}

}
