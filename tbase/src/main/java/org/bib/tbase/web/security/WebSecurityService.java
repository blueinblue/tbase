package org.bib.tbase.web.security;

import org.bib.tbase.domain.user.SecureUser;

/**
 * Security Methods
 */
public interface WebSecurityService {
	/**
	 * Programatically login the argument user, creating a SecurityContext and valid session for the user.
	 * @param secureUser The SecureUser instance to login, never null
	 */
	public void login(SecureUser secureUser);
}
