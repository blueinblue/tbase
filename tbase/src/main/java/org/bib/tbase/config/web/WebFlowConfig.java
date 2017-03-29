package org.bib.tbase.config.web;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.security.SecurityFlowExecutionListener;

@Configuration
public class WebFlowConfig extends AbstractFlowConfiguration {
// Constructors

// Public Methods
	/**
	 * FlowBuilderServices allows you to customize how WebFlow behaves.  For example, in this method, we set the ViewFactoryCreator to use a 
	 * Thymeleaf view resolver, rather than the standard JSP resolver.  We also setup JSR-303 bean validation by setting a validator.
	 * 
	 * The FlowExecutor loads flow definitions using the flowRegistry.  The FlowRegistry finds flow definitions and builds flow objects
	 * using the FlowBuilderServices to configure each flow object.
	 */
	@Bean
	public FlowBuilderServices flowBuilderServices() {
		return getFlowBuilderServicesBuilder()
				.setViewFactoryCreator(mvcViewFactoryCreator())
				.setValidator(localValidatorFactoryBean)
				.setDevelopmentMode(true)
				.setConversionService(webFlowConversionService())
				.build();
	}
	
	/**
	 * FlowDefinitionRegistry tells Spring WebFlow where to look for flow xml files.
	 */ 
	@Bean
	public FlowDefinitionRegistry flowRegistry() {
		return getFlowDefinitionRegistryBuilder(flowBuilderServices())
				.setBasePath("/WEB-INF/flows")
				.addFlowLocationPattern("/**/*-flow.xml").build();
	}
	
	/**
	 * A FlowExecutor handles the actual execution of the web flows - such as starting and resuming conversations.  In this method,
	 * we add a FlowExecutionListener to integrate WebFlow with Spring Security.
	 * 
	 * It takes as a config param the flowRegistry, which tells the FlowExecutor where to find flow definitions.
	 */
	@Bean
	public FlowExecutor flowExecutor() {
		return getFlowExecutorBuilder(flowRegistry())
				.addFlowExecutionListener(new SecurityFlowExecutionListener(), "*")
				.build();
	}
	
	/**
	 * An MvcViewFactoryCreator allows us to customize the view resolvers that Spring WebFlow uses to resolve logical view names and 
	 * render those views.  In this method, we use a ThymeLeaf View Resolver configured in the WebConfig configuration class.
	 * 
	 * Out of the box, WebFlow uses an InternalViewResolver to resolve view names to JSPs.  We replace this default with the
	 * Thymeleaf View Resolver, since we are using Thymeleaf to render views.
	 */
	@Bean
	public MvcViewFactoryCreator mvcViewFactoryCreator() {
		MvcViewFactoryCreator factoryCreator = new MvcViewFactoryCreator();
		
		factoryCreator.setViewResolvers(Arrays.<ViewResolver>asList(this.webConfig.thymeleafViewResolver()));
		
		return factoryCreator;
	}
	
	/**
	 * Replace WebFlow's automatically created DefaultConversionService with one that delegates formatting/conversion to the Spring MVC conversion service.  Configured above in the
	 * flowBuilderServices() bean method.
	 */
	@Bean
	public DefaultConversionService webFlowConversionService() {
		// Add Formatters - There's a bit of a circular reference between WebConfig and WebFlowConfig.  Typically formatters are added in the addFormatters() method of WebConfig, but
		// we have to do it this was because we're sharing the Conversion Service between MVC and WebFlow.
		if(CollectionUtils.isNotEmpty(formatters)) {
			for(Formatter<?> f : formatters) {
				mvcConversionService.addFormatter(f);
			}
		}
		
		mvcConversionService.addFormatter(new DateFormatter("yyyy-MM-dd"));
		
		// DefaultConversionService serves as a pass through to the MVC Conversion Service
		return new DefaultConversionService(mvcConversionService);
	}

	
// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(WebFlowConfig.class);
	
	/**
	 * Web Config
	 */
	@Autowired
	private WebConfig webConfig;
	
	/**
	 * The Spring MVC conversion service (create automatically by EnableWebMvc) that will serve as the delegate to WebFlow's conversion/formatting needs.
	 */
	@Inject
	@Named("mvcConversionService")
	private FormattingConversionService mvcConversionService;
	
	@Inject
	@Named("beanValidator")
	private LocalValidatorFactoryBean localValidatorFactoryBean;
	
	/**
	 * All Formatters defined in the application to provide type conversion in the web tier
	 */
	@Autowired(required=false)
	private Collection<Formatter<?>> formatters;
}
