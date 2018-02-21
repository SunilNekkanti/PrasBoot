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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Entity
@Table(name = "lead_membership_flags")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class LeadMembershipFlag extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "lead_Mbr_flag_Id", nullable = false, unique = true)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lead_mbr_id", referencedColumnName = "lead_mbr_id", nullable=false)
	private LeadMembership lead;
	
	@Column(name = "scheduled_flag")
	private Character scheduledFlag = new Character('N');
	
	@Column(name = "engaged_flag")
	private Character engagedFlag = new Character('N');
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "scheduled_date")
	private Date scheduledDate;	
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "engaged_date")
	private Date engagedDate;
	
	/**
	 * Default constructor there exists one constructor with id as parameter
	 */
	public LeadMembershipFlag() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public LeadMembershipFlag(final Integer id) {
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
	 * @return the lead
	 */
	public LeadMembership getLead() {
		return lead;
	}

	/**
	 * @param lead the lead to set
	 */
	public void setLead(LeadMembership lead) {
		this.lead = lead;
	}

	/**
	 * @return the scheduledFlag
	 */
	public Character getScheduledFlag() {
		return scheduledFlag;
	}

	/**
	 * @param scheduledFlag the scheduledFlag to set
	 */
	public void setScheduledFlag(Character scheduledFlag) {
		this.scheduledFlag = scheduledFlag;
	}

	/**
	 * @return the engagedFlag
	 */
	public Character getEngagedFlag() {
		return engagedFlag;
	}

	/**
	 * @param engagedFlag the engagedFlag to set
	 */
	public void setEngagedFlag(Character engagedFlag) {
		this.engagedFlag = engagedFlag;
	}

	/**
	 * @return the scheduledDate
	 */
	public Date getScheduledDate() {
		return scheduledDate;
	}

	/**
	 * @param scheduledDate the scheduledDate to set
	 */
	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	/**
	 * @return the engagedDate
	 */
	public Date getEngagedDate() {
		return engagedDate;
	}

	/**
	 * @param engagedDate the engagedDate to set
	 */
	public void setEngagedDate(Date engagedDate) {
		this.engagedDate = engagedDate;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LeadMembershipFlag)) {
			return false;
		}
		LeadMembershipFlag other = (LeadMembershipFlag) object;
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
