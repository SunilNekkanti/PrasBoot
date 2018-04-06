package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.WhereJoinTable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author SarathGandluri
 */
@Entity
@Table(name = "insurance")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Insurance extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "Insurance_Id", nullable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "plan_Type_id", referencedColumnName = "plan_type_id")
	private PlanType planType;

	@OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "reference_contacts", joinColumns = {
			@JoinColumn(name = "insurance_id", referencedColumnName = "Insurance_Id", nullable = false, unique = true) }, inverseJoinColumns = {
					@JoinColumn(name = "cnt_id", referencedColumnName = "cnt_id", nullable = false, unique = true) })
	private Contact contact;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "reference_contracts", joinColumns = {
			@JoinColumn(name = "insurance_id", referencedColumnName = "Insurance_Id", nullable = false, unique = true) }, inverseJoinColumns = {
					@JoinColumn(name = "contract_id", referencedColumnName = "contract_id", nullable = false, unique = true) })
	@WhereJoinTable(clause="prvdr_id is null")
	private Set<Contract> contracts;

	/**
	 * 
	 */
	public Insurance() {
		super();
	}

	/**
	 * @param id
	 */
	public Insurance(final Integer id) {
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the planTypeId
	 */
	public PlanType getPlanType() {
		return planType;
	}

	/**
	 * @param planTypeId
	 *            the planTypeId to set
	 */
	public void setPlanType(PlanType planType) {
		this.planType = planType;
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
	 * @return the contracts
	 */
	public Set<Contract> getContracts() {
		return contracts;
	}

	/**
	 * @param contracts
	 *            the contracts to set
	 */
	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Insurance)) {
			return false;
		}
		Insurance other = (Insurance) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Insurance[ id=" + id + " ]";
	}

}
