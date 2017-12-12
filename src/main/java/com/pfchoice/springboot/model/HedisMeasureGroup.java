package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "hedis_measure_group")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class HedisMeasureGroup extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "qlty_msr_group_id", nullable = false)
	private Integer id;

	@NotNull
	@Size(min = 2, max = 100, message = "The code must be between {min} and {max} characters long")
	@Column(name = "code", nullable = false)
	private String code;

	@NotNull
	@Size(min = 5, max = 200, message = "The description must be between {min} and {max} characters long")
	@Column(name = "description")
	private String description;

	/**
	 * 
	 */
	public HedisMeasureGroup() {
		super();
	}

	/**
	 * @param id
	 */
	public HedisMeasureGroup(final Integer id) {
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

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof HedisMeasureGroup)) {
			return false;
		}
		HedisMeasureGroup other = (HedisMeasureGroup) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.QualityMeasureGroup[ id=" + id + " ]";
	}

}
