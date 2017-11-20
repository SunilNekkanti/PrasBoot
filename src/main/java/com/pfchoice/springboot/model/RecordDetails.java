/**
 * 
 */
package com.pfchoice.springboot.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pfchoice.springboot.util.JsonDateAndTimeDeserializer;

/**
 * @author sarath
 *
 */
@MappedSuperclass
public class RecordDetails {

	@JsonIgnore
	@JsonDeserialize(using = JsonDateAndTimeDeserializer.class)
	@Column(name = "created_date", updatable = false)
	protected Date createdDate = new Date();

	@JsonIgnore
	@JsonDeserialize(using = JsonDateAndTimeDeserializer.class)
	@Column(name = "updated_date", updatable = false)
	private Date updatedDate = new Date();

	@JsonIgnore
	@Column(name = "created_by")
	protected String createdBy = "sarath";

	@JsonIgnore
	@Column(name = "updated_by")
	private String updatedBy = "sarath";

	@JsonIgnore
	@Column(name = "active_ind", insertable = false)
	private Character activeInd = new Character('Y');

	/**
	 * 
	 */
	public RecordDetails() {
		super();
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate
	 *            the updatedDate to set
	 */
	public void setUpdatedDate(final Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param credtedBy
	 *            the credtedBy to set
	 */
	public void setCreatedBy(final String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy
	 *            the updatedBy to set
	 */
	public void setUpdatedBy(final String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the activeInd
	 */
	public Character getActiveInd() {
		return activeInd;
	}

	/**
	 * @param activeInd
	 *            the activeInd to set
	 */
	public void setActiveInd(final Character activeInd) {
		this.activeInd = activeInd;
	}

}
