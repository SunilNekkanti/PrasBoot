package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 *
 * @author sarath
 */
@Entity(name = "membership_hospitalization")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipHospitalization extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "mbr_hos_id", nullable = false)
	private Integer id;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hos_id", referencedColumnName = "hos_id")
	@Where(clause = "active_ind ='Y'")
	private Hospital hospital;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id")
	private Membership mbr;

	@OneToOne(fetch = FetchType.LAZY)
	@Where(clause = "active_ind ='Y'")
	@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id")
	private Provider prvdr;

	@OneToOne(fetch = FetchType.LAZY)
	@Where(clause = "active_ind ='Y'")
	@JoinColumn(name = "ins_id", referencedColumnName = "insurance_id")
	private Insurance ins;

	
	@Column(name = "report")
	private String report;

	
	@Column(name = "plan_desc")
	private String planDesc;

	
	@Column(name = "authnum")
	private String authNum;

	
	@Column(name = "prior_admits")
	private Integer priorAdmits;

	
	@Temporal(TemporalType.DATE)
	@Column(name = "admit_date")
	protected Date admitDate;

	
	@Temporal(TemporalType.DATE)
	@Column(name = "exp_dc_date")
	protected Date expDisDate;

	
	@Column(name = "file_id")
	private Integer fileId;


	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "mbrHospitalization", fetch = FetchType.LAZY)
	private List<MembershipHospitalizationDetails> mbrHospitalizationDetailsList;

	/**
	 * 
	 */
	public MembershipHospitalization() {
		super();
	}

	/**
	 * @param id
	 */
	public MembershipHospitalization(final Integer id) {
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
	 * @return the hospital
	 */
	public Hospital getHospital() {
		return hospital;
	}

	/**
	 * @param hospital
	 *            the hospital to set
	 */
	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
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
	 * @return the ins
	 */
	public Insurance getIns() {
		return ins;
	}

	/**
	 * @param ins
	 *            the ins to set
	 */
	public void setIns(Insurance ins) {
		this.ins = ins;
	}

	/**
	 * @return the report
	 */
	public String getReport() {
		return report;
	}

	/**
	 * @param report
	 *            the report to set
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * @return the planDesc
	 */
	public String getPlanDesc() {
		return planDesc;
	}

	/**
	 * @param planDesc
	 *            the planDesc to set
	 */
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	/**
	 * @return the authNum
	 */
	public String getAuthNum() {
		return authNum;
	}

	/**
	 * @param authNum
	 *            the authNum to set
	 */
	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}

	/**
	 * @return the priorAdmits
	 */
	public Integer getPriorAdmits() {
		return priorAdmits;
	}

	/**
	 * @param priorAdmits
	 *            the priorAdmits to set
	 */
	public void setPriorAdmits(Integer priorAdmits) {
		this.priorAdmits = priorAdmits;
	}

	/**
	 * @return the admitDate
	 */
	public Date getAdmitDate() {
		return admitDate;
	}

	/**
	 * @param admitDate
	 *            the admitDate to set
	 */
	public void setAdmitDate(Date admitDate) {
		this.admitDate = admitDate;
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
	 * @return the mbrHospitalizationDetailsList
	 */
	public List<MembershipHospitalizationDetails> getMbrHospitalizationDetailsList() {
		return mbrHospitalizationDetailsList;
	}

	/**
	 * @param mbrHospitalizationDetailsList
	 *            the mbrHospitalizationDetailsList to set
	 */
	public void setMbrHospitalizationDetailsList(List<MembershipHospitalizationDetails> mbrHospitalizationDetailsList) {
		this.mbrHospitalizationDetailsList = mbrHospitalizationDetailsList;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MembershipHospitalization)) {
			return false;
		}
		MembershipHospitalization other = (MembershipHospitalization) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.MembershipHospitalization[ id=" + id + " ]";
	}

}
