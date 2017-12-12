package com.pfchoice.springboot.repositories;

import java.util.Map;

public interface PrasRepositoryCustom {

	Integer executeSqlScript(String sql, Map<String, Object> parameters);

}
