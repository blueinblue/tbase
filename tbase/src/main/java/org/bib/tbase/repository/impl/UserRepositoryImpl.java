package org.bib.tbase.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.QRole;
import org.bib.tbase.domain.user.QUser;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserSearchCriteria;
import org.bib.tbase.repository.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPQLQuery;

public class UserRepositoryImpl extends QueryDslRepositorySupport implements UserRepositoryCustom {
// Constructors
	public UserRepositoryImpl() {
		super(User.class);
	}

// Public Methods
	@Override
	public Page<User> search(UserSearchCriteria userSearchCriteria, Pageable pageable) {
		final QUser qUser = QUser.user;
		final QRole qRole = QRole.role;
		
		// Define the from clause with join
		JPQLQuery<User> query = from(QUser.user).distinct().innerJoin(qUser.roles, qRole);
		
		// Build the predicate
		BooleanBuilder bb = new BooleanBuilder();
		
		if(StringUtils.isNotBlank(userSearchCriteria.getEmail())) {
			bb.and(QUser.user.email.containsIgnoreCase(userSearchCriteria.getEmail()));
		}
		
		if(StringUtils.isNotBlank(userSearchCriteria.getDisplayName())) {
			bb.and(QUser.user.name.containsIgnoreCase(userSearchCriteria.getDisplayName()));
		}
		
		if(userSearchCriteria.getAccountEnabled() != null) {
			bb.and(QUser.user.enabled.eq(userSearchCriteria.getAccountEnabled()));
		}

		if(CollectionUtils.isNotEmpty(userSearchCriteria.getRoles())) {
			bb.and(qRole.in(userSearchCriteria.getRoles()));
		}
		
		query.where(bb);
		
		// Pagination Support
		if(pageable != null) {
			query = super.getQuerydsl().applyPagination(pageable, query);
		}
		
		// Execute the query
		QueryResults<User> queryResults = query.fetchResults();
		
		return new PageImpl<User>(queryResults.getResults(), pageable, queryResults.getTotal());
	}
	
// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserRepositoryImpl.class);
}
