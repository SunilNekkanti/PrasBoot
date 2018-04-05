package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pfchoice.springboot.util.JsonDateDeserializer;
import com.pfchoice.springboot.util.JsonDateSerializer;

/**
 *
 * @author SarathGandluri
 */
@Entity(name = "contract")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class ProviderContract extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "contract_Id", nullable = false)
	private Integer id;

	@Column(name = "contract_NBR")
	private String contractNBR;

	@Column(name = "pcp_provider_nbr")
	private String pcpPrvdrNBR;

	@Column(name = "pmpm")
	private Double pmpm;

	@Column(nullable = true, name = "avg_service_fund")
	private Double avgServiceFund;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "start_date")
	private Date startDate;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	@Column(name = "end_date")
	private Date endDate;

	@JsonIgnore
	@OneToOne(mappedBy = "contract", fetch = FetchType.LAZY)
	private ProviderReferenceContract referenceContract;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_upload_id", referencedColumnName = "file_upload_id")
	private FileUpload fileUpload;

	/**
	 * 
	 */
	public ProviderContract() {
		super();
	}

	/**
	 * @param id
	 */
	public ProviderContract(final Integer id) {
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
	 * @return the contractNBR
	 */
	public String getContractNBR() {
		return contractNBR;
	}

	/**
	 * @return the pcpPrvdrNBR
	 */
	public String getPcpPrvdrNBR() {
		return pcpPrvdrNBR;
	}

	/**
	 * @param pcpPrvdrNBR
	 *            the pcpPrvdrNBR to set
	 */
	public void setPcpPrvdrNBR(String pcpPrvdrNBR) {
		this.pcpPrvdrNBR = pcpPrvdrNBR;
	}

	/**
	 * @param contractNBR
	 *            the contractNBR to set
	 */
	public void setContractNBR(final String contractNBR) {
		this.contractNBR = contractNBR;
	}

	/**
	 * @return the pmpm
	 */
	public Double getPmpm() {
		return pmpm;
	}

	/**
	 * @param pmpm
	 *            the pmpm to set
	 */
	public void setPmpm(Double pmpm) {
		this.pmpm = pmpm;
	}

	/**
	 * @return the avgServiceFund
	 */
	public Double getAvgServiceFund() {
		return avgServiceFund;
	}

	/**
	 * @param avgServiceFund
	 *            the avgServiceFund to set
	 */
	public void setAvgServiceFund(Double avgServiceFund) {
		this.avgServiceFund = avgServiceFund;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the referenceContract
	 */
	public ProviderReferenceContract getReferenceContract() {
		return referenceContract;
	}

	/**
	 * @param referenceContract
	 *            the referenceContract to set
	 */
	public void setReferenceContract(ProviderReferenceContract referenceContract) {
		this.referenceContract = referenceContract;
	}

	/**
	 * @param fileUpload
	 *            the fileUpload to set
	 */
	public void setFileUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	/**
	 * @return the filesUpload
	 */
	public FileUpload getFileUpload() {
		return fileUpload;
	}

	/**
	 * @param filesUpload
	 *            the filesUpload to set
	 */
	public void setFilesUpload(FileUpload fileUpload) {
		this.fileUpload = fileUpload;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ProviderContract)) {
			return false;
		}
		ProviderContract other = (ProviderContract) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.Contract[ id=" + id + " ]";
	}

}
