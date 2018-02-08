
package com.pfchoice.springboot.repositories.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Membership;

public class MembershipSpecifications implements Specification<Membership> {

	private String searchTerm;

	private Integer insId;

	private Integer prvdrId;

	private Integer effectiveYear;

	private List<Integer> problemIds;

	public MembershipSpecifications(String searchTerm, Integer insId, Integer prvdrId, Integer effectiveYear,
			List<Integer> problemIds) {
		super();
		this.searchTerm = searchTerm;
		this.insId = insId;
		this.prvdrId = prvdrId;
		this.effectiveYear = effectiveYear;
		this.problemIds = problemIds;

	}

	public Predicate toPredicate(Root<Membership> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);

		cq.distinct(true);
		Predicate p = cb.conjunction();

		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(root.get("firstName")), containsLikePattern),
					cb.like(cb.lower(root.get("lastName")), containsLikePattern),
					cb.like(cb.lower(root.join("status").get("description")), containsLikePattern)));
		}

		if (insId != null) {
			p.getExpressions().add(cb.and(cb.equal(root.join("mbrInsuranceList").join("insId").get("id"), insId)));
		}

		if (prvdrId != null) {
			p.getExpressions().add(cb.and(cb.equal(root.join("mbrProviderList").join("prvdr").get("id"), prvdrId)));
		}

		if (effectiveYear != null) {
			String effectiveYearLikePattern = getContainsLikePattern(effectiveYear.toString());
			p.getExpressions()
					.add(cb.and(cb.like(root.join("mbrActivityMonthList").get("activityMonth").as(String.class),
							effectiveYearLikePattern)));
		}

		if (problemIds != null && problemIds.size() > 0) {
			p.getExpressions().add(cb.and(root.join("mbrProblemList").get("pbm").in(problemIds)));
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