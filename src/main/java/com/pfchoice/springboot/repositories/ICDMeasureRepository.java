package com.pfchoice.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.ICDMeasure;

@Repository
public interface ICDMeasureRepository
		extends PagingAndSortingRepository<ICDMeasure, Integer>, JpaSpecificationExecutor<ICDMeasure> {

	public ICDMeasure findById(Integer id);

	/**
	 * 
	 * @param code
	 * @return
	 */
	ICDMeasure findByCodeIgnoreCase(String code);

	/**
	 * 
	 * @param code
	 * @return
	 */
	@Query("SELECT i FROM ICDMeasure i WHERE LOWER(i.code) in ( :icdCodes)")
	List<ICDMeasure> findByCodes(@Param("icdCodes") String[] icdCodes);
}
