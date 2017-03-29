package org.bib.tbase.config.web;

import javax.servlet.Filter;

import org.bib.tbase.config.BaseConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
// Constructors
	public WebAppInitializer() {
	}
	
// Public Methods
	
// Protected Methods
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { BaseConfig.class, SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");

		return new Filter[] { characterEncodingFilter };
	}
	
// Getters & Setters

// Attributes
}
