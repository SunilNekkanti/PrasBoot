package com.pfchoice.springboot.model.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.pfchoice.springboot.repositories.specifications.MembershipClaimSpecifications;
import com.pfchoice.springboot.repositories.specifications.MembershipSpecifications;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class RecordDetailsFilterAdvisor {

	public static final Logger log = LoggerFactory.getLogger(RecordDetailsFilterAdvisor.class);
	
    @PersistenceContext
    private EntityManager entityManager;

    @Pointcut("execution(public * com.pfchoice.springboot.repositories.intf.RecordDetailsAwareRepository+.*(..)) ")
    protected void recordDetailsAwareRepositoryMethod(){

    }

    @Around(value = "recordDetailsAwareRepositoryMethod()")
    public Object enableRecordDetailsFilter(ProceedingJoinPoint joinPoint) throws Throwable{
    	 System.out.println("  annotation = **************************" );
    	
    	 // Variable holding the session
        Session session = null;
        
        try {

            // Get the Session from the entityManager in current persistence context
            session = entityManager.unwrap(Session.class);

            // Enable the filter
            Filter filter = session.enableFilter("activeIndFilter");

            // Set the parameter from the session
            filter.setParameter("activeInd", getSessionActiveIndRef());

        } catch (Exception ex) {

            // Log the error
            log.error("Error enabling activeIndFilter : Reason -" +ex.getMessage());

        }

        // Proceed with the joint point
        Object obj  = joinPoint.proceed();

        // If session was available
        if ( session != null ) {

            // Disable the filter
            session.disableFilter("activeIndFilter");

        }

        // Return
        return obj;

    }


    private Character getSessionActiveIndRef() {
		return new Character('Y');
    }
}