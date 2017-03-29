package org.bib.tbase.config;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Data Source Configuration
 */
@Configuration
@EnableTransactionManagement
@PropertySources(value={@PropertySource("classpath:appconfig/jdbc.properties"), @PropertySource("classpath:appconfig/${spring.profiles.active}/jdbc.properties")})
public class DataSourceConfig {
// Constructors

// Public Methods
	/**
	 * A DataSource instance to access an inmemory database.  Must define embedded database type in property jdbc.embed.type (use a constant value from EmbeddedDatabaseType).  Defaults
	 * to HSQL if not defined.
	 * 
	 * You may provide a comma separated list of paths to db scripts to execute on db build - property name is jdbc.embed.scripts
	 * 
	 * @return A DataSource instance
	 */
	@Profile("embed")
	@Bean(name="dataSource")
	public DataSource embedDataSource() {
		final String dbType = env.getProperty("jdbc.embed.type", "HSQL");
		
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		
		builder = builder.setType(EmbeddedDatabaseType.valueOf(dbType));
		
		// If comma separated list of db scripts is provided, then use them
		String dbScripts = env.getProperty("jdbc.embed.scripts");
		
		if(StringUtils.isNotBlank(dbScripts)) {
			builder = builder.addScripts(StringUtils.split(dbScripts, ","));
		}
		
		return builder.build();
	}
	
	/**
	 * Local env DataSource (non-embedded)
	 * @return
	 */
	@Profile({"local"})
	@Bean(name="dataSource")
	public DataSource localDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDefaultAutoCommit(false);
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(true);
		
		// Environment Config
		String driverClass = env.getProperty("jdbc.driverClassName");
		String jdbcUrl = env.getProperty("jdbc.url");
		String userName = env.getProperty("jdbc.username");
		String password = env.getProperty("jdbc.password");
		String validationQuery = env.getProperty("jdbc.validation.query");
		int initialPoolSize = env.getProperty("jdbc.initial.size", Integer.class);
				
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setInitialSize(initialPoolSize);
		
		return dataSource;
	}
	
	/**
	 * Test and Prod env DataSource - looked up by JDNI.  Must define lookup name in property jdbc.jdni.name
	 * 
	 * @return A DataSource instance
	 */
	@Profile({"test", "prod"})
	@Bean(name="dataSource")
	public DataSource jndiDataSource() throws NamingException {
		String jndiName = env.getProperty("jdbc.jndi.name");
		
		JndiTemplate jndi = new JndiTemplate();
		
        return (DataSource) jndi.lookup("java:comp/env/jdbc/" + jndiName);
	}
	
// Getters & Setters

// Attributes
	/**
	 * Environment properties
	 */
	@Inject
	private Environment env;
}
