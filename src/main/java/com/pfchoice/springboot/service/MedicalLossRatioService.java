package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MedicalLossRatio;

public interface MedicalLossRatioService {

	void saveMedicalLossRatio(MedicalLossRatio medicalLossRatio);

	void updateMedicalLossRatio(MedicalLossRatio medicalLossRatio);

	void deleteMedicalLossRatioById(Integer id);

	void deleteAllMedicalLossRatios();

	List<MedicalLossRatio> findAllMedicalLossRatios(Specification<MedicalLossRatio> spec);


}