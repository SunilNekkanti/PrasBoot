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


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class FollowupTypeFilterAdvisor {

	public static final Logger log = LoggerFactory.getLogger(FollowupTypeFilterAdvisor.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Pointcut("execution(public * com.pfchoice.springboot.repositories.intf.FollowupTypeAwareRepository+.*(..))")
	protected void followupTypeAwareRepositoryMethod() {

	}

	@Around(value = "followupTypeAwareRepositoryMethod()")
    public Object enableFollowupTypeFilter(ProceedingJoinPoint joinPoint) throws Throwable{
        // Variable holding the session
        Session session = null;
        try {

            // Get the Session from the entityManager in current persistence context
            session = entityManager.unwrap(Session.class);

            // Enable the filter
            Filter filter = session.enableFilter("followupTypeFilter");

            // Set the parameter from the session
            filter.setParameter("followupTypeId", 2);

        } catch (Exception ex) {

            // Log the error
            log.error("Error enabling followupTypeFilter : Reason -" +ex.getMessage());

        }

        // Proceed with the joint point
        Object obj  = joinPoint.proceed();

        // If session was available
        if ( session != null ) {

            // Disable the filter
            session.disableFilter("followupTypeFilter");

        }

        // Return
        return obj;

    }

}