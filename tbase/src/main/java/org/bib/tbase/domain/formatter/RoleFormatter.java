package org.bib.tbase.domain.formatter;

import java.util.Locale;

import javax.inject.Inject;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.Role;
import org.bib.tbase.service.RoleService;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 * Convert between a String representation of a Role and a Role instance - and vice versa.
 */
@Component
public class RoleFormatter implements Formatter<Role> {
// Constructors

// Public Methods
	@Override
	public String print(Role role, Locale locale) {
		return String.valueOf(role.getId());
	}

	@Override
	public Role parse(String roleIdStr, Locale locale) throws ParseException {
		Role role = null;
		
		if(NumberUtils.isCreatable(roleIdStr)) {
			role = roleService.findOne(Long.valueOf(roleIdStr));
		}
		
		return role;
	}
	
// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(RoleFormatter.class);
	
	/**
	 * Role Service
	 */
	@Inject
	private RoleService roleService;
}
