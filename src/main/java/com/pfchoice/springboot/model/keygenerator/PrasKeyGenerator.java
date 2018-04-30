package com.pfchoice.springboot.model.keygenerator;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

public class PrasKeyGenerator implements KeyGenerator{
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        //include method name in key
        key.append(method.getName());
        if (params.length > 0) {
            key.append(';');
            for (Object argument : params) {
            	System.out.println("argument.toString()"+argument.toString());
                key.append(argument.toString());
                key.append(';');
            }
        }
        return key.toString();
    }
}