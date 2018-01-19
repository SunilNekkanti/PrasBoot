package com.pfchoice.springboot.service.impl;

import com.pfchoice.springboot.model.NewMedicalLossRatio;
import com.pfchoice.springboot.repositories.NewMedicalLossRatioRepository;
import com.pfchoice.springboot.service.NewMedicalLossRatioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("newMedicalLossRatioService")
@Transactional
public class NewMedicalLossRatioServiceImpl implements NewMedicalLossRatioService {

	@Autowired
	private NewMedicalLossRatioRepository medicalLossRatioRepository;

	public void saveNewMedicalLossRatio(NewMedicalLossRatio medicalLossRatio) {
		medicalLossRatioRepository.save(medicalLossRatio);
	}

	public void updateNewMedicalLossRatio(NewMedicalLossRatio medicalLossRatio) {
		saveNewMedicalLossRatio(medicalLossRatio);
	}

	public void deleteNewMedicalLossRatioById(Integer id) {
		medicalLossRatioRepository.delete(id);
	}

	public void deleteAllNewMedicalLossRatios() {
		medicalLossRatioRepository.deleteAll();
	}


	public Page<NewMedicalLossRatio> findAllNewMedicalLossRatios(Specification<NewMedicalLossRatio> spec,
			Pageable page) {
		return medicalLossRatioRepository.findAll(spec, page);
	}

	public Page<NewMedicalLossRatio> findSummary(Integer insId, List<Integer> prvdrIds, List<Integer> reportMonths,List<Integer> activityMonths,
			Pageable page) {
		return medicalLossRatioRepository.findSummary(insId, prvdrIds,  reportMonths, activityMonths, page);
	}

	public List<String> findAllReportingYears(){
		return medicalLossRatioRepository.findAllReportingYears();
	}
}
