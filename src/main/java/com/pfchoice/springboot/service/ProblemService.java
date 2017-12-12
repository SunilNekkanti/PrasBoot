package com.pfchoice.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Problem;

public interface ProblemService {

	Problem findById(Integer id);

	Problem findByDescription(String description);

	void saveProblem(Problem problem);

	void updateProblem(Problem problem);

	void deleteProblemById(Integer id);

	void deleteAllProblems();

	List<Problem> findAllProblems();

	Page<Problem> findAllProblemsByPage(Specification<Problem> spec, Pageable pageable);

	boolean isProblemExist(Problem problem);
}