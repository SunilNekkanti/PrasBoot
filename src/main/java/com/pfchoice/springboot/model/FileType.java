package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity(name = "file_type")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class FileType extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "code", nullable = false)
	private Integer id;

	@NotNull
	@Size(min = 5, max = 150, message = "The description must be between {min} and {max} characters long")
	@Column(name = "description")
	private String description;

	@NotNull(message = "Select Insurance")
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ins_id", referencedColumnName = "Insurance_Id")
	private Insurance ins;

	@NotNull(message = "Select Active month indicator")
	@Column(name = "activity_Month_ind", nullable = true)
	private Character activityMonthInd;

	@Column(name = "tables_name", nullable = true)
	private String tablesName;

	@Column(name = "insurance_code", nullable = true)
	private String insuranceCode;

	@Column(name = "entity_name", nullable = true)
	private String entityClassName;

	/**
	 * 
	 */
	public FileType() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public FileType(final Integer id) {
		super();
		this.id = id;
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

	public Insurance getIns() {
		return ins;
	}

	public void setIns(Insurance ins) {
		this.ins = ins;
	}

	public Character getActivityMonthInd() {
		return activityMonthInd;
	}

	public void setActivityMonthInd(Character activityMonthInd) {
		this.activityMonthInd = activityMonthInd;
	}

	/**
	 * @return
	 */
	public String getTablesName() {
		return tablesName;
	}

	/**
	 * @param tablesName
	 */
	public void setTablesName(String tablesName) {
		this.tablesName = tablesName;
	}

	public String getInsuranceCode() {
		return insuranceCode;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	/**
	 * @return the entityClassName
	 */
	public String getEntityClassName() {
		return entityClassName;
	}

	/**
	 * @param entityClassName
	 *            the entityClassName to set
	 */
	public void setEntityClassName(String entityClassName) {
		this.entityClassName = entityClassName;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof FileType)) {
			return false;
		}
		FileType other = (FileType) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.County[ id=" + id + " ]";
	}

}
