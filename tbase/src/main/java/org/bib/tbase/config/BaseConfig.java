package org.bib.tbase.config;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
// TODO: Do a text search on "Package Fix" to pickup all the non-code places
// TODO: Package Fix For ArcheType
@ComponentScan(basePackages={"org.bib.tbase"}, excludeFilters={@ComponentScan.Filter(pattern={"org.bib.tbase.web.*"}, type=FilterType.REGEX),
		@ComponentScan.Filter(pattern={"org.bib.tbase.config.web.*"}, type=FilterType.REGEX)})
@EnableAspectJAutoProxy
@EnableScheduling
@PropertySources({
	@PropertySource("classpath:/appconfig/application.properties"), 
	@PropertySource("classpath:/appconfig/${spring.profiles.active}/application.properties")
})
public class BaseConfig {
// Constructors
	public BaseConfig() {
		logger.debug("Base config constructor invoked.");
	}

// Beans
	/**
	 * Registers a PropertySourcesPlaceholderConfigurer so as to load property values from the @PropertySource files.
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	/**
	 * Validator for JSR-303 Bean Validation.
	 * 
	 * Defined here so we can use JSR-303 validation outside of the web tier.  The web tier will use this instance of the validator too.
	 */
	@Bean
	public LocalValidatorFactoryBean beanValidator() {
		return new LocalValidatorFactoryBean();
	}
	
	/**
	 * Resource bundle message source.
	 * 
	 * Defined here so we can resolve messages outside of the web tier.  The web tier will use this instance of the message source too.
	 */
	@Bean(name="messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/messages");
		
		return messageSource;
	}
	
// Attributes
	/**
	 * Environment variable access.
	 */
	@Inject
	@SuppressWarnings("unused")
	private Environment env;
	
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(BaseConfig.class);
}
