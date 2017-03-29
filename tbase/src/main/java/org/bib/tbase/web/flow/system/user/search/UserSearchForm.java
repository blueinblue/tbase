package org.bib.tbase.web.flow.system.user.search;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.display.YesNoAnyDisplayable;
import org.bib.tbase.domain.user.Role;
import org.bib.tbase.domain.user.UserSearchCriteria;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;

/**
 * User search criteria.
 */
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public class UserSearchForm implements UserSearchCriteria, Serializable {
// Constructors
	/**
	 * Default
	 */
	public UserSearchForm() {
		
	}

// Public Methods
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

// Getters & Setters
	@Override
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public YesNoAnyDisplayable getEnabled() {
		return enabled;
	}
	public void setEnabled(YesNoAnyDisplayable enabled) {
		this.enabled = enabled;
	}
	@Override
	public Boolean getAccountEnabled() {
		Boolean isEnabled = null;
		
		if(this.enabled != null && this.enabled != YesNoAnyDisplayable.ANY) {
			isEnabled = this.enabled == YesNoAnyDisplayable.YES ? true : false;
		}
		
		return isEnabled;
	}

	@Override
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserSearchForm.class);
	
// Form Fields
	/**
	 * Email address - contains 
	 */
	private String email;
	
	/**
	 * Display name - contains
	 */
	private String displayName;
	
	/**
	 * Enabled flag
	 */
	private YesNoAnyDisplayable enabled = YesNoAnyDisplayable.ANY;
	
	/**
	 * Roles to search by
	 */
	private List<Role> roles = new LinkedList<Role>();
	
}
