package org.bib.tbase.repository;

import org.bib.tbase.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

/**
 * User data access
 */
@Repository
public interface UserRepository extends BaseRepository<User, Long>, UserRepositoryCustom {
	/**
	 * Find a single user by their unique email address, ignore case.
	 * 
	 * @param email The email address, never empty/null
	 * @return A User instance or null if none was found
	 */
	public User findByEmailIgnoreCase(String email);
	
	/**
	 * Find one user by their primary key, eagerly fetching all associations.
	 * @param userId The primay key of the user, never empty/null
	 * @return A User instance or null if none was found
	 */
	@EntityGraph(attributePaths={"notes"})
	public User findOneCompleteById(Long userId);
}
