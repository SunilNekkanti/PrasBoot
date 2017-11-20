package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 *
 * @author sarath
 */
@Entity
@Table(name = "hedis_measure")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class HedisMeasure extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "qlty_msr_id", nullable = false)
	private Integer id;

	
	@Column(name = "code", nullable = false)
	private String code;

	
	@Column(name = "description")
	private String description;

	
	@Transient
	private String codeAndDescription;

	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "qlty_msr_group_id", nullable = false, referencedColumnName = "qlty_msr_group_id")
	private HedisMeasureGroup hedisMsrGrp;

	/**
	 * 
	 */
	public HedisMeasure() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public HedisMeasure(final Integer id) {
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
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 
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
	 * @return the qltyMsrGrpId
	 */
	public HedisMeasureGroup getHedisMsrGrp() {
		return hedisMsrGrp;
	}

	/**
	 * @param qltyMsrGrpId
	 *            the qltyMsrGrpId to set
	 */
	public void setHedisMsrGrp(final HedisMeasureGroup hedisMsrGrp) {
		this.hedisMsrGrp = hedisMsrGrp;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof HedisMeasure)) {
			return false;
		}
		HedisMeasure other = (HedisMeasure) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.HedisMeasure[ id=" + id + " ]";
	}

}