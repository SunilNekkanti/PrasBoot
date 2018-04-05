package com.pfchoice.springboot.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.Membership;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;
import com.pfchoice.springboot.repositories.intf.ReportMonthAwareRepository;

@Repository
public interface MembershipRepository
		extends PagingAndSortingRepository<Membership, Integer>, JpaSpecificationExecutor<Membership>, RecordDetailsAwareRepository<Membership, Integer> 
, ReportMonthAwareRepository<Membership, Integer>{

	public Membership findById(Integer id);

	public Membership findByFirstName(String name);

	public Membership findByLastName(String name);

	public Membership findByDob(Date dob);

	@Query(value = "SELECT count(*)>0 FROM  csv2table_cap_report ", nativeQuery = true)
	public int isDataExistsInTable();

	@Query(value = "truncate table csv2table_cap_report ", nativeQuery = true)
	public int unloadCSV2Table();
}
