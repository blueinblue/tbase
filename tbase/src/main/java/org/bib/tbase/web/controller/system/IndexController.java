package org.bib.tbase.web.controller.system;

import java.io.*;

import org.apache.logging.log4j.*;
import org.springframework.security.core.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.*;

@Controller
public class IndexController implements Serializable {
// Constructors

// Public Methods
	/**
	 * Handles a request to the context root of the application.  Redirects to the /secure/index handler. 
	 * @return
	 */
	@RequestMapping(value={"/", ""}, method=RequestMethod.GET)
	public RedirectView doUnsecureGet() {
		return new RedirectView("/secure/index", true);
	}
	
	/**
	 * Determine which dashboard to display based on currently logged in user
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value="/secure/index", method=RequestMethod.GET)
	public String doGet(Authentication authentication) {
		return "redirect:" + getDashboardViewName(authentication);
	}
	
	/**
	 * Dynamically find dashboard and return view name - no redirect
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value="/secure/home", method=RequestMethod.GET)
	public String doGetWithoutRedirect(Authentication authentication) {
		// Use a forward to invoke the dashboard controller rather than simply rendering the view
		return "forward:" + getDashboardViewName(authentication);
	}
	
// Protected Methods
	protected String getDashboardViewName(Authentication authentication) {
		// Default to user dashboard
		String viewName = "/secure/dashboard";
		
		// Check for admin
		boolean isAdmin = false;
		
		for(GrantedAuthority auth : authentication.getAuthorities()) {
			if("ROLE_ADMIN".equalsIgnoreCase(auth.getAuthority())) {
				isAdmin = true;
				break;
			}
		}
		
		if(isAdmin) {
			viewName = "/admin/dashboard";
		}
		
		return viewName;
	}

// Getters & Setters

// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(IndexController.class);
}
