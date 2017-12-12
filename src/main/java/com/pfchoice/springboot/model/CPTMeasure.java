package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "cpt_measure")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class CPTMeasure extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "cpt_id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "code", nullable = false)
	private String code;

	@Column(name = "short_description")
	private String shortDescription;

	@Transient
	private String codeAndDescription;

	@Column(name = "description")
	private String description;

	/**
	 * Default constructor there exists one constructor with id as parameter
	 */
	public CPTMeasure() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public CPTMeasure(final Integer id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription
	 *            the shortDescription to set
	 */
	public void setShortDescription(final String shortDescription) {
		this.shortDescription = shortDescription;
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
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the codeAndDescription
	 */
	public String getCodeAndDescription() {
		return this.code + " (" + this.shortDescription + ")";
	}

	/**
	 * @param codeAndDescription
	 *            the codeAndDescription to set
	 */
	public void setCodeAndDescription(String codeAndDescription) {
		this.codeAndDescription = codeAndDescription;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CPTMeasure)) {
			return false;
		}
		CPTMeasure other = (CPTMeasure) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.CPTMeasure[ id=" + id + " ]";
	}

}
