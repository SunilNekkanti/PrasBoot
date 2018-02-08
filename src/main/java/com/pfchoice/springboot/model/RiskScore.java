package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author sarath
 */
@Entity
@Table(name = "hcc")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name = "calculateRiskScore", procedureName = "CALCULATE_RISK_SCORE", parameters = {
			@StoredProcedureParameter(name = "icdcodes", type = String.class),
			@StoredProcedureParameter(name = "instOrComm", type = String.class),
			@StoredProcedureParameter(name = "effYear", type = Integer.class),
			@StoredProcedureParameter(name = "sex", type = String.class),
			@StoredProcedureParameter(name = "dob", type = String.class),
			@StoredProcedureParameter(name = "isMedicaid", type = String.class),
			@StoredProcedureParameter(name = "isAgedOrDisabled", type = String.class),
			@StoredProcedureParameter(name = "isOriginallyDisabled", type = String.class) }) })

public class RiskScore  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "code" )
	private Integer code;

	@Column(name = "description")
	private String description;

	@Transient
	private String icdCode;
	
	@Column(name = "community_raf" )
	private BigDecimal commRAF ;
	
	@Column(name = "inst_raf" )
	private BigDecimal instRAF ;
	
	@Column(name = "effective_year" )
	private Integer effectiveYear;
	
	@Column(name = "override" )
	private String override ;

	/**
	 * 
	 */
	public RiskScore() {
		super();
	}

	/**
	 * @param id
	 */
	public RiskScore(final Integer code) {
		super();
		this.code = code;
	}
	
	public RiskScore(final Integer effectiveYear,final String icdCode, final Integer code,final String description,final String override ,final BigDecimal commRAF ) {
		super();
		this.effectiveYear = effectiveYear;
		this.icdCode = icdCode;
		this.code = code;
		this.description = description;
		this.override = override;
		this.commRAF = commRAF;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the icdCode
	 */
	public String getIcdCode() {
		return icdCode;
	}

	/**
	 * @param icdCode the icdCode to set
	 */
	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	/**
	 * @return the commRAF
	 */
	public BigDecimal getCommRAF() {
		return commRAF;
	}

	/**
	 * @param commRAF the commRAF to set
	 */
	public void setCommRAF(BigDecimal commRAF) {
		this.commRAF = commRAF;
	}

	/**
	 * @return the instRAF
	 */
	public BigDecimal getInstRAF() {
		return instRAF;
	}

	/**
	 * @param instRAF the instRAF to set
	 */
	public void setInstRAF(BigDecimal instRAF) {
		this.instRAF = instRAF;
	}

	/**
	 * @return the effectiveYear
	 */
	public Integer getEffectiveYear() {
		return effectiveYear;
	}

	/**
	 * @param effectiveYear the effectiveYear to set
	 */
	public void setEffectiveYear(Integer effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	/**
	 * @return the override
	 */
	public String getOverride() {
		return override;
	}

	/**
	 * @param override the override to set
	 */
	public void setOverride(String override) {
		this.override = override;
	}


	@Override
	public int hashCode() {
		int hash = 0;
		hash += (code != null ? code.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof RiskScore)) {
			return false;
		}
		RiskScore other = (RiskScore) object;
		if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.RiskScore[ code=" + code + " ]";
	}

}
