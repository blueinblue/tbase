package org.bib.tbase.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

/**
 * Methods applicable across all entity-centric services.
 * 
 * @param <T> Entity type
 * @param <ID> Entity's ID type
 */
public interface EntityService<T, ID extends Serializable> {
	/**
	 * Find all entities.
	 * @return A List of entity type T, possibly empty but never null
	 */
	public List<T> findAll();
	
	/**
	 * Returns all entities sorted by the given options.
	 * 
	 * @param sort
	 * @return all entities sorted by the given options
	 */
	public List<T> findAll(Sort sort);

	/**
	 * Returns a {@link Page} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 * 
	 * @param pageable
	 * @return a page of entities
	 */
	public Page<T> findAll(Pageable pageable);
	
	/**
	 * Returns a single entity matching the given {@link Example} or {@literal null} if none was found.
	 *
	 * @param example can be {@literal null}.
	 * @return a single entity matching the given {@link Example} or {@literal null} if none was found.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the Example yields more than one result.
	 */
	public <S extends T> S findOne(Example<S> example);

	/**
	 * Returns all entities matching the given {@link Example}. In case no match could be found an empty {@link Iterable}
	 * is returned.
	 *
	 * @param example can be {@literal null}.
	 * @return all entities matching the given {@link Example}.
	 */
	public <S extends T> List<S> findAll(Example<S> example);

	/**
	 * Returns all entities matching the given {@link Example} applying the given {@link Sort}. In case no match could be
	 * found an empty {@link Iterable} is returned.
	 *
	 * @param example can be {@literal null}.
	 * @param sort the {@link Sort} specification to sort the results by, must not be {@literal null}.
	 * @return all entities matching the given {@link Example}.
	 * @since 1.10
	 */
	public <S extends T> List<S> findAll(Example<S> example, Sort sort);

	/**
	 * Returns a {@link Page} of entities matching the given {@link Example}. In case no match could be found, an empty
	 * {@link Page} is returned.
	 *
	 * @param example can be {@literal null}.
	 * @param pageable can be {@literal null}.
	 * @return a {@link Page} of entities matching the given {@link Example}.
	 */
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

	/**
	 * Returns the number of instances matching the given {@link Example}.
	 *
	 * @param example the {@link Example} to count instances for, can be {@literal null}.
	 * @return the number of instances matching the {@link Example}.
	 */
	public <S extends T> long count(Example<S> example);

	/**
	 * Checks whether the data store contains elements that match the given {@link Example}.
	 *
	 * @param example the {@link Example} to use for the existence check, can be {@literal null}.
	 * @return {@literal true} if the data store contains elements that match the given {@link Example}.
	 */
	public <S extends T> boolean exists(Example<S> example);
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 * 
	 * @param entity
	 * @return the saved entity
	 */
	public <S extends T> S save(S entity);

	/**
	 * Saves all given entities.
	 * 
	 * @param entities
	 * @return the saved entities
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	public <S extends T> List<S> save(Iterable<S> entities);

	/**
	 * Retrieves an entity by its id.
	 * 
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal null} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	public T findOne(ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 * 
	 * @param id must not be {@literal null}.
	 * @return true if an entity with the given id exists, {@literal false} otherwise
	 * @throws IllegalArgumentException if {@code id} is {@literal null}
	 */
	public boolean exists(ID id);

	/**
	 * Returns the number of entities available.
	 * 
	 * @return the number of entities
	 */
	public long count();

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	public void delete(ID id);

	/**
	 * Deletes a given entity.
	 * 
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	public void delete(T entity);

	/**
	 * Deletes the given entities.
	 * 
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	public void delete(Iterable<? extends T> entities);

	/**
	 * Deletes all entities managed by the repository.
	 */
	public void deleteAll();
	
	/**
	 * Returns a single entity matching the given {@link Predicate} or {@literal null} if none was found.
	 * 
	 * @param predicate can be {@literal null}.
	 * @return a single entity matching the given {@link Predicate} or {@literal null} if none was found.
	 * @throws org.springframework.dao.IncorrectResultSizeDataAccessException if the predicate yields more than one
	 *           result.
	 */
	public T findOne(Predicate predicate);

	/**
	 * Returns all entities matching the given {@link Predicate}. In case no match could be found an empty
	 * {@link Iterable} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @return all entities matching the given {@link Predicate}.
	 */
	public List<T> findAll(Predicate predicate);

	/**
	 * Returns all entities matching the given {@link Predicate} applying the given {@link Sort}. In case no match could
	 * be found an empty {@link Iterable} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @param sort the {@link Sort} specification to sort the results by, must not be {@literal null}.
	 * @return all entities matching the given {@link Predicate}.
	 * @since 1.10
	 */
	public List<T> findAll(Predicate predicate, Sort sort);

	/**
	 * Returns all entities matching the given {@link Predicate} applying the given {@link OrderSpecifier}s. In case no
	 * match could be found an empty {@link Iterable} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @param orders the {@link OrderSpecifier}s to sort the results by
	 * @return all entities matching the given {@link Predicate} applying the given {@link OrderSpecifier}s.
	 */
	public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

	/**
	 * Returns all entities ordered by the given {@link OrderSpecifier}s.
	 * 
	 * @param orders the {@link OrderSpecifier}s to sort the results by.
	 * @return all entities ordered by the given {@link OrderSpecifier}s.
	 */
	public List<T> findAll(OrderSpecifier<?>... orders);

	/**
	 * Returns a {@link Page} of entities matching the given {@link Predicate}. In case no match could be found, an empty
	 * {@link Page} is returned.
	 * 
	 * @param predicate can be {@literal null}.
	 * @param pageable can be {@literal null}.
	 * @return a {@link Page} of entities matching the given {@link Predicate}.
	 */
	public Page<T> findAll(Predicate predicate, Pageable pageable);

	/**
	 * Returns the number of instances matching the given {@link Predicate}.
	 * 
	 * @param predicate the {@link Predicate} to count instances for, can be {@literal null}.
	 * @return the number of instances matching the {@link Predicate}.
	 */
	public long count(Predicate predicate);

	/**
	 * Checks whether the data store contains elements that match the given {@link Predicate}.
	 *
	 * @param predicate the {@link Predicate} to use for the existance check, can be {@literal null}.
	 * @return {@literal true} if the data store contains elements that match the given {@link Predicate}.
	 */
	public boolean exists(Predicate predicate);
}
