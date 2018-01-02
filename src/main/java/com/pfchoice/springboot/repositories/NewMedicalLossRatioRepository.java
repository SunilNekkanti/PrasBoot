package com.pfchoice.springboot.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfchoice.springboot.model.NewMedicalLossRatio;

@Repository
public interface NewMedicalLossRatioRepository
		extends JpaRepository<NewMedicalLossRatio, Integer>, JpaSpecificationExecutor<NewMedicalLossRatio> {

	@Query(value ="SELECT new NewMedicalLossRatio( mlr.reportMonth as reportMonth, mlr.activityMonth as activityMonth,  sum( mlr.amgMbrCnt) as amgMbrCnt, sum(mlr.funding) as funding, sum(mlr.amgProf) as amgProf,"
			+ " sum(mlr.amgInst) as amgInst ,sum(mlr.amgPhar) as amgPhar, sum(mlr.ibnr) as ibnr,sum(mlr.pcpCap) as pcpCap,sum(mlr.specCap) as specCap,sum(mlr.dentalCap) as dentalCap,sum(mlr.transCap) as transCap,sum(mlr.visCap) as visCap,"
			+ " sum(mlr.amgSLExp) as stopLossExp,sum(mlr.amgSLCredit) as stopLossCredit, sum(mlr.amgVabAdjust) as amgVabAdjust, sum(mlr.adjust) as adjust,sum(mlr.totalExp) as totalExp,"
			+ "sum(mlr.balance) as balance, qmlrfunction(reportMonth,mlr.ins.id,mlr.prvdr.id,activityMonth,true, true)  as mlr, qmlrfunction(reportMonth,mlr.ins.id,mlr.prvdr.id,activityMonth,false,true)  as qmlr) from NewMedicalLossRatio mlr"
			+ "  WHERE mlr.ins.id= :insId and mlr.prvdr.id  in ( :prvdrIds) and reportMonth  in ( :reportMonths) group by reportMonth,mlr.ins.id, activityMonth  order by reportMonth,mlr.ins.name,activityMonth ")
	Page<NewMedicalLossRatio> findSummary(@Param("insId") Integer insId, @Param("prvdrIds") List<Integer> prvdrIds, 
			@Param("reportMonths") List<Integer> reportMonths, Pageable page);

}
