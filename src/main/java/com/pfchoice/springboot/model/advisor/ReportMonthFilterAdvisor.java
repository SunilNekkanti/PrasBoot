package com.pfchoice.springboot.model.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.pfchoice.springboot.repositories.specifications.MembershipSpecifications;
import com.pfchoice.springboot.repositories.specifications.NewMedicalLossRatioSpecifications;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class ReportMonthFilterAdvisor {

	public static final Logger log = LoggerFactory.getLogger(ReportMonthFilterAdvisor.class);

	@PersistenceContext
	private EntityManager entityManager;

	private List<Integer> reportMonths = new ArrayList<>();
	
	@Pointcut("execution(public * com.pfchoice.springboot.repositories.intf.ReportMonthAwareRepository+.*(..))")
	protected void reportMonthAwareRepositoryMethod() {

	}

	@Around(value = "reportMonthAwareRepositoryMethod()")
    public Object enableReportMonthFilter(ProceedingJoinPoint joinPoint) throws Throwable{
        // Variable holding the session
        Session session = null;
        System.out.println("annotation ================="+joinPoint.getArgs());
        Object[] arguments = joinPoint.getArgs();
	   	 for(Object arg : arguments){
	   		 if( arg instanceof MembershipSpecifications ){
	   			 System.out.println("annotation MembershipSpecifications =================");
	   			reportMonths =  ((MembershipSpecifications) arg).getReportMonths();
	   		 }  else if( arg instanceof NewMedicalLossRatioSpecifications ){
	   			System.out.println("annotation NewMedicalLossRatioSpecifications =================");
	   			reportMonths =  ((NewMedicalLossRatioSpecifications) arg).getReportMonths();
	   		 }
		   			
	   	 }
   			System.out.println("ReportMonthFilterAdvisor"+StringUtils.collectionToCommaDelimitedString(reportMonths));
        try {

            // Get the Session from the entityManager in current persistence context
            session = entityManager.unwrap(Session.class);

            // Enable the filter
            Filter filter = session.enableFilter("reportMonthFilter");

            // Set the parameter from the session
            filter.setParameter("reportMonths", StringUtils.collectionToCommaDelimitedString(reportMonths));

        } catch (Exception ex) {

            // Log the error
            log.error("Error enabling reportMonthFilter : Reason -" +ex.getMessage());

        }

        // Proceed with the joint point
        Object obj  = joinPoint.proceed();

        // If session was available
        if ( session != null ) {

            // Disable the filter
            session.disableFilter("reportMonthFilter");

        }

        // Return
        return obj;

    }

}