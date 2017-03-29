package org.bib.tbase.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.repository.RoleRepositoryCustom;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import com.querydsl.core.support.QueryMixin.Role;

public class RoleRepositoryImpl extends QueryDslRepositorySupport implements RoleRepositoryCustom {
// Constructors
	public RoleRepositoryImpl() {
		super(Role.class);
	}
// Predicates 
	
// Public Methods

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(RoleRepositoryImpl.class);
}
