package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.RiskRecon;
import com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository;

@Repository
public interface RiskReconRepository extends JpaRepository<RiskRecon, Integer>, RecordDetailsAwareRepository<RiskRecon, Integer>  {

	public RiskRecon findByName(String name);

}
