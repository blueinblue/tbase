package org.bib.tbase.web.controller.system;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Spring MVC Controller for handling user authentication.
 */
@Controller
@RequestMapping("/syspublic")
public class LoginController implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public LoginController() {
		
	}

// Public Methods
	/**
	 * Show the login form to allow the user to authenticate with the system.
	 * 
	 * @param errorCode A String constant representing the error that triggered the authentication request, may be empty/null
	 * @param model The Model instance, never null
	 * @return A String representing the logical view name for the login form, never empty/null
	 */
	@RequestMapping("/login")
	public String showLoginForm(@RequestParam(name="errCode", required=false) String errorCode, Model model) {
		/*
		 * The user may have been redirected to the login form due to the following errors, identified by the String based errorCode argument:
		 * - SE: Session expired, so the user needs to login again to continue.
		 * - IL: Invalid login.  The user entered an invalid username/password and needs to try again.
		 * - ??: Any other error code reported by the system that requires re-authentication
		 * 
		 * If any of these errors occur, a message will be provided under the loginErrMsg key, corresponding a message key in the application's MessageResource bundle.
		 */
		final String ERROR_MSG_KEY = "errMsg";
		
		if(StringUtils.isNotBlank(errorCode)) {
			switch(StringUtils.upperCase(StringUtils.trim(errorCode))) {
				case "SE": {
					model.addAttribute(ERROR_MSG_KEY, "session.expired");
					break;
				}
				case "IL": {
					model.addAttribute(ERROR_MSG_KEY, "invalid.login");
					break;
				}
				default: {
					model.addAttribute(ERROR_MSG_KEY, "error.requires.reauth");
					break;
				}
			}
		}
		
		return "system/login";
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
	private static final Logger logger = LogManager.getLogger(LoginController.class);
}
