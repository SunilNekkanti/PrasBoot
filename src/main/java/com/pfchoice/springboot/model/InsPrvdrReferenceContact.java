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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 *
 * @author sarath
 */
@Entity(name = "reference_contact")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class InsPrvdrReferenceContact extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ref_cnt_Id", nullable = false)
	private Integer id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mbr_id", referencedColumnName = "mbr_id", nullable=true)
	private Membership mbr;
	
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "lead_Mbr_Id", referencedColumnName = "lead_Mbr_Id", nullable=true)
//	private LeadMembership leadMbr;

	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "insurance_id", referencedColumnName = "insurance_id", nullable=true)
	private Insurance ins;

	//
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id", nullable=true)
	private Provider prvdr;

	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "refContact")
	private Contact cnt;

	/**
	 * 
	 */
	public InsPrvdrReferenceContact() {
		super();
	}

	/**
	 * @param id
	 */
	public InsPrvdrReferenceContact(final Integer id) {
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
	public void setMbr(final Membership mbr) {
		this.mbr = mbr;
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
	public void setIns(final Insurance ins) {
		this.ins = ins;
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
	public void setPrvdr(final Provider prvdr) {
		this.prvdr = prvdr;
	}

	/**
	 * @return the cnt
	 */
	public Contact getCnt() {
		return cnt;
	}

	/**
	 * @param cnt
	 *            the cnt to set
	 */
	public void setCnt(final Contact cnt) {
		this.cnt = cnt;
	}
	
	/**
	 * @return
	 */
	/*public LeadMembership getLeadMbr() {
		return leadMbr;
	}*/

	/**
	 * @param leadMbr
	 */
	/*public void setLeadMbr(LeadMembership leadMbr) {
		this.leadMbr = leadMbr;
	}*/

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof InsPrvdrReferenceContact)) {
			return false;
		}
		InsPrvdrReferenceContact other = (InsPrvdrReferenceContact) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Contact[ id=" + id + " ]";
	}

}
