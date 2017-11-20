package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "lu_county")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class County extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "code", nullable = false)
	private Integer code;

	@Column(name = "description")
	private String description;

	@Column(name = "short_name")
	private String shortName;

	/**
	 * 
	 */
	public County() {
		super();
	}

	/**
	 * 
	 * @param code
	 */
	public County(final Integer code) {
		super();
		this.code = code;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * 
	 * @param code
	 */
	public void setCode(final Integer code) {
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
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (code != null ? code.hashCode() : 0);
		return hash;
	}

	/**
	 * @param object
	 *            the object to compare
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof County)) {
			return false;
		}
		County other = (County) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.County[ code=" + code + " ]";
	}

}
