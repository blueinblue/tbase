package org.bib.tbase.repository;

import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {
	/**
	 * Search for User instances who match all of the argument UserSearchCriteria.
	 * 
	 * @param userSearchCriteria UserSearchCriteria, never null
	 * @param pageable Page request, may be null if no paging/sorting required
	 * @return A Page of User instances, never null
	 */
	public Page<User> search(UserSearchCriteria userSearchCriteria, Pageable pageable);
}
