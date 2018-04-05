package com.pfchoice.springboot.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MembershipProblem;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface MembershipProblemRepository
		extends PagingAndSortingRepository<MembershipProblem, Integer>, JpaSpecificationExecutor<MembershipProblem> , RecordDetailsAwareRepository<MembershipProblem, Integer> {

	public MembershipProblem findById(Integer id);
	
	@Query(value="SELECT mp FROM MembershipProblem mp  WHERE mp.icdMeasure.code = :icdcode  ")
	public MembershipProblem findByICDMeasureCode(@Param("icdcode") String icdcode);
	
	@Query(value="SELECT mp FROM MembershipProblem mp  WHERE mp.icdMeasure.code = :icdcode and mp.resolvedDate is null and mp.mbr.id = :mbrId ")
	public List<MembershipProblem> isMembershipProblemExist(@Param("icdcode") String icdcode,@Param("mbrId") Integer mbrId);

	
}
