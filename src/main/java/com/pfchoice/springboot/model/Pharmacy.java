package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "pharmacy")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Pharmacy extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "pharmacy_id", nullable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "pharmacy_number")
	private String pharmacyNumber;

	@Column(name = "address_1")
	private String address1;

	@Column(name = "address_2")
	private String address2;

	@Column(name = "city")
	private String city;

	@Column(name = "state_code")
	private String stateCode;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "ncp_dpn_number")
	private String ncpDpnNumber;

	@Column(name = "npi_number")
	private String npiNumber;

	@Column(name = "file_id", nullable = false)
	private Integer fileId;

	/**
	 * 
	 */
	public Pharmacy() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public Pharmacy(final Integer id) {
		super();
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
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
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pharmacyNumber
	 */
	public String getPharmacyNumber() {
		return pharmacyNumber;
	}

	/**
	 * @param pharmacyNumber
	 *            the pharmacyNumber to set
	 */
	public void setPharmacyNumber(String pharmacyNumber) {
		this.pharmacyNumber = pharmacyNumber;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1
	 *            the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2
	 *            the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the stateCode
	 */
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCode
	 *            the stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the ncpDpnNumber
	 */
	public String getNcpDpnNumber() {
		return ncpDpnNumber;
	}

	/**
	 * @param ncpDpnNumber
	 *            the ncpDpnNumber to set
	 */
	public void setNcpDpnNumber(String ncpDpnNumber) {
		this.ncpDpnNumber = ncpDpnNumber;
	}

	/**
	 * @return the npiNumber
	 */
	public String getNpiNumber() {
		return npiNumber;
	}

	/**
	 * @param npiNumber
	 *            the npiNumber to set
	 */
	public void setNpiNumber(String npiNumber) {
		this.npiNumber = npiNumber;
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
	public void setFileId(Integer fileId) {
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
		if (!(object instanceof Pharmacy)) {
			return false;
		}
		Pharmacy other = (Pharmacy) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.AttPhysician[ id=" + id + " ]";
	}

}
