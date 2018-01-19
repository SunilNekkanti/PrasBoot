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

	
	 @Query(value ="SELECT NEW com.pfchoice.springboot.model.NewMedicalLossRatio( mlr.reportMonth , "
	 		+ "mlr.activityMonth , "
	 		+ " sum(mlr.amgMbrCnt) ,"
	 		+ " sum(mlr.funding) ,"
	 		+ " sum(mlr.amgProf),"
			+ " sum(mlr.amgInst) ,"
			+ "sum(mlr.amgPhar), "
			+ "sum(mlr.ibnr),"
			+ "sum(mlr.pcpCap) ,"
			+ "sum(mlr.specCap) ,"
			+ "sum(mlr.dentalCap) ,"
			+ "sum(mlr.transCap),"
			+ "sum(mlr.visCap) ,"
			+ " sum(mlr.amgSLExp) ,"
			+ "sum(mlr.amgSLCredit) ,"
			+ " sum(mlr.amgVabAdjust),"
			+ " sum(mlr.adjust) ,"
			+ " sum(mlr.ibnrInst) ,"
			+ " sum(mlr.ibnrProf) ,"
			+ "sum(mlr.totalExp),"
			+ "sum(mlr.balance) ,"
			+ " qmlrfunction( reportMonth, mlr.ins.id, mlr.prvdr.id,mlr.activityMonth,true,true) , "
			+ " qmlrfunction( reportMonth, mlr.ins.id, mlr.prvdr.id,mlr.activityMonth,false,true)) from com.pfchoice.springboot.model.NewMedicalLossRatio mlr"
			+ " WHERE mlr.ins.id =:insId and mlr.prvdr.id in (:prvdrIds) and mlr.reportMonth in (:reportMonths) and mlr.activityMonth in (:activityMonths)"
			+ "group by mlr.reportMonth,mlr.ins.name,mlr.activityMonth ")
	 Page<NewMedicalLossRatio> findSummary(@Param("insId") Integer insId, @Param("prvdrIds") List<Integer> prvdrIds, 
			@Param("reportMonths") List<Integer> reportMonths, @Param("activityMonths") List<Integer> activityMonths, Pageable page);

	 
	 @Query(value = "select distinct cast(left(activity_month,4) as char)  from new_medical_loss_ratio order by report_month asc", nativeQuery = true)
		public List<String> findAllReportingYears();

 }
