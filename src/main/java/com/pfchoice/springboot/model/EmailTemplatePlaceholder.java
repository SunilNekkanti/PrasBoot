package com.pfchoice.springboot.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author SarathGandluri
 */
@Entity
@Table(name = "email_template_placeholders")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class EmailTemplatePlaceholder extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", nullable = false)
	private Integer id;

	
	@Column(name = "description")
	private String description;

	
	@Column(name = "sql_script", length = 65535, columnDefinition = "TEXT")
	private String sqlScript;

	
	@Column(name = "attachment_flag", insertable = false)
	private Character attachmentFlag;

	
	@Column(name = "order_no")
	private Integer orderNo;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "email_temp_id", nullable = false, referencedColumnName = "email_temp_id")
	private EmailTemplate emailTemplate;

	/**
	 * 
	 */
	public EmailTemplatePlaceholder() {
		super();
	}

	/**
	 * @param id
	 */
	public EmailTemplatePlaceholder(final Integer id) {
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the sqlScript
	 */
	public String getSqlScript() {
		return sqlScript;
	}

	/**
	 * @param sqlScript
	 *            the sqlScript to set
	 */
	public void setSqlScript(String sqlScript) {
		this.sqlScript = sqlScript;
	}

	/**
	 * @return the attachmentFlag
	 */
	public Character getAttachmentFlag() {
		return attachmentFlag;
	}

	/**
	 * @param attachmentFlag
	 *            the attachmentFlag to set
	 */
	public void setAttachmentFlag(Character attachmentFlag) {
		this.attachmentFlag = attachmentFlag;
	}

	/**
	 * @return the orderNo
	 */
	public Integer getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo
	 *            the orderNo to set
	 */
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the emailTemplate
	 */
	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	/**
	 * @param emailTemplate
	 *            the emailTemplate to set
	 */
	public void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EmailTemplatePlaceholder)) {
			return false;
		}
		EmailTemplatePlaceholder other = (EmailTemplatePlaceholder) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.EmailTemplatePlaceholder[ id=" + id + " ]";
	}

}
