package org.bib.tbase.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.Role;
import org.bib.tbase.domain.user.SecureUser;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserRegistration;
import org.bib.tbase.repository.RoleRepository;
import org.bib.tbase.service.RoleService;
import org.bib.tbase.service.UserRegistrationService;
import org.bib.tbase.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service methods related to user registration.
 */
@Service("userRegistrationService")
public class UserRegistrationServiceImpl implements UserRegistrationService, Serializable {
// Constructors

// Public Methods
	@Override
	public void sendEmailChallengeCode(String email, String code) {
		logger.debug("Sending challenge code {} to user at email address {}", code, email);
	}

	@Override
	@Transactional
	public SecureUser registerUser(UserRegistration userReg) {
		// Look up user roles
		List<Role> roles = roleRepo.findRegularUserRoles();
		
		// Create the user
		User user = new User(userReg.getEmail(), userReg.getPassword(), userReg.getDisplayName(), roles);
		user.setBirthday(userReg.getBirthday());
		
		// Persist the user
		user = userService.save(user);
		
		// Create a SecureUser
		List<GrantedAuthority> authList = roleService.toGrantedAuthorityList(user.getRoles());
		
		SecureUser secureUser = new SecureUser(user.getId(), user.getEmail(), user.getPassword(), true, true, true, true, authList);
		
		return secureUser;
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
	private static final Logger logger = LogManager.getLogger(UserRegistrationServiceImpl.class);
	
	/**
	 * Role Repository
	 */
	@Inject
	private RoleRepository roleRepo;
	
	/**
	 * Role Service
	 */
	@Inject
	private RoleService roleService;
	
	/**
	 * User Service
	 */
	@Inject
	private UserService userService;
}
