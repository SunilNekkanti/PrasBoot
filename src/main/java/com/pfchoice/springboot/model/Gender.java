package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "lu_gender")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Gender extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "gender_id", nullable = false)
	private Byte id;

	@Column(name = "code")
	private char code;

	@Column(name = "description")
	private String description;

	/**
	 * 
	 */
	public Gender() {
		super();
	}

	/**
	 * 
	 * @param id
	 */
	public Gender(final Byte id) {
		super();
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public Byte getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(final Byte id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public char getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(final char code) {
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
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Gender)) {
			return false;
		}
		Gender other = (Gender) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Gender[ id=" + id + " ]";
	}

}
