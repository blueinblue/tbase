package org.bib.tbase.web.controller.dashboard.admin;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.display.Displayable;
import org.bib.tbase.domain.display.GenericDisplayable;
import org.hibernate.validator.constraints.NotBlank;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class AdminDashboardController implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public AdminDashboardController() {
		
	}

// Public Methods
	@ModelAttribute("allInterests")
	public List<Displayable> allInterests() {
		List<Displayable> interests = new LinkedList<Displayable>();
		
		interests.add(new GenericDisplayable("HIKING", "Hiking"));
		interests.add(new GenericDisplayable("BIKING", "Biking"));
		interests.add(new GenericDisplayable("CAMPING", "Camping"));
		interests.add(new GenericDisplayable("CARDS", "Playing Cards"));
		interests.add(new GenericDisplayable("EATING", "Fine Dining"));
		
		return interests;
	}
	/**
	 * Populate the admin dashboard
	 */
	@GetMapping
	public String showDashboard(Model model) {
		
		for(GrantedAuthority auth : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			logger.debug(auth.getAuthority());
		}
		
		model.addAttribute("formBean", new FormBean());
		
		return "dashboard/admin/dashboard";
	}

	@PostMapping
	public String postForm(@Valid @ModelAttribute("formBean") FormBean formBean, BindingResult bindingResult, Model model) {
		if(formBean.happy == false) {
			bindingResult.rejectValue("happy", "", "You must be happy to be here!");
		}
		
		if(bindingResult.hasErrors()) {
			return "dashboard/admin/dashboard";
		}
		
		logger.debug("formBean = {}", formBean);
		
		return "dashboard/admin/dashboard";
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
	private static final Logger logger = LogManager.getLogger(AdminDashboardController.class);
	
	@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
	public static class FormBean implements Serializable {
		// Constructors
		public FormBean() {
			
		}
		
		// Public Methods
		@Override
		public String toString() {
			return Pojomatic.toString(this);
		}
		
		// Getters & Setters
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		
		public boolean isHappy() {
			return happy;
		}
		public void setHappy(boolean happy) {
			this.happy = happy;
		}

		public List<String> getInterests() {
			return interests;
		}
		public void setInterests(List<String> interests) {
			this.interests = interests;
		}

		
		public String getPrimaryInterest() {
			return primaryInterest;
		}
		public void setPrimaryInterest(String primaryInterest) {
			this.primaryInterest = primaryInterest;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}

		// Attributes
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private Long id = 100L;
		
		@NotBlank(message="Cannot be blank")
		private String userName;
		
		@NotNull
		private boolean happy;
		
		private String password;
		
		private String color;
		
		private List<String> interests = new LinkedList<String>();
		
		private String primaryInterest;
		
		private String message;
	}
	
}
