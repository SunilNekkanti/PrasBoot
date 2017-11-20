package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.RiskRecon;

@Repository
public interface RiskReconRepository extends JpaRepository<RiskRecon, Integer> {

	public RiskRecon findByName(String name);

}
