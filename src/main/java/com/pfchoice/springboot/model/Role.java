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
@Entity(name = "role")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Role extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "Id", nullable = false)
	private Integer id;

	@Column(name = "role")
	private String role;

	/*
	 * @JsonIgnore
	 * 
	 * @ManyToMany(fetch = FetchType.LAZY)
	 * 
	 * @JoinTable(name = "user_roles", joinColumns = {
	 * 
	 * @JoinColumn(name = "role_id", referencedColumnName = "id",nullable =
	 * false, updatable = false) }, inverseJoinColumns = {
	 * 
	 * @JoinColumn(name = "user_id", referencedColumnName = "id",nullable =
	 * false, updatable = false) }) private Set<User> users;
	 */
	/**
	 * 
	 */
	public Role() {
		super();
	}

	/**
	 * @param id
	 */
	public Role(final Integer id) {
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
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the user
	 */
	/*
	 * public Set<User> getUsers() { return users; }
	 */
	/**
	 * @param user
	 *            the user to set
	 */
	/*
	 * public void setUsers(Set<User> users) { this.users = users; }
	 */

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Role)) {
			return false;
		}
		Role other = (Role) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Role[ id=" + id + " ]";
	}

}
