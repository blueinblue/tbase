package org.bib.tbase.web.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/syspublic/error")
public class ErrorController {
// Constructors

// Public Methods
	@GetMapping
	public Object generalError() {
		return new ModelAndView("errors/general_error");
	}
	
	@RequestMapping(value="/accessDenied", method=RequestMethod.GET)
	public String accessDenied(HttpServletRequest request, ModelMap modelMap) {
		
		// Context Path of Requested Resource
		modelMap.put("req_context_path", request.getRequestURI());
		
		return "errors/access_denied";
	}

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(ErrorController.class);
}
