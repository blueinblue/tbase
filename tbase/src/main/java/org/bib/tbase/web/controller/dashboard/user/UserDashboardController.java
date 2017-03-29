package org.bib.tbase.web.controller.dashboard.user;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/secure/dashboard")
public class UserDashboardController implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public UserDashboardController() {
		
	}
	
// Public Methods
	@GetMapping
	public String showDashboard(Model model) {
		return "dashboard/user/dashboard";
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
	private static final Logger logger = LogManager.getLogger(UserDashboardController.class);
}
