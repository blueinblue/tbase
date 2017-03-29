package org.bib.tbase.domain.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.querydsl.core.types.Predicate;

public class UserPredicates {
// Constructors
	private UserPredicates() {
	}
	
// Public Methods
	/**
	 * Email equals (ignore case)
	 * @param email
	 * @return
	 */
	public static Predicate emailEq(String email) {
		return QUser.user.email.equalsIgnoreCase(email);
	}
	
	/**
	 * Name equals (ignore case)
	 * @param name
	 * @return
	 */
	public static Predicate nameEq(String name) {
		return QUser.user.name.equalsIgnoreCase(name);
	}
	
// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserPredicates.class);
}
