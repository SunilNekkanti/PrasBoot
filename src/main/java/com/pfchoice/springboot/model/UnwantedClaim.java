package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Component;



/**
 *
 * @author sarath
 */
@Component
public class UnwantedClaim  extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	private String firstName;

	
	private String lastName;

	
	private Character gender;
	
	
	@Temporal(TemporalType.DATE)
	private Date dob;

	
	private String claimType;
	
	
	private Double unwantedClaims;

	/**
	 * 
	 */
	public UnwantedClaim() {
		super();
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Double getUnwantedClaims() {
		return unwantedClaims;
	}

	public void setUnwantedClaims(Double unwantedClaims) {
		this.unwantedClaims = unwantedClaims;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	
}
