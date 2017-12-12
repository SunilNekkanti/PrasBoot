package com.pfchoice.springboot.repositories.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Problem;

public class ProblemSpecifications implements Specification<Problem> {

	private String searchTerm;

	private Integer insId;

	private Integer effectiveYear;

	public ProblemSpecifications(String searchTerm, Integer insId, Integer effectiveYear) {
		super();
		this.searchTerm = searchTerm;
		this.insId = insId;
		this.effectiveYear = effectiveYear;
	}

	public Predicate toPredicate(Root<Problem> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);

		cq.distinct(true);
		Predicate p = cb.conjunction();

		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(root.get("firstName")), containsLikePattern),
					cb.like(cb.lower(root.get("lastName")), containsLikePattern)));
		}

		if (insId != null) {
			p.getExpressions().add(cb.and(cb.equal(root.join("insId").get("id"), insId)));
		}
		if (effectiveYear != null) {
			p.getExpressions().add(cb.and(cb.equal(root.get("effectiveYear"), effectiveYear)));
		}

		p.getExpressions().add(cb.and(cb.equal(root.get("activeInd"), 'Y')));
		return p;

	}

	private static String getContainsLikePattern(String searchTerm) {
		if (searchTerm == null || searchTerm.isEmpty()) {
			return "%";
		} else {
			return "%" + searchTerm.toLowerCase() + "%";
		}
	}
}