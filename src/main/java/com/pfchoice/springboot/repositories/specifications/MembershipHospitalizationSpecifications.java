
package com.pfchoice.springboot.repositories.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.MembershipHospitalization;
import com.pfchoice.springboot.model.Provider;

public class MembershipHospitalizationSpecifications implements Specification<MembershipHospitalization> {

	private String searchTerm;

	private List<Integer> insIds;
	
	private List<Integer> prvdrIds;


	public MembershipHospitalizationSpecifications(String searchTerm, List<Integer> insIds,List<Integer> prvdrIds) {
		super();
		this.searchTerm = searchTerm;
		this.insIds = insIds;
		this.prvdrIds = prvdrIds;

	}

	public Predicate toPredicate(Root<MembershipHospitalization> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);

		Join<MembershipHospitalization, Provider> prvdr =  root.join("prvdr") ;
		
		cq.distinct(true);
		Predicate p = cb.conjunction();

		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(root.join("mbr").get("firstName")), containsLikePattern),
					cb.like(cb.lower(root.join("mbr").get("lastName")), containsLikePattern),
					cb.like(cb.lower(prvdr.get("name")), containsLikePattern),
					cb.like(cb.lower(root.get("facilityName")), containsLikePattern),
					cb.like(cb.lower(root.get("erNoOfVisits").as(String.class)), containsLikePattern),
					cb.like(cb.lower(root.get("erVisitDate").as(String.class)), containsLikePattern),
					cb.like(cb.lower(root.get("erLastVisitDate").as(String.class)), containsLikePattern),
					cb.like(cb.lower(root.get("potentiallyAvoidableERVisit")), containsLikePattern),
					cb.like(cb.lower(root.get("primaryDiagnosis")), containsLikePattern)
					));
		}

		if (insIds != null && insIds.size() > 0) {
			p.getExpressions().add(cb.and(root.join("ins").get("id").in(insIds)));
		}

		if (prvdrIds != null && prvdrIds.size() > 0) {
			p.getExpressions().add(cb.and(prvdr.get("id").in(prvdrIds)));
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