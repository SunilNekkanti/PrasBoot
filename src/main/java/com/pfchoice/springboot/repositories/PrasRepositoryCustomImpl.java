package com.pfchoice.springboot.repositories;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

public class PrasRepositoryCustomImpl implements PrasRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional
	public Integer executeSqlScript(String sql, Map<String, Object> parameters) {

		Query query = em.createNativeQuery(sql);
		for (Map.Entry<String, Object> entry : parameters.entrySet()) {
			if (entry.getValue() instanceof String) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		return query.executeUpdate();
	}

}
