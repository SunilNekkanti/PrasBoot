package com.pfchoice.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.MedicalLossRatio;

@Repository
public interface MedicalLossRatioRepository
		extends JpaRepository<MedicalLossRatio, Integer>, JpaSpecificationExecutor<MedicalLossRatio> {

}
