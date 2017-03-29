package org.bib.tbase.config.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.pagination.FixedWidthPaginationCalculator;
import org.bib.pagination.PageRequestFactory;
import org.bib.pagination.PaginationCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagingConfig {
// Constructors

// Public Methods
	@Bean("paginationCalc")
    public PaginationCalculator paginationCalc() {
        // 10 = Maximum/preferred number of page links to show in a pagination component
        PaginationCalculator pageCalc = new FixedWidthPaginationCalculator(10);

        return pageCalc;
    }
	
	@Bean("pageRequestFactory")
    public PageRequestFactory pageRequestFactory() {
        // 20 = Default number of elements to display in a page if not otherwise specified
        PageRequestFactory prFactory = new PageRequestFactory(20);
        
        return prFactory;
    }

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(PagingConfig.class);
}
