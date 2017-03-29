package org.bib.tbase;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * TestConfig will be processed last, so put property overrides in test.properties.
 */
@Configuration
@PropertySource({"classpath:/test.properties"})
public class TestConfig {
// Constructors

// Public Methods
	
// Getters & Setters

// Attributes
}
