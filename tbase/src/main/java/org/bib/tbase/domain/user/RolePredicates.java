package org.bib.tbase.domain.user;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.querydsl.core.types.Predicate;

public class RolePredicates {
// Constructors

// Public Methods
	/**
	 * Role name matches one of the argument role names.
	 * @param roleNames Strings representing role names
	 * @return A Predicate instance, never null
	 */
	public static Predicate roleNameIn(String ... roleNames) {
		return QRole.role.name.toUpperCase().in(Collections2.transform(Arrays.asList(roleNames), new Function<String, String>() {
			@Override
			public String apply(String input) {
				return StringUtils.upperCase(input);
			}
			
		}));
	}
	
	/**
	 * Role name equals argument roleName (ignore case)
	 * @param roleName The role name, may be empty/null
	 * @return A Predicate
	 */
	public static Predicate roleNameEq(String roleName) {
		return QRole.role.name.equalsIgnoreCase(roleName);
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
	private static final Logger logger = LogManager.getLogger(RolePredicates.class);
}
