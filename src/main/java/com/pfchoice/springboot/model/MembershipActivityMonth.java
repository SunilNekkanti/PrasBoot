package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author SarathGandluri
 */
@Entity
@Table(name = "membership_activity_month")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipActivityMonth extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_act_mnth_id", nullable = false)
	private Integer id;

	@Column(name = "activity_month")
	private Integer activityMonth;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "mbr_id", nullable = false, referencedColumnName = "mbr_id")
	private Membership mbr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prvdr_id", nullable = false, referencedColumnName = "prvdr_id")
	private Provider prvdr;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ins_id", nullable = false, referencedColumnName = "insurance_id")
	private Insurance ins;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "file_id", nullable = false, referencedColumnName = "file_id")
	private File file;

	@Column(name = "is_roster")
	private Character isRoster;

	@Column(name = "is_cap")
	private Character isCap;
	
	@Column(name = "raf_score" )
	private BigDecimal rafScore ;

	@Transient
	private String mbrFullName;

	@Transient
	private String prvdrName;

	/**
	 * 
	 */
	public MembershipActivityMonth() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipActivityMonth(final Integer id) {
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
	 * @return the activityMonth
	 */
	public Integer getActivityMonth() {
		return activityMonth;
	}

	/**
	 * @param activityMonth
	 *            the activityMonth to set
	 */
	public void setActivityMonth(Integer activityMonth) {
		this.activityMonth = activityMonth;
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
	 * @return the prvdr
	 */
	public Provider getPrvdr() {
		return prvdr;
	}

	/**
	 * @param prvdr
	 *            the prvdr to set
	 */
	public void setPrvdr(Provider prvdr) {
		this.prvdr = prvdr;
	}

	/**
	 * @return the insId
	 */
	public Insurance getIns() {
		return ins;
	}

	/**
	 * @param insId
	 *            the insId to set
	 */
	public void setIns(Insurance ins) {
		this.ins = ins;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return
	 */
	public Character getIsRoster() {
		return isRoster;
	}

	/**
	 * @param isRoster
	 */
	public void setIsRoster(Character isRoster) {
		this.isRoster = isRoster;
	}

	/**
	 * @return
	 */
	public Character getIsCap() {
		return isCap;
	}

	/**
	 * @param isCap
	 */
	public void setIsCap(Character isCap) {
		this.isCap = isCap;
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
	 * @return the mbrFullName
	 */
	public String getMbrFullName() {
		return mbr.getLastName() + ',' + mbr.getFirstName();
	}

	/**
	 * @param mbrFullName
	 *            the mbrFullName to set
	 */
	public void setMbrFullName(String mbrFullName) {
		this.mbrFullName = mbrFullName;
	}

	/**
	 * @return the prvdrName
	 */
	public String getPrvdrName() {
		return prvdr.getName();
	}

	/**
	 * @param prvdrName
	 *            the prvdrName to set
	 */
	public void setPrvdrName(String prvdrName) {
		this.prvdrName = prvdrName;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipActivityMonth)) {
			return false;
		}
		MembershipActivityMonth other = (MembershipActivityMonth) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipActivityMonth[ id=" + id + " ]";
	}

}
