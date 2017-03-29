package org.bib.tbase.web.flow.system.userreg;
import static org.bib.tbase.domain.user.UserPredicates.*;
import static org.bib.tbase.web.util.FlowUtil.*;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.service.UserService;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;

@Component
public class UserRegFormValidator implements Serializable {
// Constructors

// Public Methods
	/**
	 * Triggered when the user submits the user details page.
	 * 
	 * @param userRegForm The UserRegForm containing the details of the registration, never null
	 * @param context The ValidationContext instance, never null
	 */
	public void validateEnterUserDetails(UserRegForm userRegForm, ValidationContext context) {
		MessageContext messages = context.getMessageContext();
		
		// Check if email is already registered
		final String email = StringUtils.trimToEmpty(userRegForm.getEmail());
		
		if(userService.count(emailEq(email)) > 0) {
			messages.addMessage(err("email", "Email already registered"));
		}
		
		// Check if display name is already taken
		final String dispName = StringUtils.trimToEmpty(userRegForm.getDisplayName());
		
		if(userService.count(nameEq(dispName)) > 0) {
			messages.addMessage(err("displayName", "Name already registered"));
		}
		
		// Check if passwords match
		final String pass = StringUtils.trimToEmpty(userRegForm.getPassword());
		final String pass2 = StringUtils.trimToEmpty(userRegForm.getPasswordConfirm());
		
		if(StringUtils.equals(pass, pass2) == false) {
			messages.addMessage(err("password", "Passwords did not match"));
		}
	}
	
	/**
	 * Triggered when user submits email confirmation code.  Checks to make sure that the user submitted code matches what was sent to the user's email address.  Validation
	 * fails if values do not match.
	 * 
	 * @param context The ValidationContext, never null
	 */
	public void validateEnterEmailConfirmationCode(UserRegForm userRegForm, ValidationContext context) {
		MessageContext messages = context.getMessageContext();
		
		if(! StringUtils.equalsIgnoreCase(userRegForm.getEmailChallengeCode(), userRegForm.getEmailConfirmationCode())) {
			messages.addMessage(err("emailConfirmationCode", "Code does not match"));
		}
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
	private static final Logger logger = LogManager.getLogger(UserRegFormValidator.class);
	
	/**
	 * User Service
	 */
	@Inject
	private transient UserService userService;
}
