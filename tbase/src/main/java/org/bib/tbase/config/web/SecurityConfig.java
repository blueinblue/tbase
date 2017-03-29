package org.bib.tbase.config.web;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.bib.tbase.service.UserService;
import org.bib.tbase.web.security.CustomAccessDeniedHandler;
import org.bib.tbase.web.security.CustomSavedRequestAwareAuthenticationSuccessHandler;
import org.bib.tbase.web.security.PathMatchingRedirectInvalidSessionStrategy;
import org.bib.tbase.web.security.PathMatchingRedirectInvalidSessionStrategy.PathMatchingRedirectHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
// Constructors

// Public Methods
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
		
//        auth
//        	.inMemoryAuthentication()
//        		.withUser("api_user").password("password").roles("API")
//        		.and()
//        		.withUser("admin").password("password").roles("ADMIN", "USER")
//        		.and()
//        		.withUser("reguser").password("password").roles("USER");
    }
	
	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
				.csrf().disable()
				.antMatcher("/api/**")
				.authorizeRequests()
					.anyRequest().hasAnyRole("ADMIN", "API")
				.and()
					.httpBasic()
				.and()
					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	}
	
	@Configuration
	@Order(2)
	public static class FormWebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		public void configure(WebSecurity web) throws Exception {
			web
				.ignoring()
					.antMatchers("/public/**", "/webjars/**", "/resources/**");
		}
		
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
				.sessionManagement()
					.invalidSessionStrategy(invalidSessionStrategy())
					.and()
				.csrf()
					.and()
				.exceptionHandling()
					.accessDeniedHandler(customAccessDeniedHandler())
					.and()
				.authorizeRequests()
					.antMatchers("/admin/**").hasRole("ADMIN")
					.antMatchers("/secure/**").hasAnyRole("USER", "ADMIN")
					.antMatchers("/j_spring_security_check").permitAll()
					.antMatchers("/syspublic/**").permitAll()
					.antMatchers("/system/userreg/**").permitAll()
					.anyRequest().authenticated()
					.and()
				.formLogin()
					.loginPage("/syspublic/login")
					.loginProcessingUrl("/j_spring_security_check")
					.successHandler(new CustomSavedRequestAwareAuthenticationSuccessHandler("/secure/index", true))
					.failureUrl("/syspublic/login?errCode=IL")
					.usernameParameter("j_username")
					.passwordParameter("j_password")
					.permitAll()
					.and()
				.logout()
					.permitAll();
		}
		
		/**
		 * Handles access denied exceptions that occur when a form is submitted with a missing CSRF token due to session timeout.
		 * @return A AccessDeniedHandler implementation
		 */
		@Bean
		public AccessDeniedHandler customAccessDeniedHandler() {
			CustomAccessDeniedHandler handler = new CustomAccessDeniedHandler();
			
			handler.setInvalidSessionStrategy(invalidSessionStrategy());
			
			return handler;
		}
		
		/**
		 * Invalid session strategy that redirects to the login page when the session is invalid.
		 * @return A SimpleRedirectInvalidSessionStrategy instance
		 */
		@Bean
		public InvalidSessionStrategy invalidSessionStrategy() {
			/*
			 * Spring Security checks each request against its SessionManagementFilter, which detects expired/invalid/non-existant sessions.  If a problematic session is
			 * found, then the filter uses its configured InvalidSessionStrategy to handle the redirect.
			 * 
			 * In the simple case, the out of the box InvalidSessinStrategy sends a redirect to the login page.  We want to display a message that the session expired,
			 * so we provide our own implementation.  Our impl, PathMatchingRedirectInvalidSessionStrategy, also handles redirects for AJAX requests, which the out
			 * of the box component doesn't do.
			 * 
			 * There are some special cases where the user has an expired session and they request a specific URL. For example, the user's session expires without them
			 * knowning, and they then click the "Logout" link in the app.  We don't want to display the session expired message, so we simply forward them to the login
			 * screen.  The handlers below handle these special cases, each identified by a URL requested by the user at a time when Spring Security believes their
			 * session is invalid/non-existant.
			 */
			PathMatchingRedirectInvalidSessionStrategy strat = new PathMatchingRedirectInvalidSessionStrategy(sessionExpiredUrl() + "?errCode=SE");
			
			PathMatchingRedirectHandler loginHandler = new PathMatchingRedirectHandler("/j_spring_security_check", sessionExpiredUrl() + "?errCode=IL");
			strat.addHandler(loginHandler);
			
			PathMatchingRedirectHandler logoutHandler = new PathMatchingRedirectHandler("/logout", sessionExpiredUrl());
			strat.addHandler(logoutHandler);
			
			PathMatchingRedirectHandler reloginHandler = new PathMatchingRedirectHandler("/syspublic/login", sessionExpiredUrl());
			strat.addHandler(reloginHandler);
			
			return strat;
		}
		
		@Bean(name="sessionExpiredUrl")
		public String sessionExpiredUrl() {
			return "/syspublic/login";
		}
	}
	
	/**
	 * Use this little util for encoding a username and password for use in the HTTP Auth header.  Clients can use this header
	 * to make calls to the RESTful API.
	 * @param args
	 */
	public static void main(String[] args) {
		byte[] encodedBytes = Base64.encodeBase64("api_user:password".getBytes());

		String USER_PASS = new String(encodedBytes);
		
		System.out.println(USER_PASS);
	}

// Getters & Setters

// Attributes
	/**
	 * Custom user details service
	 */
	@Inject
	private UserService userService;
	
}
