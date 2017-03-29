package org.bib.tbase.repository;

import java.util.List;

import org.bib.tbase.domain.user.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Role data access
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, Long>, RoleRepositoryCustom {
	/**
	 * Find a List of Role instances that are given to regular, everday users of the system.
	 * @return A List, possibly empty but never null, of Role instances
	 */
	@Query(value="SELECT r FROM Role r WHERE r.name IN ('USER')")
	public List<Role> findRegularUserRoles();
}