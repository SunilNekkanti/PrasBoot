package com.pfchoice.springboot.repositories.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.PlaceOfService;

public class PlaceOfServiceSpecifications implements Specification<PlaceOfService> {

	private String searchTerm;

	public PlaceOfServiceSpecifications(String searchTerm) {
		super();
		this.searchTerm = searchTerm;
	}

	public Predicate toPredicate(Root<PlaceOfService> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);
		cq.distinct(true);

		Predicate p = cb.conjunction();
		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions()
					.add(cb.or(cb.like(cb.lower(root.get("code")), containsLikePattern),
							cb.like(cb.lower(root.get("description")), containsLikePattern),
							cb.like(cb.lower(root.get("name")), containsLikePattern)));
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