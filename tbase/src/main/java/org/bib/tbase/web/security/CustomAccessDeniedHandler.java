package org.bib.tbase.web.security;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.logging.log4j.*;
import org.springframework.security.access.*;
import org.springframework.security.web.access.*;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.session.*;

/**
 * An extension of the Spring Security AccessDeniedHandlerImpl that will forward to a login page if the user POSTS a form without a CSRF token.  This typically happens
 * when the user submits a form after their session has expired.
 */
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {
// Constructors
	public CustomAccessDeniedHandler() {
	}

// Public Methods
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (!response.isCommitted()) {
			// If the CSRF Token is missing, then the user submitted a form without a valid session - most likely session timed out
			if(accessDeniedException instanceof MissingCsrfTokenException) {
				logger.debug("Access denied because of missing CSRF token.");
				
				invalidSessionStrategy.onInvalidSessionDetected(request, response);
			}
			else {
				super.handle(request, response, accessDeniedException);
			}
		}
	}

// Getters & Setters
	public InvalidSessionStrategy getInvalidSessionStrategy() {
		return invalidSessionStrategy;
	}
	public void setInvalidSessionStrategy(InvalidSessionStrategy invalidSessionStrategy) {
		this.invalidSessionStrategy = invalidSessionStrategy;
	}
	
// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(CustomAccessDeniedHandler.class);
	
	/**
	 * InvalidSessionStrategy implementation to handle session expired events.
	 */
	private InvalidSessionStrategy invalidSessionStrategy = new SimpleRedirectInvalidSessionStrategy("/");
}
