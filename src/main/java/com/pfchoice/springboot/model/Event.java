package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pfchoice.springboot.util.JsonDateAndTimeDeserializer;
import com.pfchoice.springboot.util.JsonDateAndTimeSerializer;

/**
 * @author sarath
 *
 */
@Entity
@Table(name = "event")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Event extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "event_id", nullable = false)
	private Integer id;

	@Column(name = "event_name")
	private String eventName;

	@JsonSerialize(using = JsonDateAndTimeSerializer.class)
	@JsonDeserialize(using = JsonDateAndTimeDeserializer.class)
	@Column(name = "event_date_starttime", nullable = true)
	private Date eventDateStartTime;

	@JsonSerialize(using = JsonDateAndTimeSerializer.class)
	@JsonDeserialize(using = JsonDateAndTimeDeserializer.class)
	@Column(name = "event_date_endtime", nullable = true)
	private Date eventDateEndTime;

	@ManyToOne
	@JoinColumn(name = "facility_type_id", referencedColumnName = "code")
	private EventType eventType;

	@Column(name = "notes", length = 65535, columnDefinition = "TEXT")
	private String notes;

	@OneToOne(cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "event_contacts", joinColumns = {
			@JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "contact_id", referencedColumnName = "cnt_id", nullable = false, updatable = false, unique = true) })
	private Contact contact;

	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinTable(name = "event_files_upload", joinColumns = {
			@JoinColumn(name = "event_id", referencedColumnName = "event_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "files_upload_id", referencedColumnName = "file_upload_id", nullable = false, updatable = false) })
	private Set<FileUpload> attachments;

	@JsonIgnore
	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
	private Set<EventAssignment> eventAssignments;

	/**
	 * 
	 */
	public Event() {
		super();
	}

	/**
	 * @param id
	 */
	public Event(final Integer id) {
		super();
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the lastName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
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

	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the eventDateStartTime
	 */
	public Date getEventDateStartTime() {
		return eventDateStartTime;
	}

	/**
	 * @param eventDateStartTime
	 *            the eventDateStartTime to set
	 */
	public void setEventDateStartTime(Date eventDateStartTime) {
		this.eventDateStartTime = eventDateStartTime;
	}

	/**
	 * @return the eventDateEndTime
	 */
	public Date getEventDateEndTime() {
		return eventDateEndTime;
	}

	/**
	 * @param eventDateEndTime
	 *            the eventDateEndTime to set
	 */
	public void setEventDateEndTime(Date eventDateEndTime) {
		this.eventDateEndTime = eventDateEndTime;
	}

	/**
	 * @return the attachments
	 */
	public Set<FileUpload> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments
	 *            the attachments to set
	 */
	public void setAttachments(Set<FileUpload> attachments) {
		this.attachments = attachments;
	}

	/**
	 * @return the eventAssignments
	 */
	public Set<EventAssignment> getEventAssignments() {
		return eventAssignments;
	}

	/**
	 * @param eventAssignments
	 *            the eventAssignments to set
	 */
	public void setEventAssignments(Set<EventAssignment> eventAssignments) {
		this.eventAssignments = eventAssignments;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Event)) {
			return false;
		}
		Event other = (Event) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Event[ id=" + id + " ]";
	}

}
