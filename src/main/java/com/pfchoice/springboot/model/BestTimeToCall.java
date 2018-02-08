package com.pfchoice.springboot.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Mohanasundharam
 */
@Entity(name = "lu_best_time_to_call")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class BestTimeToCall extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "code", nullable = false, columnDefinition = "tinyint")
	private Short id;

	@Column(name = "description")
	private String description;

	/**
	 * 
	 */
	public BestTimeToCall() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public BestTimeToCall(final Short id) {
		super();
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public Short getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(final Short id) {
		this.id = id;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof BestTimeToCall)) {
			return false;
		}
		BestTimeToCall other = (BestTimeToCall) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.BestTimeToCall[ id=" + id + " ]";
	}

}
