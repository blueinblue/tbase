package org.bib.tbase.config.web;

import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.spring4.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring4.webflow.view.FlowAjaxThymeleafView;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

import nz.net.ultraq.thymeleaf.LayoutDialect;

// TODO: Package Fix for ArcheType
@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"org.bib.tbase.web", "org.bib.tbase.config.web"} )
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
// Constructors
	
// Public Methods
	
// Web Flow Config
	/**
	 * A Spring WebFlow HandlerMapping implementation that maps URL fragments to web flow definitions.
	 * 
	 * The FlowHandlerMapping takes a URL, like /app/flows/personEdit and resolves the flow id (personEdit) in the FlowRegistry to the
	 * personEdit-flow.xml flow.
	 * 
	 * Since we're using both plain Spring MVC and Spring Webflow, we set the order of the flowHandlerMapping to 0, so the DispatcherServlet
	 * will check the flow registry first.  If the flowHandlerMapping can't return a handler for the request, the DispatcherServlet
	 * moves on to the standard Spring MVC handlerMapping.
	 */ 
	@Bean
	public FlowHandlerMapping flowHandlerMapping() {
		FlowHandlerMapping flowHandlerMapping = new FlowHandlerMapping();
		
		flowHandlerMapping.setFlowRegistry(webFlowConfig.flowRegistry());
		flowHandlerMapping.setOrder(0);
		
		return flowHandlerMapping;
	}
	
	/**
	 * A Spring WebFlow HandlerAdapter implementation that takes the HttpRequest/HttpResponse and services the request with
	 * a flow executor.
	 */
	@Bean
	public FlowHandlerAdapter flowHandlerAdapter() {
		FlowHandlerAdapter flowHandlerAdapter = new FlowHandlerAdapter();
		
		flowHandlerAdapter.setFlowExecutor(webFlowConfig.flowExecutor());
		flowHandlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
		
		return flowHandlerAdapter;
	}
	
// ThymeLeaf Config
	/**
	 * A Thymeleaf ViewResolver implementation that maps logical view names to thymeleaf templates by way of a template resolver configured in the template engine.
	 */
	@Bean
	public ThymeleafViewResolver thymeleafViewResolver() {
		AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
		
		viewResolver.setViewClass(FlowAjaxThymeleafView.class);
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setOrder(1);
		
		return viewResolver;
	}
	
	/**
	 * Template Resolver for resolving thymeleaf views returned by Spring Web Flow.
	 */
	@Bean(name = "webFlowTemplateResolver")
	public ITemplateResolver webFlowTemplateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/flows/");
		templateResolver.setSuffix(".html");
		templateResolver.setOrder(2);
		templateResolver.setApplicationContext(appContext);
		
		return templateResolver;
	}
	
	/**
	 * Template Resolver for resolving thymeleaf views returned by standard Spring MVC Controllers.
	 */
	@Bean(name = "mvcTemplateResolver")
	public ITemplateResolver mvcTemplateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setOrder(1);
		templateResolver.setApplicationContext(appContext);
		templateResolver.setCheckExistence(true);
		
		return templateResolver;
	}
	
	/**
	 * Thymeleaf template engine that registers template resolvers and dialects with Thymeleaf.
	 */
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setEnableSpringELCompiler(true);
		
		Set<ITemplateResolver> templateResolvers = new HashSet<ITemplateResolver>();
		templateResolvers.add(webFlowTemplateResolver());
		templateResolvers.add(mvcTemplateResolver());
		
		templateEngine.setTemplateResolvers(templateResolvers);
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new SpringSecurityDialect());
		
		return templateEngine;
	}
	
// Exception Resolvers
	/**
	 * Global exception handler that handles any exceptions not handled by controllers.
	 */
	@Bean(name="globalExceptionHandler")
	public HandlerExceptionResolver globalExceptionHandler() {
		return new HandlerExceptionResolver() {
			@Override
			public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
				// Log it
				errLogger.error("Global error caught by globalExceptionHandler", ex);
				
				// Store exception artifacts in model
				RequestContextUtils.getOutputFlashMap(request).put("ex", ex);
				RequestContextUtils.getOutputFlashMap(request).put("rootStackTrace", ExceptionUtils.getRootCauseStackTrace(ex));
				
				// Ajax requests are handled differently than regular get/post requests
				boolean isAjaxRequest = "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
				
				ModelAndView mav = new ModelAndView();
				
				/*
				 * If a server error occurs while processing an AJAX request, then we need to send a special, application defined status code back to the client side JavaScript.  When the
				 * client receives the 900 status, it knows to redirect to the error page.  See the Ajax oncomplete handler in layout.html.
				 * 
				 *  If an error occurs while processing a regular, non-ajax request, then we redirect the user to the error page.
				 */
				if (isAjaxRequest) {
					response.setStatus(900);
				}
				else {
					RedirectView redirectView = new RedirectView("/syspublic/error", true);
					mav.setView(redirectView);
				}
				
				return mav;
			}
			
			// Attributes
			private final Logger errLogger = LogManager.getLogger("Global-Exception-Handler");
		};
	}


	
// WebMvcConfigurerAdapter Overrides
	/**
	 * Resource handlers to serve static resources.
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// App defined js, img, and css resources
		registry.addResourceHandler("/public/**").addResourceLocations("/public/");
		
		// WebJar bundles
		registry.addResourceHandler("/webjars/**")
        	.addResourceLocations("/resources/", "/webjars/")
        	.setCacheControl(CacheControl.maxAge(30L, TimeUnit.DAYS).cachePublic())
        	.resourceChain(true)
        	.addResolver(new WebJarsResourceResolver());
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	/**
	 * Configure MessageConverters (like Jackson, which translates to JSON)
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// Jackson2ObjectMapperBuilder - allows us to customize Jackson behaviors
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.indentOutput(true).dateFormat(new SimpleDateFormat("MM-dd-yyyy"));
		
		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
		converters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));
	}
	
	/**
	 * Add Converters/Formatters to the DefaultFormattingConversionService registered in the app context by the EnableWebMvc annotation.  The conversion service is registered 
	 * under the bean name "mvcConversionService."
	 * 
	 * WebFlow Note:  If this is all you configured, then WebFlow would not be able to take advantage of the formatter/converters registered here.  WebFlow creates its own conversion service
	 * bean, a DefaultConversionService.  Luckily this class's constructor takes as an argument a ConversionService instance, by which we can pass a reference to the Spring MVC conversion service,
	 * thus tying the two together.  Killing two birds with one stone is the goal here - along with centralzing our global converters in one configuration.
	 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		super.addFormatters(registry);
		
		DateFormatter dateFormatter = new DateFormatter("MM/dd/yyyy");
		dateFormatter.setLenient(true);
		registry.addFormatter(dateFormatter);
	}
	
// Getters & Setters
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}
	
// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(WebConfig.class);
	
	/**
	 * Web Flow Config
	 */
	@Autowired
	private WebFlowConfig webFlowConfig;
	
	/**
	 * Application Context for SpringResourceTemplateResolvers
	 */
	private ApplicationContext appContext;
}