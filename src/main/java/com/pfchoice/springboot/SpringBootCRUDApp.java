package com.pfchoice.springboot;

import org.apache.commons.logging.LogFactory;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.cache.annotation.CacheKeyGenerator;

import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.pfchoice.springboot.configuration.JpaConfiguration;
import com.pfchoice.springboot.model.keygenerator.PrasCacheKeyGenerator;
import com.pfchoice.springboot.model.keygenerator.PrasKeyGenerator;

@Import(JpaConfiguration.class)
@SpringBootApplication(scanBasePackages = { "com.pfchoice.springboot" }) // same as @Configuration  @EnableAutoConfiguration  @ComponentScan

//@EnableCaching(mode = AdviceMode.ASPECTJ)
public class SpringBootCRUDApp  {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCRUDApp.class, args);
	}
	
	 @Bean("prasExecutor")
	    public Executor asyncExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(4);
	        executor.setMaxPoolSize(4);
	        executor.setQueueCapacity(5);
	        executor.setThreadNamePrefix("PrasBootFileUpload-");
	        executor.initialize();
	        return executor;
	    }
	 
	 /*@Bean("prasKeyGenerator")
	 public KeyGenerator keyGenerator() {
	        return new PrasKeyGenerator();
	    }
	 
	 @Bean("prasCacheKeyGenerator")
	 public CacheKeyGenerator cacheKeyGenerator() {
	        return new PrasCacheKeyGenerator();
	    }
	 
	 
	 
	 @SuppressWarnings({ "static-access", "deprecation" })
	@Bean
	    public CacheManager cacheManager() {
	        long ttl = Long.parseLong("300");

	        double pctOfHeap = Double.valueOf("0.5");
	        long cacheSizeMB =  new Double(Runtime.getRuntime().maxMemory() * pctOfHeap / 1048576.0).longValue();

	        LogFactory.getLog(getClass()).info(
	                String.format("Initializing cache TTL=%d secs, size=%d MB (%.2f percent of max heap)",
	                        ttl, cacheSizeMB, pctOfHeap * 100));

	        org.ehcache.config.CacheConfiguration<Object, Object> cacheConfiguration = CacheConfigurationBuilder
	                .newCacheConfigurationBuilder(Object.class, Object.class,
	                        ResourcePoolsBuilder.newResourcePoolsBuilder()
	                                .heap(3000L))
	                .withExpiry(Expirations.timeToLiveExpiration(new org.ehcache.expiry.Duration(ttl, TimeUnit.SECONDS)))
	                .build();

	        Map<String, CacheConfiguration<?, ?>> caches = new HashMap<>();
	        caches.put("insurances", cacheConfiguration);
	        caches.put("providers", cacheConfiguration);
	        caches.put("reportingYears", cacheConfiguration);
	        caches.put("memberships", cacheConfiguration);
	        caches.put("membershipClaims", cacheConfiguration);

	        EhcacheCachingProvider provider = (EhcacheCachingProvider) javax.cache.Caching.getCachingProvider();

	        // when our cacheManager bean is re-created several times for
	        // diff test configurations, this provider seems to hang on to state
	        // causing cache settings to not be right. so we always close().
	        provider.close();

	        DefaultConfiguration configuration = new DefaultConfiguration(
	                caches, provider.getDefaultClassLoader());

	        return new JCacheCacheManager(
	                provider.getCacheManager(provider.getDefaultURI(), configuration));
	    }
	    */
}
