package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author SarathGandluri
 */
@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "provider")
@FilterDefs({
@FilterDef(name="insuranceFilter", defaultCondition="FIND_IN_SET(insurance_id,:insIds)" , parameters = { @ParamDef(name = "insIds", type = "text") })
})
public class Provider extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "code")
	private String code;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "prvdr_Id", nullable = false)
	private Integer id;

	@Column(name = "name")
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "prvdr", fetch = FetchType.LAZY)
	@OrderBy(clause = "insurance_id asc")
	@OrderColumn(name = "insurance_id")
	@Filter(name="insuranceFilter")
	private Set<ProviderReferenceContract> prvdrRefContracts;

	@OneToOne(optional = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "reference_contacts", joinColumns = {
			@JoinColumn(name = "prvdr_id", referencedColumnName = "prvdr_id", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "cnt_id", referencedColumnName = "cnt_id", nullable = false, unique = true) })
	private Contact contact;

	
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

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the prvdrRefcontracts
	 */
	public Set<ProviderReferenceContract> getPrvdrRefContracts() {
		return prvdrRefContracts;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @param id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param prvdrRefcontracts
	 *            the prvdrRefcontracts to set
	 */
	public void setPrvdrRefContracts(Set<ProviderReferenceContract> prvdrRefContracts) {
		this.prvdrRefContracts = prvdrRefContracts;
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
