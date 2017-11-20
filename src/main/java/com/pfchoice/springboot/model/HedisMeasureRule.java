package com.pfchoice.springboot.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



/**
 *
 * @author sarath
 */
@Entity
@Table(name = "hedis_measure_rule")
@DynamicUpdate(value = true)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class HedisMeasureRule extends RecordDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "hedis_msr_rule_Id", nullable = false)
	private Integer id;

	
	@Column(name = "description")
	private String description;

	
	@Column(name = "short_description")
	private String shortDescription;

	
	@OneToOne(fetch = FetchType.LAZY)
	@Where(clause = "active_ind ='Y'")
	@JoinColumn(name = "ins_id", referencedColumnName = "Insurance_Id")
	private Insurance insId;

	
	@Column(name = "problem_flag")
	private Character problemFlag;

	
	@Column(name = "cpt_or_icd")
	private Byte cptOrIcd;

	
	@OneToOne
	@JoinColumn(name = "hedis_id", referencedColumnName = "qlty_msr_id")
	@Where(clause = "active_ind ='Y'")
	private HedisMeasure hedisMeasure;

	@Fetch(FetchMode.SELECT)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "hedis_cpt_measure", joinColumns = {
			@JoinColumn(name = "hedis_msr_rule_Id", referencedColumnName = "hedis_msr_rule_Id") }, inverseJoinColumns = {
					@JoinColumn(name = "cpt_id", referencedColumnName = "cpt_id") })
	@Where(clause = "active_ind ='Y'")
	private Set<CPTMeasure> cptCodes;

	
	@Fetch(FetchMode.SELECT)
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "hedis_icd_measure", joinColumns = {
			@JoinColumn(name = "hedis_msr_rule_Id", referencedColumnName = "hedis_msr_rule_Id") }, inverseJoinColumns = {
					@JoinColumn(name = "icd_id", referencedColumnName = "icd_id") })
	@Where(clause = "active_ind ='Y'")
	private Set<ICDMeasure> icdCodes;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "gender_id", referencedColumnName = "gender_id")
	@Where(clause = "active_ind ='Y'")
	private Gender genderId;

	
	@Column(nullable = true, name = "lower_age_limit")
	private BigDecimal lowerAgeLimit;

	
	@Column(nullable = true, name = "upper_age_limit")
	private BigDecimal upperAgeLimit;
	
	
	@Column(name = "dose_count")
	private Integer doseCount;

	@Column(name = "dose_1")
	private Integer dose1;
	
	
	@Column(name = "dose_2")
	private Integer dose2;
	
	
	@Column(name = "dose_3")
	private Integer dose3;
	
	
	@Column(name = "dose_4")
	private Integer dose4;
	
	
	@Column(name = "dose_5")
	private Integer dose5;
	
	
	@Column(name = "dose_6")
	private Integer dose6;
	
	
	@Column(name = "datepart_1")
	private String datepart1;
	
	
	@Column(name = "datepart_2")
	private String datepart2;
	
	
	@Column(name = "datepart_3")
	private String datepart3;
	
	
	@Column(name = "datepart_4")
	private String datepart4;
	
	
	@Column(name = "datepart_5")
	private String datepart5;
	
	
	@Column(name = "datepart_6")
	private String datepart6;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "frequency_type_code", referencedColumnName = "code")
	private FrequencyType frequencyType;

	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(nullable = true, name = "problem_id", referencedColumnName = "pbm_Id")
	private Problem pbm;

	
	@Temporal(TemporalType.DATE)
	@Column(nullable = true, name = "age_effective_from")
	private Date ageEffectiveFrom;

	
	@Temporal(TemporalType.DATE)
	@Column(nullable = true, name = "age_effective_to")
	private Date ageEffectiveTo;

	
	@Column(name = "effective_year")
	private Integer effectiveYear;

	
	@Transient
	private String hedisMeasureCode;

	
	@Transient
	private String cptMeasureCode;

	
	@Transient
	private String icdMeasureCode;

	
	@Transient
	private String genderDescription;

	
	@Transient
	private String problemDescription;
	
	
	@Transient
	private List<String> dateparts;
	
	/**
	 * 
	 */
	public HedisMeasureRule() {
		super();
	}

	/**
	 * @param id
	 */
	public HedisMeasureRule(final Integer id) {
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
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription
	 *            the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return the insId
	 */
	public Insurance getInsId() {
		return insId;
	}

	/**
	 * @param insId
	 *            the insId to set
	 */
	public void setInsId(Insurance insId) {
		this.insId = insId;
	}

	/**
	 * @return the problemFlag
	 */
	public Character getProblemFlag() {
		return problemFlag;
	}

	/**
	 * @param problemFlag
	 *            the problemFlag to set
	 */
	public void setProblemFlag(Character problemFlag) {
		this.problemFlag = problemFlag;
	}

	/**
	 * @return the cptOrIcd
	 */
	public Byte getCptOrIcd() {
		return cptOrIcd;
	}

	/**
	 * @param cptOrIcd
	 *            the cptOrIcd to set
	 */
	public void setCptOrIcd(Byte cptOrIcd) {
		this.cptOrIcd = cptOrIcd;
	}

	/**
	 * @return the hedisMeasure
	 */
	public HedisMeasure getHedisMeasure() {
		return hedisMeasure;
	}

	/**
	 * @param hedisMeasure
	 *            the hedisMeasure to set
	 */
	public void setHedisMeasure(final HedisMeasure hedisMeasure) {
		this.hedisMeasure = hedisMeasure;
	}

	/**
	 * @return the effectiveYear
	 */
	public Integer getEffectiveYear() {
		return effectiveYear;
	}

	/**
	 * @param effectiveYear
	 *            the effectiveYear to set
	 */
	public void setEffectiveYear(final Integer effectiveYear) {
		this.effectiveYear = effectiveYear;
	}

	/**
	 * @return the cptCodes
	 */
	public Set<CPTMeasure> getCptCodes() {
		return cptCodes;
	}

	/**
	 * @param cptCodes
	 *            the cptCodes to set
	 */
	public void setCptCodes(Set<CPTMeasure> cptCodes) {
		this.cptCodes = cptCodes;
	}

	/**
	 * @return the icdCodes
	 */
	public Set<ICDMeasure> getIcdCodes() {
		return icdCodes;
	}

	/**
	 * @param icdCodes
	 *            the icdCodes to set
	 */
	public void setIcdCodes(Set<ICDMeasure> icdCodes) {
		this.icdCodes = icdCodes;
	}

	/**
	 * @return the genderId
	 */
	public Gender getGenderId() {
		return genderId;
	}

	/**
	 * @param genderId
	 *            the genderId to set
	 */
	public void setGenderId(Gender genderId) {
		this.genderId = genderId;
	}

	/**
	 * @return the lowerAgeLimit
	 */
	public BigDecimal getLowerAgeLimit() {
		return lowerAgeLimit;
	}

	/**
	 * @param lowerAgeLimit
	 *            the lowerAgeLimit to set
	 */
	public void setLowerAgeLimit(BigDecimal lowerAgeLimit) {
		this.lowerAgeLimit = lowerAgeLimit;
	}

	/**
	 * @return the upperAgeLimit
	 */
	public BigDecimal getUpperAgeLimit() {
		return upperAgeLimit;
	}

	/**
	 * @param upperAgeLimit
	 *            the upperAgeLimit to set
	 */
	public void setUpperAgeLimit(BigDecimal upperAgeLimit) {
		this.upperAgeLimit = upperAgeLimit;
	}

	/**
	 * @return the doseCount
	 */
	public Integer getDoseCount() {
		return doseCount;
	}

	/**
	 * @param doseCount
	 *            the doseCount to set
	 */
	public void setDoseCount(Integer doseCount) {
		this.doseCount = doseCount;
	}

	/**
	 * @return the dose1
	 */
	public Integer getDose1() {
		return dose1;
	}

	/**
	 * @param dose1 the dose1 to set
	 */
	public void setDose1(Integer dose1) {
		this.dose1 = dose1;
	}

	/**
	 * @return the dose2
	 */
	public Integer getDose2() {
		return dose2;
	}

	/**
	 * @param dose2 the dose2 to set
	 */
	public void setDose2(Integer dose2) {
		this.dose2 = dose2;
	}

	/**
	 * @return the dose3
	 */
	public Integer getDose3() {
		return dose3;
	}

	/**
	 * @param dose3 the dose3 to set
	 */
	public void setDose3(Integer dose3) {
		this.dose3 = dose3;
	}

	/**
	 * @return the dose4
	 */
	public Integer getDose4() {
		return dose4;
	}

	/**
	 * @param dose4 the dose4 to set
	 */
	public void setDose4(Integer dose4) {
		this.dose4 = dose4;
	}

	/**
	 * @return the dose5
	 */
	public Integer getDose5() {
		return dose5;
	}

	/**
	 * @param dose5 the dose5 to set
	 */
	public void setDose5(Integer dose5) {
		this.dose5 = dose5;
	}

	/**
	 * @return
	 */
	public String getDatepart1() {
		return datepart1;
	}

	/**
	 * @param datepart1
	 */
	public void setDatepart1(String datepart1) {
		this.datepart1 = datepart1;
	}

	/**
	 * @return
	 */
	public String getDatepart2() {
		return datepart2;
	}

	/**
	 * @param datepart2
	 */
	public void setDatepart2(String datepart2) {
		this.datepart2 = datepart2;
	}

	/**
	 * @return
	 */
	public String getDatepart3() {
		return datepart3;
	}

	/**
	 * @param datepart3
	 */
	public void setDatepart3(String datepart3) {
		this.datepart3 = datepart3;
	}

	/**
	 * @return
	 */
	public String getDatepart4() {
		return datepart4;
	}

	/**
	 * @param datepart4
	 */
	public void setDatepart4(String datepart4) {
		this.datepart4 = datepart4;
	}

	/**
	 * @return
	 */
	public String getDatepart5() {
		return datepart5;
	}

	/**
	 * @param datepart5
	 */
	public void setDatepart5(String datepart5) {
		this.datepart5 = datepart5;
	}

	/**
	 * @return the frequencyType
	 */
	public FrequencyType getFrequencyType() {
		return frequencyType;
	}

	/**
	 * @param frequencyType
	 *            the frequencyType to set
	 */
	public void setFrequencyType(FrequencyType frequencyType) {
		this.frequencyType = frequencyType;
	}

	/**
	 * @return the pbm
	 */
	public Problem getPbm() {
		return pbm;
	}

	/**
	 * @param pbm
	 *            the pbm to set
	 */
	public void setPbm(Problem pbm) {
		this.pbm = pbm;
	}

	/**
	 * @return the ageEffectiveFrom
	 */
	public Date getAgeEffectiveFrom() {
		return ageEffectiveFrom;
	}

	/**
	 * @param ageEffectiveFrom
	 *            the ageEffectiveFrom to set
	 */
	public void setAgeEffectiveFrom(Date ageEffectiveFrom) {
		this.ageEffectiveFrom = ageEffectiveFrom;
	}

	/**
	 * @return the ageEffectiveTo
	 */
	public Date getAgeEffectiveTo() {
		return ageEffectiveTo;
	}

	/**
	 * @param ageEffectiveTo
	 *            the ageEffectiveTo to set
	 */
	public void setAgeEffectiveTo(Date ageEffectiveTo) {
		this.ageEffectiveTo = ageEffectiveTo;
	}

	/**
	 * @return the hedisMeasureCode
	 */
	public String getHedisMeasureCode() {
		return hedisMeasureCode;
	}

	/**
	 * @param hedisMeasureCode
	 *            the hedisMeasureCode to set
	 */
	public void setHedisMeasureCode(String hedisMeasureCode) {
		this.hedisMeasureCode = hedisMeasureCode;
	}
	
	
	/**
	 * @return the cptMeasureCode
	 */
	public String getCptMeasureCode() {
		return cptMeasureCode;
	}

	/**
	 * @param cptMeasureCode
	 *            the cptMeasureCode to set
	 */
	public void setCptMeasureCode(String cptMeasureCode) {
		this.cptMeasureCode = cptMeasureCode;
	}

	/**
	 * @return the icdMeasureCode
	 */
	public String getIcdMeasureCode() {
		return icdMeasureCode;
	}

	/**
	 * @param icdMeasureCode
	 *            the icdMeasureCode to set
	 */
	public void setIcdMeasureCode(String icdMeasureCode) {
		this.icdMeasureCode = icdMeasureCode;
	}

	/**
	 * @return the genderDescription
	 */
	public String getGenderDescription() {
		return genderDescription;
	}

	/**
	 * @param genderDescription
	 *            the genderDescription to set
	 */
	public void setGenderDescription(String genderDescription) {
		this.genderDescription = genderDescription;
	}

	/**
	 * @return the problemDescription
	 */
	public String getProblemDescription() {
		return problemDescription;
	}

	/**
	 * @param problemDescription
	 *            the problemDescription to set
	 */
	public void setProblemDescription(String problemDescription) {
		this.problemDescription = problemDescription;
	}

	/**
	 * @return the dose6
	 */
	public Integer getDose6() {
		return dose6;
	}

	/**
	 * @param dose6 the dose6 to set
	 */
	public void setDose6(Integer dose6) {
		this.dose6 = dose6;
	}

	/**
	 * @return the datepart6
	 */
	public String getDatepart6() {
		return datepart6;
	}

	/**
	 * @param datepart6 the datepart6 to set
	 */
	public void setDatepart6(String datepart6) {
		this.datepart6 = datepart6;
	}

	/**
	 * @return the dateparts
	 */
	public List<String> getDateparts() {
		return dateparts;
	}

	/**
	 * @param dateparts the dateparts to set
	 */
	public void setDateparts(List<String> dateparts) {
		this.dateparts = dateparts;
	}


	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof HedisMeasureRule)) {
			return false;
		}
		HedisMeasureRule other = (HedisMeasureRule) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.pfchoice.springboot.model.HedisMeasureRule[ id=" + id + " ]";
	}

}
