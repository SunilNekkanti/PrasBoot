package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.MedicalLossRatio;
import com.pfchoice.springboot.repositories.MedicalLossRatioRepository;
import com.pfchoice.springboot.service.MedicalLossRatioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("medicalLossRatioService")
@Transactional
public class MedicalLossRatioServiceImpl implements MedicalLossRatioService {

	@Autowired
	private MedicalLossRatioRepository medicalLossRatioRepository;

	public void saveMedicalLossRatio(MedicalLossRatio medicalLossRatio) {
		medicalLossRatioRepository.save(medicalLossRatio);
	}

	public void updateMedicalLossRatio(MedicalLossRatio medicalLossRatio) {
		saveMedicalLossRatio(medicalLossRatio);
	}

	public void deleteMedicalLossRatioById(Integer id) {
		medicalLossRatioRepository.delete(id);
	}

	public void deleteAllMedicalLossRatios() {
		medicalLossRatioRepository.deleteAll();
	}

	public List<MedicalLossRatio> findAllMedicalLossRatios(Specification<MedicalLossRatio> spec) {
		return (List<MedicalLossRatio>) medicalLossRatioRepository.findAll(spec);
	}

}
