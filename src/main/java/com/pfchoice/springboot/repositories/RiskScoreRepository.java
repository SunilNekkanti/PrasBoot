package com.pfchoice.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.RiskScore;

@Repository
public interface RiskScoreRepository
		extends PagingAndSortingRepository<RiskScore, Integer>, JpaSpecificationExecutor<RiskScore> ,RiskScoreRepositoryCustom {

	@Query(value="select distinct cast(effective_year as char) from hcc order by effective_year desc", nativeQuery= true)
	public List<String> findAllPaymentYears();
	
	
	@Procedure(name = "calculateRiskScore")
	public List<RiskScore> calculateRiskScore(@Param("icdcodes") String icdcodes);
}
