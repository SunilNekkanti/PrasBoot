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

	@Query("SELECT new NewMedicalLossRatio( mlr.reportMonth as reportMonth, mlr.activityMonth as activityMonth, sum(mlr.patients) as patients,"
			+ "sum(mlr.fund) as fund, sum(mlr.prof) as prof,sum(mlr.inst) as inst, sum(mlr.pharmacy) as pharmacy,sum(mlr.ibnr) as ibnr,sum(mlr.pcpCap) as pcpCap,"
			+ "sum(mlr.specCap) as specCap, sum(mlr.stopLossExp) as stopLossExp,sum(mlr.stopLossCredit) as stopLossCredit,sum(mlr.adjust) as adjust,sum(mlr.totalExp) as totalExp,"
			+ "sum(mlr.balance) as balance,sum(mlr.unwantedClaims) as unwantedClaims,sum(mlr.stopLoss) as stopLoss,sum(mlr.totalExp)/sum(mlr.fund)*100 as mlr, sum(mlr.qmlr) as qmlr) from NewMedicalLossRatio mlr"
			+ "  WHERE mlr.ins.id= :insId and mlr.prvdr.id  in ( :prvdrIds) and reportMonth  in ( :reportMonths) group by mlr.ins.id,reportMonth, activityMonth ")
	Page<NewMedicalLossRatio> findSummary(@Param("insId") Integer insId, @Param("prvdrIds") List<Integer> prvdrIds,
			@Param("reportMonths") List<Integer> reportMonths, Pageable page);

}
