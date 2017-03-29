package org.bib.tbase.service;

import org.bib.tbase.domain.user.SecureUser;
import org.bib.tbase.domain.user.UserRegistration;

/**
 * Service for registering users.
 */
public interface UserRegistrationService {
	/**
	 * E-mail the argument email address the argument code for the purpose of validating their email address.
	 * 
	 * @param email The email address to send the message to, never empty/null
	 * @param code The code to send, never empty/null
	 */
	public void sendEmailChallengeCode(String email, String code);
	
	/**
	 * Register a new user with the details supplied by the argument UserRegistration.
	 * 
	 * @param userReg The UserRegistration instance, never null
	 * @return A SecureUser representing the newly registered user
	 */
	public SecureUser registerUser(UserRegistration userReg);
}
