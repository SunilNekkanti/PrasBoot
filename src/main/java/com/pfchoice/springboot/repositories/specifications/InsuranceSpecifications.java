package com.pfchoice.springboot.repositories.specifications;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Insurance;

public class InsuranceSpecifications implements Specification<Insurance> {

	private String searchTerm;
	
	private String currentScreen = "Active";

	public InsuranceSpecifications(String searchTerm, String currentScreen) {
		super();
		this.searchTerm = searchTerm;
		this.currentScreen = currentScreen;
	}

	public Predicate toPredicate(Root<Insurance> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);
		cq.distinct(true);

		Predicate p = cb.conjunction();

		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(root.get("name")), containsLikePattern)));
		}

		p.getExpressions().add(cb.and(cb.equal(root.get("activeInd"), 'Y')));
		// p.getExpressions().add(cb.and(cb.isNull(root.join("contract").join("referenceContract").get("prvdr"))));
		

		Expression<Date> contractStartTime = root.join("contracts").get("startDate");
		Expression<Date> contractEndTime = root.join("contracts").get("endDate");

		if("Active".equals(this.currentScreen)){
			p.getExpressions().add(cb.and(cb.between(
					cb.function("date_format", Date.class, cb.literal(new Date()), cb.literal("%Y-%m-%d")),
					cb.function("date_format", Date.class, contractStartTime, cb.literal("%Y-%m-%d")),
					cb.function("date_format", Date.class, contractEndTime, cb.literal("%Y-%m-%d"))))); 
	
		} else{
			p.getExpressions().add(cb.or(
					cb.greaterThan(cb.function("date_format", Date.class, contractStartTime, cb.literal("%Y-%m-%d")), cb.function("date_format", Date.class, cb.literal(new Date()), cb.literal("%Y-%m-%d"))),
			        cb.lessThan(cb.function("date_format", Date.class, contractEndTime, cb.literal("%Y-%m-%d")), cb.function("date_format", Date.class, cb.literal(new Date()), cb.literal("%Y-%m-%d")))  
			        ));
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
}