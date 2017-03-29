package org.bib.tbase.web.security;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.SecureUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Web Security Service implementation.
 */
@Service("webSecurityService")
public class WebSecurityServiceImpl implements WebSecurityService {
// Constructors

// Public Methods
	@Override
	public void login(SecureUser secureUser) {
		Authentication auth = new UsernamePasswordAuthenticationToken(secureUser, secureUser.getPassword(), secureUser.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		// Create a new session and add the security context.
//	    HttpSession session = request.getSession(true);
//	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
	}
	
// Getters & Setters

// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(WebSecurityServiceImpl.class);
}
