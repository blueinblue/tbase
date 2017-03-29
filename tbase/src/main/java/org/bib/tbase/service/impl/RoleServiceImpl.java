package org.bib.tbase.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.Role;
import org.bib.tbase.domain.user.RolePredicates;
import org.bib.tbase.repository.RoleRepository;
import org.bib.tbase.service.RoleService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
@Transactional
public class RoleServiceImpl extends AbstractEntityService<Role, Long> implements RoleService {
// Constructors

// Public Methods
	@Override
	public List<GrantedAuthority> toGrantedAuthorityList(Collection<Role> roles) {
		List<GrantedAuthority> authList = new LinkedList<GrantedAuthority>();
		
		for(Role role : roles) {
			authList.add(role.toGrantedAuthority());
		}
		
		return authList;
	}
	
	@Override
	public List<Role> getRolesForRoleSet(RoleSet roleSet) {
		List<Role> roleList = new LinkedList<Role>();
		
		switch(roleSet) {
			case ADMIN_USER: {
				roleList.addAll(this.findAll(RolePredicates.roleNameIn("ADMIN", "USER")));
				logger.debug("FOund roles: {}", roleList);
				break;
			}
			case REGULAR_USER: {
				roleList.addAll(this.findAll(RolePredicates.roleNameIn("USER")));
				logger.debug("FOund roles: {}", roleList);
				break;
			}
			default: {
				break;
			}
			
		}
		
		return roleList;
	}
	
// Getters & Setters
	
// Protected Methods
	@Override
	protected RoleRepository getRepo() {
		return roleRepo;
	}

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(RoleServiceImpl.class);
	
	/**
	 * Role Repository
	 */
	@Inject
	private RoleRepository roleRepo;
}
