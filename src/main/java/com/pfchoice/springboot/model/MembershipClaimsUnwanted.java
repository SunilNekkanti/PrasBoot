package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MembershipClaimsUnwanted implements Serializable {

	private static final long serialVersionUID = 1L;

	private String claimType;

	private String firstName;

	private String lastName;

	@Temporal(TemporalType.DATE)
	private Date dob;

	private Character gender;

	private double unwantedClaims;

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public double getUnwantedClaims() {
		return unwantedClaims;
	}

	public void setUnwantedClaims(double unwantedClaims) {
		this.unwantedClaims = unwantedClaims;
	}

}
