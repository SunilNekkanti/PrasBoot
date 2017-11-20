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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 *
 * @author sarath
 */
@Entity(name = "membership_hospitalization_details")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipHospitalizationDetails extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_hos_details_id", nullable = false)
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_hos_id", referencedColumnName = "mbr_hos_id")
	private MembershipHospitalization mbrHospitalization;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "att_phy_id", referencedColumnName = "att_phy_id")
	private AttPhysician attPhysician;

	
	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "room_type_code", referencedColumnName = "code")
	private PlaceOfService roomType;

	
	@Column(name = "adm_dx")
	private String admDx;

	
	@Temporal(TemporalType.DATE)
	@Column(name = "exp_dc_dt")
	protected Date expDisDate;

	
	@Column(name = "auth_days")
	private Integer authDays;

	
	@Column(name = "cm_pri_user")
	private String cmPriUser;

	
	@Column(name = "disease_cohort")
	private String diseaseCohort;

	
	@Column(name = "comorbidities")
	private String comorbidities;

	
	@Column(name = "file_id")
	private Integer fileId;

	/**
	 * 
	 */
	public MembershipHospitalizationDetails() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipHospitalizationDetails(final Integer id) {
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
	 * @return the mbrHospitalization
	 */
	public MembershipHospitalization getMbrHospitalization() {
		return mbrHospitalization;
	}

	/**
	 * @param mbrHospitalization
	 *            the mbrHospitalization to set
	 */
	public void setMbrHospitalization(MembershipHospitalization mbrHospitalization) {
		this.mbrHospitalization = mbrHospitalization;
	}

	/**
	 * @return the attPhysician
	 */
	public AttPhysician getAttPhysician() {
		return attPhysician;
	}

	/**
	 * @param attPhysician
	 *            the attPhysician to set
	 */
	public void setAttPhysician(AttPhysician attPhysician) {
		this.attPhysician = attPhysician;
	}

	/**
	 * @return the roomType
	 */
	public PlaceOfService getRoomType() {
		return roomType;
	}

	/**
	 * @param roomType
	 *            the roomType to set
	 */
	public void setRoomType(PlaceOfService roomType) {
		this.roomType = roomType;
	}

	/**
	 * @return the admDx
	 */
	public String getAdmDx() {
		return admDx;
	}

	/**
	 * @param admDx
	 *            the admDx to set
	 */
	public void setAdmDx(String admDx) {
		this.admDx = admDx;
	}

	/**
	 * @return the expDisDate
	 */
	public Date getExpDisDate() {
		return expDisDate;
	}

	/**
	 * @param expDisDate
	 *            the expDisDate to set
	 */
	public void setExpDisDate(Date expDisDate) {
		this.expDisDate = expDisDate;
	}

	/**
	 * @return the authDays
	 */
	public Integer getAuthDays() {
		return authDays;
	}

	/**
	 * @param authDays
	 *            the authDays to set
	 */
	public void setAuthDays(Integer authDays) {
		this.authDays = authDays;
	}

	/**
	 * @return the cmPriUser
	 */
	public String getCmPriUser() {
		return cmPriUser;
	}

	/**
	 * @param cmPriUser
	 *            the cmPriUser to set
	 */
	public void setCmPriUser(String cmPriUser) {
		this.cmPriUser = cmPriUser;
	}

	/**
	 * @return the diseaseCohort
	 */
	public String getDiseaseCohort() {
		return diseaseCohort;
	}

	/**
	 * @param diseaseCohort
	 *            the diseaseCohort to set
	 */
	public void setDiseaseCohort(String diseaseCohort) {
		this.diseaseCohort = diseaseCohort;
	}

	/**
	 * @return the comorbidities
	 */
	public String getComorbidities() {
		return comorbidities;
	}

	/**
	 * @param comorbidities
	 *            the comorbidities to set
	 */
	public void setComorbidities(String comorbidities) {
		this.comorbidities = comorbidities;
	}

	/**
	 * @return the fileId
	 */
	public Integer getFileId() {
		return fileId;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(final Integer fileId) {
		this.fileId = fileId;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipHospitalizationDetails)) {
			return false;
		}
		MembershipHospitalizationDetails other = (MembershipHospitalizationDetails) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipHospitalizationDetails[ id=" + id + " ]";
	}

}
