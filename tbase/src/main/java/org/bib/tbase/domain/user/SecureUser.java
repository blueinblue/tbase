package org.bib.tbase.domain.user;

import java.io.Serializable;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A wrapper around the Spring Security User implementation that allows us to store a bit more information about the user.
 */
public class SecureUser extends User implements Serializable, UserDetails {
// Constructors
	public SecureUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		
		this.id = userId;
	}
	
// Public Methods

// Getters & Setters
	public Long getId() {
		return id;
	}
	
// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(SecureUser.class);
	
	/**
	 * Primary key of User model object that backs this secure user.
	 */
	private Long id;
}
