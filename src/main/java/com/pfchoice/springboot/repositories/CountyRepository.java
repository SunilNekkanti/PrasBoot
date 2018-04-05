package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.County;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface CountyRepository extends JpaRepository<County, Integer>, RecordDetailsAwareRepository<County, Integer> {

	public County findByDescription(String code);
}
