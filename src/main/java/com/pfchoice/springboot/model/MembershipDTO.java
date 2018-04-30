package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "membership")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@FilterDefs(
	@FilterDef(name="followupTypeFilter", defaultCondition="followup_type_id=:followupTypeId" , parameters = { @ParamDef(name = "followupTypeId", type = "int") })
)
public class MembershipDTO extends RecordDetails implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Id
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

	@OneToOne(optional = true,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "reference_contacts", joinColumns = {
			@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "cnt_id", referencedColumnName = "cnt_id", nullable = false, unique = true) })
	@Where(clause = "active_ind ='Y'")
	private Contact contact;
	
	@Column(name = "SRC_SYS_MBR_NBR")
	private String srcSysMbrNbr;
	
	@Column(name = "mbr_hasdisability", insertable = false)
	private Character hasDisability = new Character('N');
	
	@Column(name = "mbr_hasMedicaid", insertable = false)
	private Character hasMedicaid = new Character('N');

	@Transient
	private BigDecimal rafScore ;

	@JsonIgnore
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 25)
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "mbr")
	@OrderBy("createdDate Desc")
	@Filter(name="followupTypeFilter")
	private List<MembershipFollowup> mbrFollowupList;
	

	
	/**
	 * 
	 */
	public MembershipDTO() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipDTO(final Integer id) {
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
		medicareNo = (mbrFollowupList != null && mbrFollowupList.size() > 0)?  mbrFollowupList.get(0).getFollowupDetails():"";
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
	 * @return the contact
	 */
	public Contact getContact() {
		getMbrFollowupList();
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
	
	/**
	 * @return the hasDisability
	 */
	public Character getHasDisability() {
		return hasDisability;
	}

	/**
	 * @param hasDisability the hasDisability to set
	 */
	public void setHasDisability(Character hasDisability) {
		this.hasDisability = hasDisability;
	}

	/**
	 * @return the hasMedicaid
	 */
	public Character getHasMedicaid() {
		return hasMedicaid;
	}

	/**
	 * @param hasMedicaid the hasMedicaid to set
	 */
	public void setHasMedicaid(Character hasMedicaid) {
		this.hasMedicaid = hasMedicaid;
	}

	/**
	 * @return the rafScore
	 */
	public BigDecimal getRafScore() {
		return rafScore;
	}

	/**
	 * @param rafScore the rafScore to set
	 */
	public void setRafScore(BigDecimal rafScore) {
		this.rafScore = rafScore;
	}

	/**
	 * @return the mbrFollowupList
	 */
	public List<MembershipFollowup> getMbrFollowupList() {
		return mbrFollowupList;
	}

	/**
	 * @param mbrFollowupList the mbrFollowupList to set
	 */
	public void setMbrFollowupList(List<MembershipFollowup> mbrFollowupList) {
		this.mbrFollowupList = mbrFollowupList;
	}

	 
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipDTO)) {
			return false;
		}
		MembershipDTO other = (MembershipDTO) object;
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
