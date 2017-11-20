package com.pfchoice.springboot.service.impl;

import java.util.List;

import com.pfchoice.springboot.model.Problem;
import com.pfchoice.springboot.repositories.ProblemRepository;
import com.pfchoice.springboot.service.ProblemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("problemService")
@Transactional
public class ProblemServiceImpl implements ProblemService {

	@Autowired
	private ProblemRepository problemRepository;

	public Problem findById(Integer id) {
		return problemRepository.findOne(id);
	}

	public Problem findByDescription(String description) {
		return problemRepository.findByDescription(description);
	}
	
	public void saveProblem(Problem problem) {
		problemRepository.save(problem);
	}

	public void updateProblem(Problem problem) {
		saveProblem(problem);
	}

	public void deleteProblemById(Integer id) {
		problemRepository.delete(id);
	}

	public void deleteAllProblems() {
		problemRepository.deleteAll();
	}

	public List<Problem> findAllProblems() {
		return (List<Problem>) problemRepository.findAll();
	}

	public Page<Problem> findAllProblemsByPage(Specification<Problem> spec, Pageable pageable) {
		return problemRepository.findAll(spec, pageable);
	}

	public boolean isProblemExist(Problem problem) {
		return findById(problem.getId()) != null;
	}

}
