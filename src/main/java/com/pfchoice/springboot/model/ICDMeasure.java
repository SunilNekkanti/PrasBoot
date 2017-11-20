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
@Table(name = "icd_measure")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ICDMeasure extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "icd_id", nullable = false)
	private Integer id;

	
	@Column(name = "code", nullable = false)
	private String code;

	
	@Column(name = "description")
	private String description;

	
	@Transient
	private String codeAndDescription;

	
	@Column(name = "hcc")
	private String hcc;

	
	@Column(name = "rxhcc")
	private String rxhcc;

	/**
	 * 
	 */
	public ICDMeasure() {
		super();
	}

	/**
	 * @param id
	 */
	public ICDMeasure(final Integer id) {
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

	/**
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(final String code) {
		this.code = code;
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
		return this.code + " (" + this.description + ")";
	}

	/**
	 * @param codeAndDescription
	 *            the codeAndDescription to set
	 */
	public void setCodeAndDescription(String codeAndDescription) {
		this.codeAndDescription = codeAndDescription;
	}

	/**
	 * @return the hcc
	 */
	public String getHcc() {
		return hcc;
	}

	/**
	 * @param hcc
	 *            the hcc to set
	 */
	public void setHcc(String hcc) {
		this.hcc = hcc;
	}

	/**
	 * @return the rxhcc
	 */
	public String getRxhcc() {
		return rxhcc;
	}

	/**
	 * @param rxhcc
	 *            the rxhcc to set
	 */
	public void setRxhcc(String rxhcc) {
		this.rxhcc = rxhcc;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ICDMeasure)) {
			return false;
		}
		ICDMeasure other = (ICDMeasure) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.ICDMeasure[ id=" + id + " ]";
	}

}
