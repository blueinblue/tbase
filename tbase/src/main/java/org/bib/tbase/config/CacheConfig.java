package org.bib.tbase.config;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Cache Configuration
 */
@Configuration
@EnableCaching
public class CacheConfig {
// Constructors

// Public Methods
	/**
	 * Cache Manager
	 */
	@Bean
	public CacheManager cacheManager() {
		// Add any additional caches here
		CacheManager cacheManager = new ConcurrentMapCacheManager(USER_CACHE_NAME);
		
		return cacheManager;
	}

	
	
// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(CacheConfig.class);
	
// Constants
	/**
	 * Cache Name for User entities.
	 */
	public static final String USER_CACHE_NAME = "userCache";
}
