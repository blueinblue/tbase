package org.bib.tbase.service;

import java.util.Collection;
import java.util.List;

import org.bib.tbase.domain.user.Role;
import org.springframework.security.core.GrantedAuthority;

public interface RoleService extends EntityService<Role, Long> {
	/**
	 * Used to group roles into sets applied to common classes of users.
	 */
	public enum RoleSet {
		REGULAR_USER, ADMIN_USER;
	}
	
	/**
	 * Convert the argument collection of Role instances into a List of GrantedAuthority instances.
	 * 
	 * @param roles The Collection of Role instances, possibly empty but never null
	 * @return A List of GrantedAuthority instances, possibly empty but never null
	 */
	public List<GrantedAuthority> toGrantedAuthorityList(Collection<Role> roles);
	
	/**
	 * Get the Role instances associated with the argument RoleSet
	 * 
	 * @return A List, possibly empty but never null, of Role instances
	 */
	public List<Role> getRolesForRoleSet(RoleSet roleSet);
}
