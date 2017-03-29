package org.bib.tbase.web.flow.system.user.edit;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserNote;
import org.bib.tbase.service.UserService;
import org.bib.tbase.web.util.FlowUtil;
import org.springframework.stereotype.Component;
import org.springframework.webflow.execution.RequestContext;

@Component("userEditAction")
public class UserEditAction implements Serializable {
// Constructors

// Public Methods
	public UserEditForm createUserEditForm(Long userId) {
		UserEditForm userEditForm = new UserEditForm();
		
		if(userId != null) {
			User user = userService.findOneComplete(userId);
			
			if(user == null) {
				throw new RuntimeException("Failed to find user with ID: " + userId);
			}
			
			userEditForm.setUser(user);
			
		}
		
		return userEditForm;
	}
	
	public String saveUser(UserEditForm userEditForm, RequestContext context) {
		String outcome = "success";
		
		try {
			/*
			 * If we are creating a brand new user and the creator entered an initial comment, create one.
			 */
			if(userEditForm.getUser().getId() == null && StringUtils.isNotBlank(userEditForm.getInitialComment())) {
				UserNote userNote = new UserNote(userEditForm.getInitialComment());
				
				userEditForm.getUser().addNote(userNote);
			}
			
			User savedUser = userService.save(userEditForm.getUser());
			
			userEditForm.setUser(savedUser);
			
			context.getFlashScope().put("msg", "User saved.");
		}
		catch(Exception e) {
			logger.error("An error occured while saving user.", e);
			
			context.getFlashScope().put("errMsg", "Failed to save user.");
			
			outcome = "error";
		}
		
		return outcome;
	}
	
	/**
	 * AJAX handler method for saving an account note.
	 * 
	 * @param userEditForm The user edit form, never null
	 * @param context The RequestContext, never null
	 * @return A String representing the outcome of the action (success/error)
	 */
	public String saveNote(UserEditForm userEditForm, RequestContext context) {
		UserNote userNote = null;
		
		try {
			userNote = FlowUtil.readJson(context, UserNote.class);
		}
		catch(Exception e) {
			throw new RuntimeException("Failed to read JSON UserNote data.");
		}
		
		userEditForm.getUser().addNote(userNote);
		
		return saveUser(userEditForm, context);
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
	private static final Logger logger = LogManager.getLogger(UserEditAction.class);
	
	/**
	 * User Service
	 */
	@Inject
	private UserService userService;
}
