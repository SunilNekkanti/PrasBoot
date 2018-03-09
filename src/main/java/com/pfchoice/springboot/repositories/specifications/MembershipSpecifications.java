
package com.pfchoice.springboot.repositories.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.pfchoice.springboot.model.Insurance;
import com.pfchoice.springboot.model.Membership;
import com.pfchoice.springboot.model.MembershipActivityMonth;
import com.pfchoice.springboot.model.MembershipLevelSummary;
import com.pfchoice.springboot.model.Provider;


public class MembershipSpecifications implements Specification<Membership> {

	private String searchTerm;

	private List<Integer> insIds;

	private List<Integer> prvdrIds;
	
	private List<Integer> reportMonths;

	private List<Integer> activityMonths;

	private Integer effectiveYear;

	private List<Integer> problemIds;
	
	private Integer mlrFrom;
	
	private Integer mlrTo;
	

	public MembershipSpecifications(String searchTerm, List<Integer> insIds, List<Integer> prvdrIds, Integer mlrFrom, Integer mlrTo,
			List<Integer> reportMonths, List<Integer> activityMonths, Integer effectiveYear, List<Integer> problemIds) {
		super();
		this.searchTerm = searchTerm;
		this.insIds = insIds;
		this.prvdrIds = prvdrIds;
		this.effectiveYear = effectiveYear;
		this.problemIds = problemIds;
		this.reportMonths = reportMonths;
		this.activityMonths = activityMonths;
		this.mlrFrom = mlrFrom;
		this.mlrTo = mlrTo;
	}

	
	public Predicate toPredicate(Root<Membership> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

		String containsLikePattern = getContainsLikePattern(searchTerm);
       
		Subquery<Membership> subQuery  = cq.subquery(Membership.class);
		Root<Membership>  subQueryRoot =   subQuery.from(Membership.class);
		
		cq.distinct(true);
		Predicate p = cb.conjunction();
		Predicate phaving = cb.conjunction();
   
		Join<Membership, MembershipActivityMonth> mbrActivityMonthList = subQueryRoot.join("mbrActivityMonthList") ;
		Join<MembershipActivityMonth, Insurance> mbrActivityMonthIns =  mbrActivityMonthList.join("ins");
		Join<MembershipActivityMonth, Provider> mbrActivityMonthprvdr =  mbrActivityMonthList.join("prvdr");
		
		if (insIds != null && insIds.size() > 0) {
			p.getExpressions().add(cb.and(mbrActivityMonthIns.get("id").in(insIds)));
		}

		if (prvdrIds != null && prvdrIds.size() > 0) {
			p.getExpressions().add(cb.and(mbrActivityMonthprvdr.get("id").in(prvdrIds)));
		}
		
		if (searchTerm != null && !"".equals(searchTerm)) {
			p.getExpressions().add(cb.or(cb.like(cb.lower(subQueryRoot.get("firstName")), containsLikePattern),
					cb.like(cb.lower(subQueryRoot.get("lastName")), containsLikePattern),
					cb.like(cb.lower(subQueryRoot.join("status").get("description")), containsLikePattern),
					cb.like(cb.lower(mbrActivityMonthIns.get("name")), containsLikePattern),
					cb.like(cb.lower(mbrActivityMonthprvdr.get("name")), containsLikePattern),
			        cb.like(cb.lower(subQueryRoot.join("mbrRafScores", JoinType.LEFT).get("rafScore").as(String.class)), containsLikePattern)));
		}

		if (reportMonths != null && reportMonths.size() > 0 && activityMonths != null && activityMonths.size() > 0) {
			Join<Membership, MembershipLevelSummary> mbrLevelSummaryList = subQueryRoot.join("mbrLevelSummaryList", JoinType.LEFT) ;
			
			p.getExpressions().add(cb.and(mbrLevelSummaryList.get("reportMonth").in(reportMonths)));
			p.getExpressions().add(cb.and(mbrLevelSummaryList.get("activityMonth").in(activityMonths)));
			p.getExpressions().add(cb.and(cb.equal(mbrActivityMonthList.get("activityMonth"),mbrLevelSummaryList.get("activityMonth"))));
			Expression<Double> funding = cb.sum(mbrLevelSummaryList.<Double>get("funding"));
			Expression<Double> exp = cb.sum(mbrLevelSummaryList.<Double>get("exp"));
			Expression<Double> avgMlr = cb.toDouble(cb.prod(cb.quot(exp, funding), 100.0));
			if(mlrFrom!= null && !"".equals(mlrFrom)){
				phaving.getExpressions().add(cb.greaterThanOrEqualTo(avgMlr, Double.valueOf(mlrFrom)));
			}
			if(mlrTo!= null && !"".equals(mlrTo)){
				phaving.getExpressions().add(cb.lessThanOrEqualTo(avgMlr, Double.valueOf(mlrTo)));
			}
			
		} 
			
		if (effectiveYear != null) {
			String effectiveYearLikePattern = getContainsLikePattern(effectiveYear.toString());
			p.getExpressions()
					.add(cb.and(cb.like(mbrActivityMonthList.get("activityMonth").as(String.class),
							effectiveYearLikePattern)));
		}

		if (problemIds != null && problemIds.size() > 0) {
			p.getExpressions().add(cb.and(subQueryRoot.join("mbrProblemList").get("pbm").in(problemIds)));
		}
		p.getExpressions().add(cb.and(cb.equal(subQueryRoot.get("activeInd"), 'Y')));
		
		subQuery.select(subQueryRoot.get("id")).where(p);
		subQuery.groupBy(subQueryRoot.get("id"));
		subQuery.having(phaving);
         
		return cb.and(root.get("id").in(subQuery));

	}

	private static String getContainsLikePattern(String searchTerm) {
		if (searchTerm == null || searchTerm.isEmpty()) {
			return "%";
		} else {
			return "%" + searchTerm.toLowerCase() + "%";
		}
	}
}