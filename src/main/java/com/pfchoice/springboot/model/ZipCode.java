package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "lu_state_zip")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ZipCode extends RecordDetails implements Serializable, FieldHandled {

	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "zipcode", nullable = false)
	private Integer code;

	@JsonIgnore
	@LazyToOne(LazyToOneOption.NO_PROXY)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "statecode", referencedColumnName = "code")
	private State stateCode;

	@JsonIgnore
	private FieldHandler fieldHandler;

	/**
	 * 
	 */
	public ZipCode() {
		super();
	}

	/**
	 * @param code
	 */
	public ZipCode(final Integer code) {
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
	 * @return the stateCode
	 */
	public State getStateCode() {
		if (fieldHandler != null) {
			return (State) fieldHandler.readObject(this, "stateCode", stateCode);
		}
		return stateCode;
	}

	/**
	 * @param stateCode
	 *            the stateCode to set
	 */
	public void setStateCode(final State stateCode) {
		if (fieldHandler != null) {
			this.stateCode = (State) fieldHandler.writeObject(this, "stateCode", this.stateCode, stateCode);
			return;
		}
		this.stateCode = stateCode;
	}

	@Override
	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	@Override
	public FieldHandler getFieldHandler() {
		return fieldHandler;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (code != null ? code.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ZipCode)) {
			return false;
		}
		ZipCode other = (ZipCode) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.ZipCode[ code=" + code + " ]";
	}

}
