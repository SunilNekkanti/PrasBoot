package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@Table(name = "event_assignment")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class EventAssignment extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "event_assignment_id", nullable = false)
	private Integer id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", referencedColumnName = "event_id")
	private Event event;

	@Column(name = "repeat_rule")
	private String repeatRule;

	@JsonSerialize(using = JsonDateAndTimeSerializer.class)
	@JsonDeserialize(using = JsonDateAndTimeDeserializer.class)
	@Column(name = "event_date_starttime", nullable = true)
	private Date eventDateStartTime;

	@JsonSerialize(using = JsonDateAndTimeSerializer.class)
	@JsonDeserialize(using = JsonDateAndTimeDeserializer.class)
	@Column(name = "event_date_endtime", nullable = true)
	private Date eventDateEndTime;

	@Fetch(FetchMode.SELECT)
	@ManyToMany( fetch = FetchType.LAZY)
	@JoinTable(name = "event_assignment_representatives", joinColumns = {
			@JoinColumn(name = "event_assignment_id", referencedColumnName = "event_assignment_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) })
	private Set<User> representatives;

	/**
	 * 
	 */
	public EventAssignment() {
		super();
	}

	/**
	 * @param id
	 */
	public EventAssignment(final Integer id) {
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
	 * @return the repeatRule
	 */
	public String getRepeatRule() {
		return repeatRule;
	}

	/**
	 * @param repeatRule
	 *            the repeatRule to set
	 */
	public void setRepeatRule(String repeatRule) {
		this.repeatRule = repeatRule;
	}

	/**
	 * @return the representatives
	 */
	public Set<User> getRepresentatives() {
		return representatives;
	}

	/**
	 * @param representatives
	 *            the representatives to set
	 */
	public void setRepresentatives(Set<User> representatives) {
		this.representatives = representatives;
	}

	/**
	 * @return the event
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(Event event) {
		this.event = event;
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

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EventAssignment)) {
			return false;
		}
		EventAssignment other = (EventAssignment) object;
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
		return "com.pfchoice.springboot.model.EventAssignment[ id=" + id + " ]";
	}

}
