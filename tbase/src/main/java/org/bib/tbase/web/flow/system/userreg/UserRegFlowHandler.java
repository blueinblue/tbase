package org.bib.tbase.web.flow.system.userreg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.SecureUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.FlowExecutionOutcome;
import org.springframework.webflow.mvc.servlet.AbstractFlowHandler;

/**
 * A Flow Handler to provide custom handling of the user registration flow outcomes.
 */
@Component("system/userreg")
public class UserRegFlowHandler extends AbstractFlowHandler {
// Constructors

// Public Methods
	@Override
	public String handleExecutionOutcome(FlowExecutionOutcome outcome, HttpServletRequest request, HttpServletResponse response) {
		// The end state, registrationCompleted is reached so the user successfully registered.  Here we log them in and redirect them to the index page.
		if (outcome.getId().equals("registrationCompleted")) {
			SecureUser secureUser = (SecureUser) outcome.getOutput().get("secureUser");
			
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(secureUser, secureUser.getPassword(), secureUser.getAuthorities());
			authToken.setDetails(new WebAuthenticationDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		
		return "/";
	}
// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserRegFlowHandler.class);
}
