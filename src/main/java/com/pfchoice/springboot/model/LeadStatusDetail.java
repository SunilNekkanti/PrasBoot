package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Mohanasundharam
 */
@Entity
@Table(name = "lu_membership_status_detail")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class LeadStatusDetail extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "code", columnDefinition = "TINYINT", nullable = false)
	private Short id;

	@Column(name = "description")
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "lu_membership_status_id", referencedColumnName = "code")
	private LeadStatus leadStatus;
	

	/**
	 * 
	 */
	public LeadStatusDetail() {
		super();
	}

	/**
	 * @param id
	 */
	public LeadStatusDetail(final Short id) {
		super();
		this.id = id;
	}

	/**
	 * @return
	 */
	public Short getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(final Short id) {
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

	/**
	 * @return the leadStatus
	 */
	public LeadStatus getLeadStatus() {
		return leadStatus;
	}

	/**
	 * @param leadStatus the leadStatus to set
	 */
	public void setLeadStatus(LeadStatus leadStatus) {
		this.leadStatus = leadStatus;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LeadStatusDetail)) {
			return false;
		}
		LeadStatusDetail other = (LeadStatusDetail) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.LeadStatus[ id=" + id + " ]";
	}

}
