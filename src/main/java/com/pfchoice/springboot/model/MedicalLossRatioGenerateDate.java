package com.pfchoice.springboot.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Component
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MedicalLossRatioGenerateDate implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long count;

	private Integer insId;

	private Integer prvdrId;

	private Integer reportMonth;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * @return the insId
	 */
	public Integer getInsId() {
		return insId;
	}

	/**
	 * @param insId
	 *            the insId to set
	 */
	public void setInsId(Integer insId) {
		this.insId = insId;
	}

	/**
	 * @return the prvdrId
	 */
	public Integer getPrvdrId() {
		return prvdrId;
	}

	/**
	 * @param prvdrId
	 *            the prvdrId to set
	 */
	public void setPrvdrId(Integer prvdrId) {
		this.prvdrId = prvdrId;
	}

	/**
	 * @return the reportMonth
	 */
	public Integer getReportMonth() {
		return reportMonth;
	}

	/**
	 * @param reportDate
	 *            the reportDate to set
	 */
	public void setReportMonth(Integer reportMonth) {
		this.reportMonth = reportMonth;
	}

}
