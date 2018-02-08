package com.pfchoice.springboot.repositories.specifications;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.LeadMembership;

public class LeadSpecifications implements Specification<LeadMembership> {

	private String roleName;
	private String searchTerm;
	private Integer userId;
	private String username;

	public LeadSpecifications(Integer userId, String username, String roleName, String searchTerm) {
		super();
		this.roleName = roleName;
		this.searchTerm = searchTerm;
		this.userId = userId;
		this.username = username;
	}

	public Predicate toPredicate(Root<LeadMembership> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		String containsLikePattern = getContainsLikePattern(searchTerm);
		cq.distinct(true);
		Predicate p = cb.conjunction();
		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions()
					.add(cb.or(cb.like(cb.lower(root.get("firstName")), containsLikePattern),
							cb.like(cb.lower(root.get("lastName")), containsLikePattern),
							cb.like(root.join("gender").get("description"), containsLikePattern),
							cb.like(root.join("language").get("description"), containsLikePattern),
							cb.like(root.join("status").get("description"), containsLikePattern),
							cb.like(root.join("statusDetail", JoinType.LEFT).get("description"), containsLikePattern)

			));
		}
		
		if (!"ADMIN".equals(roleName)) {
			p.getExpressions().add(cb.and(cb.notEqual(cb.upper(root.join("status").get("description")), "HOLD")));
		}

		if ("EVENT_COORDINATOR".equals(roleName)) {
			p.getExpressions().add(cb.and(cb.equal(root.get("createdBy").as(String.class), username)));
		}
		if ("AGENT".equals(roleName)) {
			p.getExpressions().add(cb.and(cb
					.equal((root.join("agentLeadAppointmentList").join("user").get("id").as(Integer.class)), userId)));
			p.getExpressions().add(cb.and(cb.equal(cb.upper(root.join("status").get("description")), "AGENT")));

			Expression<Date> appointmentTime = root.join("agentLeadAppointmentList").get("appointmentTime");
			Expression<Date> allocationEndTime = cb.function("AGENT_ALLOCATION_ENDDATE", Date.class, appointmentTime,
					cb.literal(1440));

			p.getExpressions().add(cb.lessThanOrEqualTo(appointmentTime, allocationEndTime));
			p.getExpressions().add(cb.and(cb.equal(root.join("agentLeadAppointmentList", JoinType.LEFT).get("activeInd"), 'Y')));
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