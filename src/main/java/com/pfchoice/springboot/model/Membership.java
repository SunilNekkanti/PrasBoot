package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "membership")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Membership extends RecordDetails implements Serializable, FieldHandled {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_id", nullable = false)
	private Integer id;

	@Column(name = "mbr_firstname")
	private String firstName;

	@Column(name = "mbr_lastname")
	private String lastName;

	@ManyToOne
	@JoinColumn(name = "mbr_genderid", referencedColumnName = "gender_id")
	private Gender genderId;

	@Column(name = "Mbr_CountyCode")
	private String countyCode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_ethinic_code", referencedColumnName = "code")
	private Ethinicity ethinicCode;

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 25)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "mbr")
	@Where(clause = "active_ind ='Y'")
	private List<MembershipProvider> mbrProviderList;

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 25)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "mbr")
	@Where(clause = "active_ind ='Y'")
	private List<MembershipInsurance> mbrInsuranceList;

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 25)
	@OneToMany(mappedBy = "mbr", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("dueDate")
	private List<MembershipHedisMeasure> mbrHedisMeasureList;

	@Column(name = "mbr_dob")
	@Temporal(TemporalType.DATE)
	private Date dob;

	@Column(name = "mbr_medicaidno")
	private String medicaidNo;

	@Column(name = "mbr_medicareno")
	private String medicareNo;

	@Column(name = "file_id")
	private Integer fileId;

	@OneToOne
	@JoinColumn(name = "mbr_status", referencedColumnName = "code")
	@Where(clause = "active_ind ='Y'")
	private MembershipStatus status;

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 25)
	@OneToMany(mappedBy = "mbr", fetch = FetchType.EAGER)
	@Where(clause = "active_ind ='Y'")
	@OrderBy("admitDate")
	private List<MembershipHospitalization> mbrHospitalizationList;

	@JsonIgnore
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 25)
	@OneToMany(mappedBy = "mbr", fetch = FetchType.LAZY)
	@Where(clause = "active_ind ='Y'")
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<MembershipClaim> mbrClaimList;

	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 25)
	@OneToMany(mappedBy = "mbr", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("startDate")
	private List<MembershipProblem> mbrProblemList;

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "mbr", fetch = FetchType.EAGER)
	@Where(clause = "active_ind ='Y'")
	private List<MembershipActivityMonth> mbrActivityMonthList;

	@OneToOne(optional = true,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "reference_contacts", joinColumns = {
			@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "cnt_id", referencedColumnName = "cnt_id", nullable = false, unique = true) })
	@Where(clause = "active_ind ='Y'")
	private Contact contact;
	
	@Column(name = "SRC_SYS_MBR_NBR")
	private String srcSysMbrNbr;

	@JsonIgnore
	private FieldHandler fieldHandler;

	/**
	 * 
	 */
	public Membership() {
		super();
	}

	/**
	 * @param id
	 */
	public Membership(final Integer id) {
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the genderId
	 */
	public Gender getGenderId() {
		return genderId;
	}

	/**
	 * @param genderId
	 *            the genderId to set
	 */
	public void setGenderId(final Gender genderId) {
		this.genderId = genderId;
	}

	/**
	 * @return the countyCode
	 */
	public String getCountyCode() {
		return countyCode;
	}

	/**
	 * @param countyCode
	 *            the countyCode to set
	 */
	public void setCountyCode(final String countyCode) {
		this.countyCode = countyCode;
	}

	/**
	 * @return the mbrProviderList
	 */
	public List<MembershipProvider> getMbrProviderList() {
		return mbrProviderList;
	}

	/**
	 * @param mbrProviderList
	 *            the mbrProviderList to set
	 */
	public void setMbrProviderList(List<MembershipProvider> mbrProviderList) {
		this.mbrProviderList = mbrProviderList;

	}

	/**
	 * @return the mbrInsuranceList
	 */
	public List<MembershipInsurance> getMbrInsuranceList() {
		return mbrInsuranceList;
	}

	/**
	 * @param mbrInsuranceList
	 *            the mbrInsuranceList to set
	 */
	public void setMbrInsuranceList(List<MembershipInsurance> mbrInsuranceList) {
		this.mbrInsuranceList = mbrInsuranceList;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @return the ethinicCode
	 */
	public Ethinicity getEthinicCode() {
		return ethinicCode;
	}

	/**
	 * @param ethinicCode
	 *            the ethinicCode to set
	 */
	public void setEthinicCode(final Ethinicity ethinicCode) {
		this.ethinicCode = ethinicCode;
	}

	/**
	 * @return the status
	 */
	public MembershipStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final MembershipStatus status) {
		this.status = status;
	}

	/**
	 * @return the medicaidNo
	 */
	public String getMedicaidNo() {
		return medicaidNo;
	}

	/**
	 * @param medicaidNo
	 *            the medicaidNo to set
	 */
	public void setMedicaidNo(final String medicaidNo) {
		this.medicaidNo = medicaidNo;
	}

	/**
	 * @return the medicareNo
	 */
	public String getMedicareNo() {
		return medicareNo;
	}

	/**
	 * @param medicareNo
	 *            the medicareNo to set
	 */
	public void setMedicareNo(final String medicareNo) {
		this.medicareNo = medicareNo;
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

	/**
	 * @return the mbrHedisMeasureList
	 */
	public List<MembershipHedisMeasure> getMbrHedisMeasureList() {
		return mbrHedisMeasureList;
	}

	/**
	 * @param mbrHedisMeasureList
	 *            the mbrHedisMeasureList to set
	 */
	public void setMbrHedisMeasureList(List<MembershipHedisMeasure> mbrHedisMeasureList) {
		this.mbrHedisMeasureList = mbrHedisMeasureList;
	}

	/**
	 * @return the mbrHospitalizationList
	 */
	public List<MembershipHospitalization> getMbrHospitalizationList() {
		return mbrHospitalizationList;
	}

	/**
	 * @param mbrHospitalizationList
	 *            the mbrHospitalizationList to set
	 */
	public void setMbrHospitalizationList(List<MembershipHospitalization> mbrHospitalizationList) {
		this.mbrHospitalizationList = mbrHospitalizationList;
	}

	/**
	 * @return the mbrClaimList
	 */
	public List<MembershipClaim> getMbrClaimList() {
		return mbrClaimList;
	}

	/**
	 * @return the mbrProblemList
	 */
	public List<MembershipProblem> getMbrProblemList() {
		return mbrProblemList;
	}

	/**
	 * @param mbrProblemList
	 *            the mbrProblemList to set
	 */
	public void setMbrProblemList(List<MembershipProblem> mbrProblemList) {
		this.mbrProblemList = mbrProblemList;
	}

	/**
	 * @param mbrClaimList
	 *            the mbrClaimList to set
	 */
	@SuppressWarnings("unchecked")
	public void setMbrClaimList(List<MembershipClaim> mbrClaimList) {
		if (fieldHandler != null) {
			this.mbrClaimList = (List<MembershipClaim>) fieldHandler.writeObject(this, "mbrClaimList",
					this.mbrClaimList, mbrClaimList);
			return;
		}
		this.mbrClaimList = mbrClaimList;
	}

	/**
	 * @return
	 */
	/*
	 * public List<Contact> getContactList() { return contactList; }
	 */

	/**
	 * @param contactList
	 */
	/*
	 * public void setContactList(List<Contact> contactList) { this.contactList
	 * = contactList; }
	 */

	/**
	 * @return
	 */
	public List<MembershipActivityMonth> getMbrActivityMonthList() {
		return mbrActivityMonthList;
	}

	/**
	 * @param mbrActivityMonthList
	 */
	public void setMbrActivityMonthList(List<MembershipActivityMonth> mbrActivityMonthList) {
		this.mbrActivityMonthList = mbrActivityMonthList;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the srcSysMbrNbr
	 */
	public String getSrcSysMbrNbr() {
		return srcSysMbrNbr;
	}

	/**
	 * @param srcSysMbrNbr the srcSysMbrNbr to set
	 */
	public void setSrcSysMbrNbr(String srcSysMbrNbr) {
		this.srcSysMbrNbr = srcSysMbrNbr;
	}

	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Membership)) {
			return false;
		}
		Membership other = (Membership) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Membership[ id=" + id + "]";
	}

}
