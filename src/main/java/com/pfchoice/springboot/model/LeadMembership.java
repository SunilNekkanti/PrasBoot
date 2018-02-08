package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pfchoice.springboot.util.JsonDateDeserializer;
import com.pfchoice.springboot.util.JsonDateSerializer;

/**
 * @author sarath
 *
 */
@Entity
@Table(name = "lead_membership")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class LeadMembership extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "lead_Mbr_id", nullable = false)
	private Integer id;

	@Column(name = "lead_Mbr_LastName")
	private String lastName;

	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "lead")
	private List<AgentLeadAppointment> agentLeadAppointmentList;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "insurance_type_id", referencedColumnName = "plan_type_id")
	private PlanType planType;

	@Column(name = "present_insurance")
	private String initialInsurance;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "lead_Mbr_DOB")
	private Date dob;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lead_Mbr_ethinic_code", referencedColumnName = "code")
	private Ethinicity ethinicCode;

	@Column(name = "file_id")
	private Integer fileId;

	@Column(name = "lead_Mbr_FirstName")
	private String firstName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lead_Mbr_GenderID", referencedColumnName = "gender_id")
	private Gender gender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lead_Mbr_languageId", referencedColumnName = "code")
	private Language language;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "best_time_to_call_id", referencedColumnName = "code")
	private BestTimeToCall bestTimeToCall;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lead_Mbr_Status", referencedColumnName = "code", insertable = false)
	private LeadStatus status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lead_mbr_status_detail_id", referencedColumnName = "code", insertable = false)
	private LeadStatusDetail statusDetail;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "lead_contacts", joinColumns = {
			@JoinColumn(name = "lead_mbr_id", referencedColumnName = "lead_mbr_id", nullable = false ) }, inverseJoinColumns = {
					@JoinColumn(name = "contact_id", referencedColumnName = "cnt_id", nullable = false, unique = true) })
	private Contact contact;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", referencedColumnName = "event_id")
	private Event event;

	@Column(name = "consent_form_signed")
	private Character consentFormSigned = new Character('N');

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_upload_id", referencedColumnName = "file_upload_id", nullable = false)
	private FileUpload fileUpload;

	@Fetch(FetchMode.SELECT)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lead")
	private List<LeadNotes> leadNotes;

	@javax.persistence.Transient
	private String notesHistory;

	/**
	 * 
	 */
	public LeadMembership() {
		super();
	}

	/**
	 * @param id
	 */
	public LeadMembership(final Integer id) {
		super();
		this.id = id;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof LeadMembership)) {
			return false;
		}
		LeadMembership other = (LeadMembership) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @return the ethinicCode
	 */
	public Ethinicity getEthinicCode() {
		return ethinicCode;
	}

	/**
	 * @return the fileId
	 */
	public Integer getFileId() {
		return fileId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the genderId
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the status
	 */
	public LeadStatus getStatus() {
		return status;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @param ethinicCode
	 *            the ethinicCode to set
	 */
	public void setEthinicCode(final Ethinicity ethinicCode) {
		this.ethinicCode = ethinicCode;
	}

	/**
	 * @param fileId
	 *            the fileId to set
	 */
	public void setFileId(final Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param genderId
	 *            the genderId to set
	 */
	public void setGender(final Gender gender) {
		this.gender = gender;
	}

	/**
	 * @param id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final LeadStatus status) {
		this.status = status;
	}

	/**
	 * @return the statusDetail
	 */
	public LeadStatusDetail getStatusDetail() {
		return statusDetail;
	}

	/**
	 * @param statusDetail the statusDetail to set
	 */
	public void setStatusDetail(LeadStatusDetail statusDetail) {
		this.statusDetail = statusDetail;
	}

	/**
	 * @return the language
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

	/**
	 * @return the bestTimeToCall
	 */
	public BestTimeToCall getBestTimeToCall() {
		return bestTimeToCall;
	}

	/**
	 * @param bestTimeToCall
	 *            the bestTimeToCall to set
	 */
	public void setBestTimeToCall(BestTimeToCall bestTimeToCall) {
		this.bestTimeToCall = bestTimeToCall;
	}

	/**
	 * @return the agentLeadAppointmentList
	 */
	public List<AgentLeadAppointment> getAgentLeadAppointmentList() {
		return agentLeadAppointmentList;
	}

	/**
	 * @param agentLeadAppointmentList
	 *            the agentLeadAppointmentList to set
	 */
	public void setAgentLeadAppointmentList(List<AgentLeadAppointment> agentLeadAppointmentList) {
		for (AgentLeadAppointment agentLeadAppointment : agentLeadAppointmentList) {
			agentLeadAppointment.setLead(this);
			agentLeadAppointment.setCreatedBy("Sarath");
			agentLeadAppointment.setUpdatedBy("Sarath");
		}
		this.agentLeadAppointmentList = agentLeadAppointmentList;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return this.contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
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
	 * @return the consentFormSigned
	 */
	public Character getConsentFormSigned() {
		return consentFormSigned;
	}

	/**
	 * @param consentFormSigned
	 *            the consentFormSigned to set
	 */
	public void setConsentFormSigned(Character consentFormSigned) {
		this.consentFormSigned = consentFormSigned;
	}

	/**
	 * @return the fileUpload
	 */
	public FileUpload getFileUpload() {
		return fileUpload;
	}

	/**
	 * @param fileUpload
	 *            the fileUpload to set
	 */
	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	/**
	 * @return the planType
	 */
	public PlanType getPlanType() {
		return planType;
	}

	/**
	 * @param planType
	 *            the planType to set
	 */
	public void setPlanType(PlanType planType) {
		this.planType = planType;
	}

	/**
	 * @return the presentInsurance
	 */
	public String getInitialInsurance() {
		return initialInsurance;
	}

	/**
	 * @param presentInsurance
	 *            the presentInsurance to set
	 */
	public void setInitialInsurance(String initialInsurance) {
		this.initialInsurance = initialInsurance;
	}

	/**
	 * @return the leadNotes
	 */
	public List<LeadNotes> getLeadNotes() {
		return leadNotes;
	}

	/**
	 * @param leadNotes
	 *            the leadNotes to set
	 */
	public void setLeadNotes(List<LeadNotes> leadNotes) {
		this.leadNotes = leadNotes;
	}

	/**
	 * @return the notesHistory
	 */
	public String getNotesHistory() {

		return leadNotes.stream().sorted(Comparator.comparing(LeadNotes::getCreatedDate).reversed())
				.map(LeadNotes::toString).collect(Collectors.joining(" "));

	}

	/**
	 * @param notesHistory
	 *            the notesHistory to set
	 */
	public void setNotesHistory(String notesHistory) {
		this.notesHistory = leadNotes.stream().map(ln -> ln.getNotes()).reduce("", String::concat);

	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.LeadMembership[ id=" + id + " ]";
	}

}
