package org.bib.tbase.service;

import org.bib.tbase.domain.user.SecureUser;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Service methods for the User entity.
 */
public interface UserService extends EntityService<User, Long>, UserDetailsService {
	/**
	 * Create a SecureUser instance from the argument User instance.
	 * 
	 * @param user The User instance, never null, must be populated with all roles and attributes
	 * @return A SecureUser instance, never null
	 */
	public SecureUser toSecureUser(User user);
	
	/**
	 * Search for User instances who match all of the argument UserSearchCriteria.
	 * 
	 * @param userSearchCriteria UserSearchCriteria, never null
	 * @param pageable Page request, may be null if no paging/sorting required
	 * @return A Page of User instances, never null
	 */
	public Page<User> search(UserSearchCriteria userSearchCriteria, Pageable pageable);
	
	/**
	 * Find one user by their primary key, eagerly fetching all associations.
	 * @param userId The primay key of the user, never empty/null
	 * @return A User instance or null if none was found
	 */
	public User findOneComplete(Long userId);
}
