package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.NewMedicalLossRatio;

public interface NewMedicalLossRatioService {

	void saveNewMedicalLossRatio(NewMedicalLossRatio medicalLossRatio);

	void updateNewMedicalLossRatio(NewMedicalLossRatio medicalLossRatio);

	void deleteNewMedicalLossRatioById(Integer id);

	void deleteAllNewMedicalLossRatios();

	List<NewMedicalLossRatio> findAllNewMedicalLossRatios(Integer insId, List<Integer> prvdrIds,
			List<Integer> reportMonths);

	Page<NewMedicalLossRatio> findSummary(Integer insId, List<Integer> prvdrIds, List<Integer> reportMonths,
			Pageable Page);

	Page<NewMedicalLossRatio> findAllNewMedicalLossRatios(Specification<NewMedicalLossRatio> spec, Pageable Page);

}