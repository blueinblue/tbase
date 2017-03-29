package org.bib.tbase.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Common base for all Spring Data repositories.
 * @param <T> The entity type handled by this repository
 * @param <ID> The id type of the entity
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {
	/**
	 * Find an entity by its objectId attribute.
	 * @param objectId The objectId, never null
	 * @return An entity of type T with that object id, never null
	 * @throws NoResultException if no entity of type T exists
	 */
	public T findByObjectId(String objectId);
}
