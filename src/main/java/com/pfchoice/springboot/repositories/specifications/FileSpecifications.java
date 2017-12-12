package com.pfchoice.springboot.repositories.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.File;

public class FileSpecifications implements Specification<File> {

	private String searchTerm;

	public FileSpecifications(String searchTerm) {
		super();
		this.searchTerm = searchTerm;
	}

	public Predicate toPredicate(Root<File> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);
		cq.distinct(true);

		Predicate p = cb.conjunction();
		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions()
					.add(cb.or(cb.like(cb.lower(root.get("fileName")), containsLikePattern),
							cb.like(cb.lower(root.join("fileType").get("description")), containsLikePattern),
							cb.like(cb.lower(root.join("fileType").join("ins").get("name")), containsLikePattern)));
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