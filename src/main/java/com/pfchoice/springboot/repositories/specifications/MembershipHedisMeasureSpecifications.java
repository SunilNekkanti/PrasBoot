
package com.pfchoice.springboot.repositories.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipHedisMeasure;

public class MembershipHedisMeasureSpecifications implements Specification<MembershipHedisMeasure> {

	private String searchTerm;

	private Integer mbrId;

	private List<Integer> hedisRules;

	public MembershipHedisMeasureSpecifications(String searchTerm, Integer mbrId, List<Integer> hedisRules) {
		super();
		this.searchTerm = searchTerm;
		this.mbrId = mbrId;
		this.hedisRules = hedisRules;

	}

	public Predicate toPredicate(Root<MembershipHedisMeasure> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);

		cq.distinct(true);
		Predicate p = cb.conjunction();

		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(root.get("firstName")), containsLikePattern),
					cb.like(cb.lower(root.get("lastName")), containsLikePattern)));
		}

		if (mbrId != null) {
			p.getExpressions().add(cb.and(root.join("mbr").get("id").in(mbrId)));
		}

		if (hedisRules != null && hedisRules.size() > 0) {
			p.getExpressions().add(cb.and(root.join("hedisMeasureRule").get("id").in(hedisRules)));
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