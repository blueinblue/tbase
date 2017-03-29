package org.bib.tbase.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.bib.tbase.repository.BaseRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

public class BaseRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements BaseRepository<T, ID> {
// Constructors
	public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager em) {
		super(entityInformation, em);
		
		this.em = em;
	}

// Public Methods
	@Override
	public T findByObjectId(String objectId) {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(super.getDomainClass());
		
		Root<T> root = criteriaQuery.from(super.getDomainClass());
		
		Predicate objIdPredicate = criteriaBuilder.equal(root.get("objectId"), objectId);
		
		criteriaQuery.where(objIdPredicate);
		
		return em.createQuery(criteriaQuery).getSingleResult();
	}

// Getters & Setters

// Attributes
	/**
	 * Entity Manager
	 */
	protected final EntityManager em;

}
