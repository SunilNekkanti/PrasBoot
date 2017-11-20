package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author SarathGandluri
 */
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "provider")
public class Provider extends RecordDetails implements Serializable, FieldHandled {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "prvdr_Id", nullable = false)
	private Integer id;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinTable(name = "provider_languages", joinColumns = {
			@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "language_id", referencedColumnName = "code", nullable = false, updatable = false) })
	public Set<Language> languages;

	@OneToOne(cascade =   CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "reference_contacts", joinColumns = {
			@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id", nullable = false ) }, inverseJoinColumns = {
					@JoinColumn(name = "cnt_id", referencedColumnName = "cnt_id", nullable = false, unique = true) })
	private Contact contact;
	
	@Fetch(FetchMode.JOIN)
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REMOVE },fetch = FetchType.LAZY, mappedBy = "prvdr")
	@Where(clause = "prvdr_id is not null and insurance_id is null")
	private Set<ReferenceContract> refContracts = new HashSet<>();
	
	@Fetch(FetchMode.JOIN)
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REMOVE },fetch = FetchType.LAZY, mappedBy = "prvdr")
	@Where(clause = "prvdr_id is not null  and insurance_id is not null")
	private Set<ReferenceContract> refInsContracts = new HashSet<>();
	
	@Transient
	private Set<Insurance> insurances;
	
	
	@JsonIgnore
	private FieldHandler fieldHandler;
	
	/**
	 * 
	 */
	public Provider() {
		super();

	}

	/**
	 * @param id
	 */
	public Provider(final Integer id) {
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(final String code) {
		this.code = code;
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
	 * @return the languages
	 */
	public Set<Language> getLanguages() {
		return languages;
	}

	/**
	 * @param languages
	 *            the languages to set
	 */
	public void setLanguages(Set<Language> languages) {
		this.languages = languages;
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

	/* (non-Javadoc)
	 * @see org.hibernate.bytecode.internal.javassist.FieldHandled#setFieldHandler(org.hibernate.bytecode.internal.javassist.FieldHandler)
	 */
	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.bytecode.internal.javassist.FieldHandled#getFieldHandler()
	 */
	public FieldHandler getFieldHandler() { return fieldHandler;
	}
	
	/**
	 * @return the refContracts
	 */
	public Set<ReferenceContract> getRefContracts() {
			return refContracts;
	}

	/**
	 * @param refContracts the refContracts to set
	 */
	public void setRefContracts(Set<ReferenceContract> refContracts) {
		this.refContracts = refContracts;
	}
	
	/**
	 * @return the refInsContracts
	 */
	public Set<ReferenceContract> getRefInsContracts() {
		return refInsContracts;
	}

	/**
	 * @param refInsContracts the refInsContracts to set
	 */
	public void setRefInsContracts(Set<ReferenceContract> refInsContracts) {
		this.refInsContracts = refInsContracts;
	}

	/**
	 * @return the insurances
	 */
	public Set<Insurance> getInsurances() {
		return  refContracts.stream().map(rfc -> rfc.getIns()).collect(Collectors.toSet());
	}

	/**
	 * @param insurances the insurances to set
	 */
	public void setInsurances(Set<Insurance> insurances) {
		this.insurances = insurances;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Provider)) {
			return false;
		}
		Provider other = (Provider) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		result.append(this.getClass().getName());
		result.append(" Object {");
		result.append("\n");

		// determine fields declared in this class only (no fields of
		// superclass)
		Field[] fields = this.getClass().getDeclaredFields();

		// print field names paired with their values
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName()))
				continue;
			result.append("  ");
			try {
				result.append(field.getName());
				result.append(": ");
				// requires access to private field:
				result.append(field.get(this));
			} catch (IllegalAccessException ex) {
				System.out.println(ex);
			}
			result.append("\n");
		}
		result.append("}");

		return result.toString();
	}

}
