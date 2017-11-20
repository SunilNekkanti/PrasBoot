package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "lu_state")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class State extends RecordDetails implements Serializable, FieldHandled {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "code", nullable = false)
	private Integer code;

	@Column(name = "description")
	private String description;

	@Column(name = "shot_name")
	private String shortName;

	@LazyCollection(LazyCollectionOption.EXTRA)
	@OneToMany(mappedBy = "stateCode", fetch = FetchType.LAZY)
	private Set<ZipCode> zipCodes;

	@JsonIgnore
	private FieldHandler fieldHandler;

	/**
	 * 
	 */
	public State() {
		super();
	}

	/**
	 * @param code
	 */
	public State(final Integer code) {
		super();
		this.code = code;
	}

	/**
	 * @return
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code
	 */
	public void setCode(final Integer code) {
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

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName
	 *            the shortName to set
	 */
	public void setShortName(final String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the zipCode
	 */
	@SuppressWarnings("unchecked")
	public Set<ZipCode> getZipCodes() {
		if (fieldHandler != null) {
			return (Set<ZipCode>) fieldHandler.readObject(this, "zipCodes", zipCodes);
		} else {
			return zipCodes;
		}

	}

	/**
	 * @param zipCode
	 *            the zipCode to set
	 */
	@SuppressWarnings("unchecked")
	public void setZipCodes(final Set<ZipCode> zipCodes) {
		if (fieldHandler != null) {
			this.zipCodes = (Set<ZipCode>) fieldHandler.writeObject(this, "zipCodes", this.zipCodes, zipCodes);
			return;
		}
		this.zipCodes = zipCodes;
	}

	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof State)) {
			return false;
		}
		State other = (State) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.State[ code=" + code + " ]";
	}

}
