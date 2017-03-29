package org.bib.tbase;

import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.config.BaseConfig;
import org.bib.tbase.config.web.PagingConfig;
import org.bib.tbase.config.web.SecurityConfig;
import org.bib.tbase.dbunit.ColumnSensingReplacementDataSetLoader;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

/**
 * Base unit test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BaseConfig.class, SecurityConfig.class, TestConfig.class, PagingConfig.class })
@Transactional
@Rollback(true)
@WebAppConfiguration
@EnableWebSecurity
@TestExecutionListeners({ DbUnitTestExecutionListener.class, WithSecurityContextTestExecutionListener.class })
@DbUnitConfiguration(dataSetLoader=ColumnSensingReplacementDataSetLoader.class, databaseConnection="dataSource")
@DatabaseSetup("classpath:sampleData.xml")
public class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {
// Constants
	/**
	 * Default spring profile to run unit tests under.
	 */
	public static final String DEFAULT_SPRING_PROFILE = "embed";
	
// Constructors

// Public Methods
	/**
	 * Test subclasses can use this JdbcTemplate directly.
	 */
	@Override
	@Resource(name="dataSource")
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
// Protected Methods
	/**
	 * Look up the spring profile from the System environment.  Allows us to override the active profile by setting the active profile as an environment variable under the key of SPRING_PROFILE.
	 * @return A String representing the override profile, or null if no override exists
	 */
	protected static String getSpringProfileFromEnv() {
		String value = null;
		
		Map<String, String> envMap = System.getenv();
		
		if(envMap.containsKey("SPRING_PROFILE")) {
			value = envMap.get("SPRING_PROFILE");
		}
		
		return StringUtils.trimToNull(value);
	}

// Getters & Setters
	

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(BaseTest.class);
	
	static {
		// If profile named is defined on the environment, use it rather than the local profile
		String envSpringProfile = getSpringProfileFromEnv();
		
		if(StringUtils.isNotBlank(envSpringProfile)) {
			System.setProperty("spring.profiles.active", envSpringProfile);
		}
		else {
			System.setProperty("spring.profiles.active", DEFAULT_SPRING_PROFILE);
		}
		
		logger.debug("spring.profiles.active = " + System.getProperty("spring.profiles.active"));
	}
}
