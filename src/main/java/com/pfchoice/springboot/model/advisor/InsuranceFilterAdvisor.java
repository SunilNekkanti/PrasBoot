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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class InsuranceFilterAdvisor {

	public static final Logger log = LoggerFactory.getLogger(InsuranceFilterAdvisor.class);
	
    @PersistenceContext
    private EntityManager entityManager;

    private List<Integer> insIds = new ArrayList<>();
    
    @Pointcut("execution(public * com.pfchoice.springboot.repositories.intf.InsuranceAwareRepository+.*(..)) ")
    protected void insuranceAwareRepositoryMethod(){

    }

    @Around(value = "insuranceAwareRepositoryMethod()")
    public Object enableInsuranceFilter(ProceedingJoinPoint joinPoint) throws Throwable{
    	
    	 // Variable holding the session
        Session session = null;
        Object[] arguments = joinPoint.getArgs();
	   	 for(Object arg : arguments){
	   		 if( arg instanceof MembershipSpecifications ){
	   			insIds =  ((MembershipSpecifications) arg).getInsIds();
	   		 }  
		   	
	   	 }
        try {

            // Get the Session from the entityManager in current persistence context
            session = entityManager.unwrap(Session.class);

            // Enable the filter
            Filter filter = session.enableFilter("insuranceFilter");

            // Set the parameter from the session
            filter.setParameter("insIds", StringUtils.collectionToCommaDelimitedString(insIds));

        } catch (Exception ex) {

            // Log the error
            log.error("Error enabling insuranceFilter : Reason -" +ex.getMessage());

        }

        // Proceed with the joint point
        Object obj  = joinPoint.proceed();

        // If session was available
        if ( session != null ) {

            // Disable the filter
            session.disableFilter("insuranceFilter");

        }

        // Return
        return obj;

    }

}