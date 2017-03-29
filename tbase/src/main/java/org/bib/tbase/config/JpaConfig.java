package org.bib.tbase.config;

import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.repository.audit.UserIdAuditorAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
// TODO: Package Fix for ArcheType
@EnableJpaRepositories(basePackages="org.bib.tbase.repository", entityManagerFactoryRef="entityManagerFactory", transactionManagerRef="transactionManager")
@EnableTransactionManagement
public class JpaConfig {
// Constructors

// Public Methods
	/**
	 * Entity Manager Factory
	 * 
	 * <p><b>NOTE:</b> For Spring Data to work, the name of this method must match the entityManagerFactoryRef value on the EnableJpaRepositories annotation.</p>
	 */
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		// Get config values
		String packagesToScan = env.getProperty("jpa.packages.to.scan");
		
		// Configure emf
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaDialect(new HibernateJpaDialect());
		emf.setPackagesToScan(packagesToScan);
		emf.setDataSource(dataSource);
		emf.setJpaProperties(additionalProperties());
		emf.afterPropertiesSet();
		
		return emf.getObject();
	}
	
	/**
	 * Transaction Manager
	 * 
	 * <p><b>NOTE:</b> For Spring Data to work, the name of this method must match the transactionManagerRef value on the EnableJpaRepositories annotation.</p>
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		
		return txManager;
	}
	
	/**
	 * Exception Translation
	 */
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslator() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	/**
	 * Auditing
	 */
	@Bean(name="auditorProvider")
	public AuditorAware<Long> auditorProvider() {
		return new UserIdAuditorAware();
	}
	
	
	/**
	 * Datasource initializer - used to populate database on startup, if enabled.
	 * @return
	 */
	@Bean
	public DataSourceInitializer dataSourceInitializer() {
		DataSourceInitializer dsInit = new DataSourceInitializer();
		
		dsInit.setDataSource(dataSource);
		
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		
		databasePopulator.addScript(new ClassPathResource(initDatabasePath));
		dsInit.setDatabasePopulator(databasePopulator);
		dsInit.setEnabled(Boolean.parseBoolean(initDatabase));
		
		return dsInit;
	}
	
// Protected Methods
	/**
	 * Additional JPA Properties for EntityManagerFactory
	 * 
	 * See https://docs.jboss.org/hibernate/orm/3.6/reference/en-US/html/session-configuration.html (Optional configuration properties section)
	 */
	protected Properties additionalProperties() {
		// Gather env properties
		String dialect = env.getProperty("jpa.hibernate.dialect");
		String showSql = env.getProperty("jpa.hibernate.show_sql", "false");
		String formatSql = env.getProperty("jpa.hibernate.format_sql", "false");
		String useSqlComments = env.getProperty("jpa.hibernate.use_sql_comments", "false");
		String generateDdlMode = env.getProperty("jpa.hibernate.ddl.mode", "");
		
		// Build properties
		Properties props = new Properties();
		
		props.setProperty("hibernate.dialect", dialect);
		props.setProperty("hibernate.show_sql", showSql);
		props.setProperty("hibernate.format_sql", formatSql);
		props.setProperty("hibernate.use_sql_comments", useSqlComments);
		
		if(StringUtils.isNotBlank(generateDdlMode)) {
			props.setProperty("hibernate.hbm2ddl.auto", generateDdlMode);
		}
		
		return props;
	}
	
// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(JpaConfig.class);
	
	/**
	 * Environment variable access.
	 */
	@Inject
	private Environment env;
	
	/**
	 * Data Source
	 */
	@Inject
	@Named("dataSource")
	private DataSource dataSource;
	
	/**
	 * Load server database with data? 
	 */
	@Value("${init.db:false}")
	private String initDatabase;
	
	/**
	 * If initDatabase is enabled, then path to sql file (classpath relative)
	 */
	@Value("${init.db.path}")
	private String initDatabasePath;
}
