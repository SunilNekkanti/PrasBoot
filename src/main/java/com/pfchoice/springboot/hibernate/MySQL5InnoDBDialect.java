package com.pfchoice.springboot.hibernate;

import java.sql.Types;

import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.BigDecimalType;

public class MySQL5InnoDBDialect extends org.hibernate.dialect.MySQL5InnoDBDialect { 
	 
	  public MySQL5InnoDBDialect () { 
	 
	    super(); 
	 
	    registerColumnType(Types.BOOLEAN, "BIT(1)"); 
	    registerFunction("qmlrfunction", new SQLFunctionTemplate(BigDecimalType.INSTANCE, "qmlrfunction(?1, ?2, ?3, ?4, ?5, ?6)")); 
	  } 
	}