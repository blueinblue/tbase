package org.bib.tbase.domain.user;

import java.util.Date;

/**
 * Implementors will provide basic information for registering a new user.
 */
public interface UserRegistration {
	/**
	 * Get the user's email address
	 * @return A String representing the user's email address, never empty/null
	 */
	public String getEmail();
	
	/**
	 * Get the user's display name - how they are seen to other users of the system
	 * @return A String representing the user's display name, never empty/null
	 */
	public String getDisplayName();
	
	/**
	 * Get the user's password
	 * @return A String representing the user's password, never empty/null
	 */
	public String getPassword();
	
	/**
	 * Get the user's birthday (optional)
	 * 
	 * @return A Date representing the user's birthday, may be null
	 */
	public Date getBirthday();
}
