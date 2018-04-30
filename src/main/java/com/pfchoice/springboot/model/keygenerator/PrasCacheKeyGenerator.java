package com.pfchoice.springboot.model.keygenerator;

import java.lang.annotation.Annotation;

import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.GeneratedCacheKey;

public class PrasCacheKeyGenerator implements CacheKeyGenerator {
    @Override
    public GeneratedCacheKey generateCacheKey(final CacheKeyInvocationContext<? extends Annotation> cacheKeyInvocationContext) {
        return new GeneratedCacheKey() {
            @Override
            public int hashCode() {
            	System.out.println("42");
                return 42;
            }

            @Override
            public boolean equals(Object other) {
            	System.out.println("true");
                return true;
            }
        };
    }
}
