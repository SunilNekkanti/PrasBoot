package com.pfchoice.springboot.repositories.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.EventAssignment;

public class EventAssignmentSpecifications implements Specification<EventAssignment> {

	private String searchTerm;
	private String roleName;
	private Integer userId;

	public EventAssignmentSpecifications(Integer userId, String roleName, String searchTerm) {
		super();
		this.searchTerm = searchTerm;
		this.roleName = roleName;
		this.userId = userId;
	}

	public Predicate toPredicate(Root<EventAssignment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);
		cq.distinct(true);

		Predicate p = cb.conjunction();
		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions()
					.add(cb.or(cb.like(root.join("event").get("eventName"), containsLikePattern),
							cb.like(root.get("eventDateStartTime").as(String.class), containsLikePattern),
							cb.like(root.get("eventDateEndTime").as(String.class), containsLikePattern),
							cb.like(root.join("representatives").get("name"), containsLikePattern)

			));
		}
		

		if ("AGENT".equals(roleName) || "EVENT_COORDINATOR".equals(roleName)) {
			p.getExpressions().add(cb.and(cb.equal(root.join("representatives").get("id").as(Integer.class), userId)));
			
			/*Expression<Date> eventStartTime = root.get("eventDateStartTime");
			Expression<Date> eventEndTime = root.get("eventDateEndTime");

			p.getExpressions()
			.add(cb.and(cb.between(
					cb.function("date_format", Date.class, cb.literal(new Date()), cb.literal("%Y-%m-%d")),
					cb.function("date_format", Date.class, eventStartTime, cb.literal("%Y-%m-%d")),
					cb.function("date_format", Date.class, eventEndTime, cb.literal("%Y-%m-%d")))));*/
			
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