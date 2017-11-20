package com.pfchoice.springboot.repositories.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Insurance;

public class InsuranceSpecifications implements Specification<Insurance> {

	private String searchTerm;

	public InsuranceSpecifications(String searchTerm) {
		super();
		this.searchTerm = searchTerm;
	}

	public Predicate toPredicate(Root<Insurance> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		
		String containsLikePattern = getContainsLikePattern(searchTerm);
		cq.distinct(true);

		Predicate p = cb.conjunction();
		
		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(root.get("name")), containsLikePattern)));
		}
		
		p.getExpressions().add(cb.and(cb.equal(root.get("activeInd"), 'Y')));
		p.getExpressions().add(cb.and(cb.isNull(root.join("contract").join("referenceContract").join("prvdr",JoinType.LEFT).get("id"))));
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