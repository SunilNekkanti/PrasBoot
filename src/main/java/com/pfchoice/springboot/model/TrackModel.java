package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity(name = "lu_track_model")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class TrackModel extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "code", nullable = false)
	private Byte code;

	@Column(name = "description")
	private String description;

	/**
	 * 
	 */
	public TrackModel() {
		super();
	}

	/**
	 * @param code
	 */
	public TrackModel(final Byte code) {
		super();
		this.code = code;
	}

	/**
	 * @return
	 */
	public Byte getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(final Byte code) {
		this.code = code;
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
		hash += (code != null ? code.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TrackModel)) {
			return false;
		}
		TrackModel other = (TrackModel) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.TrackModel[ code=" + code + " ]";
	}

}
