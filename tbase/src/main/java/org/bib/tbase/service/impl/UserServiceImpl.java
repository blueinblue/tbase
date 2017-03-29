package org.bib.tbase.service.impl;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.SecureUser;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserSearchCriteria;
import org.bib.tbase.repository.UserRepository;
import org.bib.tbase.service.RoleService;
import org.bib.tbase.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service methods for the User entity
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends AbstractEntityService<User, Long> implements UserService {
// Constructors

// Public Methods
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmailIgnoreCase(email);
		
		if(user == null) {
			throw new UsernameNotFoundException("User name or password is incorrect.");
		}
		
		return toSecureUser(user);
	}

	@Override
	public SecureUser toSecureUser(User user) {
		return new SecureUser(user.getId(), user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, roleService.toGrantedAuthorityList(user.getRoles()));
	}
	
	@Override
	public Page<User> search(UserSearchCriteria userSearchCriteria, Pageable pageable) {
		return userRepo.search(userSearchCriteria, pageable);
	}
	
	@Override
	public User findOneComplete(Long userId) {
		return userRepo.findOneCompleteById(userId);
	}
	
// Getters & Setters
	
// Protected Methods
	@Override
	protected UserRepository getRepo() {
		return userRepo;
	}

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);
	
	/**
	 * User Repository
	 */
	@Inject
	private UserRepository userRepo;
	
	/**
	 * Role Service
	 */
	@Inject
	private RoleService roleService;
}
