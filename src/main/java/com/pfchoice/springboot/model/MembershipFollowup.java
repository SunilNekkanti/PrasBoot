package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 *
 * @author SarathGandluri
 */
@Entity(name = "membership_followup")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipFollowup extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_hedis_followup_id", nullable = false)
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", nullable = false, referencedColumnName = "mbr_id")
	private Membership mbr;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "followup_type_id", nullable = false, referencedColumnName = "id")
	private FollowupType followupType;

	
	@Column(name = "followup_details")
	private String followupDetails;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_contact")
	private Date dateOfContact;

	@Transient
	private List<Map<Integer, String>> mbrHedisMeasureIds = new ArrayList<>();
	
	@Transient
	private String notesHistory;

	/**
	 * 
	 */
	public MembershipFollowup() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipFollowup(final Integer id) {
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
	 * @return the followupType
	 */
	public FollowupType getFollowupType() {
		return followupType;
	}

	/**
	 * @param followupType
	 *            the followupType to set
	 */
	public void setFollowupType(FollowupType followupType) {
		this.followupType = followupType;
	}

	/**
	 * @return the followupDetails
	 */
	public String getFollowupDetails() {
		return followupDetails;
	}

	/**
	 * @param followupDetails
	 *            the followupDetails to set
	 */
	public void setFollowupDetails(String followupDetails) {
		this.followupDetails = followupDetails;
	}

	/**
	 * @return the dateOfContact
	 */
	public Date getDateOfContact() {
		return dateOfContact;
	}

	/**
	 * @param dateOfContact
	 *            the dateOfContact to set
	 */
	public void setDateOfContact(Date dateOfContact) {
		this.dateOfContact = dateOfContact;
	}

	/**
	 * @return the ruleIds
	 */
	public List<Map<Integer, String>> getMbrHedisMeasureIds() {
		return mbrHedisMeasureIds;
	}

	/**
	 * @param ruleIds
	 *            the ruleIds to set
	 */
	public void setMbrHedisMeasureIds(List<Map<Integer, String>> mbrHedisMeasureIds) {
		this.mbrHedisMeasureIds = mbrHedisMeasureIds;
	}

	/**
	 * @return the notesHistory
	 */
	public String getNotesHistory() {
		return createdDate+" >>>>> "+createdBy+" >>> "+followupDetails;
	}

	/**
	 * @param notesHistory the notesHistory to set
	 */
	public void setNotesHistory(String notesHistory) {
		this.notesHistory = notesHistory;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipFollowup)) {
			return false;
		}
		MembershipFollowup other = (MembershipFollowup) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MemberhsipHedisFollowup[ id=" + id + " ]";
	}

}
