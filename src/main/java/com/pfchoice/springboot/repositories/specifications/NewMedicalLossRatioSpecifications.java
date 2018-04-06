
package com.pfchoice.springboot.repositories.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.NewMedicalLossRatio;

public class NewMedicalLossRatioSpecifications implements Specification<NewMedicalLossRatio> {

	private String searchTerm;

	private Integer insId;

	private List<Integer> prvdrIds;

	private List<Integer> reportMonths;
	
	private List<Integer> activityMonths;

	public NewMedicalLossRatioSpecifications(String searchTerm, Integer insId, List<Integer> prvdrIds,
			List<Integer> reportMonths,List<Integer> activityMonths) {
		super();
		this.searchTerm = searchTerm;
		this.insId = insId;
		this.prvdrIds = prvdrIds;
		this.reportMonths = reportMonths;
		this.activityMonths = activityMonths;

	}

	public Predicate toPredicate(Root<NewMedicalLossRatio> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);
		cq.distinct(true);

		Predicate p = cb.conjunction();

		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(root.join("ins").get("name")), containsLikePattern),
					cb.like(cb.lower(root.join("prvdr").get("name")), containsLikePattern),
					cb.like(root.get("reportMonth").as(String.class), containsLikePattern),
					cb.like(root.get("activityMonth").as(String.class), containsLikePattern)));
			System.out.println("searchTerm" + searchTerm);

		}

		if (insId != null) {
			p.getExpressions().add(cb.and(cb.equal(root.join("ins").get("id"), insId)));
		}

		if (prvdrIds != null && prvdrIds.size() > 0) {
			p.getExpressions().add(cb.and(root.join("prvdr").get("id").in(prvdrIds)));
		}

		if (reportMonths != null && reportMonths.size() > 0) {
			p.getExpressions().add(cb.and(root.get("reportMonth").in(reportMonths)));
		}

		if (activityMonths != null && activityMonths.size() > 0) {
			p.getExpressions().add(cb.and(root.get("activityMonth").in(activityMonths)));
		}
		return p;

	}

	private static String getContainsLikePattern(String searchTerm) {
		if (searchTerm == null || searchTerm.isEmpty()) {
			return "%";
		} else {
			return "%" + searchTerm.toLowerCase() + "%";
		}
	}
	
	/**
	 * @return the reportMonths
	 */
	public List<Integer> getReportMonths() {
		return reportMonths;
	}

}