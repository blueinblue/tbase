package org.bib.tbase.repository.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.SecureUser;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Auditor Aware implementation that records unique key of user who made the edit.
 */
public class UserIdAuditorAware implements AuditorAware<Long> {
// Constructors

// Public Methods
	@Override
	public Long getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
 
        return ((SecureUser) authentication.getPrincipal()).getId();
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
	private static final Logger logger = LogManager.getLogger(UserIdAuditorAware.class);
}
