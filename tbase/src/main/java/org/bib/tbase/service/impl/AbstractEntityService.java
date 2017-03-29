package org.bib.tbase.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.repository.BaseRepository;
import org.bib.tbase.service.EntityService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;

public abstract class AbstractEntityService<T, ID extends Serializable> implements EntityService<T, ID> {
// Constructors

// Public Methods
	@Override
	public List<T> findAll() {
		return getRepo().findAll();
	}

	@Override
	public List<T> findAll(Sort sort) {
		return getRepo().findAll(sort);
	}

	@Override
	public Page<T> findAll(Pageable pageable) {
		return getRepo().findAll(pageable);
	}

	@Override
	public <S extends T> S findOne(Example<S> example) {
		return getRepo().findOne(example);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example) {
		return getRepo().findAll(example);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
		return getRepo().findAll(example, sort);
	}

	@Override
	public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
		return getRepo().findAll(example, pageable);
	}

	@Override
	public <S extends T> long count(Example<S> example) {
		return getRepo().count(example);
	}

	@Override
	public <S extends T> boolean exists(Example<S> example) {
		return getRepo().exists(example);
	}

	@Override
	public <S extends T> S save(S entity) {
		return getRepo().save(entity);
	}

	@Override
	public <S extends T> List<S> save(Iterable<S> entities) {
		return getRepo().save(entities);
	}

	@Override
	public T findOne(ID id) {
		return getRepo().findOne(id);
	}

	@Override
	public boolean exists(ID id) {
		return getRepo().exists(id);
	}

	@Override
	public long count() {
		return getRepo().count();
	}

	@Override
	public void delete(ID id) {
		getRepo().delete(id);
	}

	@Override
	public void delete(T entity) {
		getRepo().delete(entity);
	}

	@Override
	public void delete(Iterable<? extends T> entities) {
		getRepo().delete(entities);
	}

	@Override
	public void deleteAll() {
		getRepo().deleteAll();
	}

	@Override
	public T findOne(Predicate predicate) {
		return getRepo().findOne(predicate);
	}

	@Override
	public List<T> findAll(Predicate predicate) {
		return IteratorUtils.toList(getRepo().findAll(predicate).iterator());
	}

	@Override
	public List<T> findAll(Predicate predicate, Sort sort) {
		return IteratorUtils.toList(getRepo().findAll(predicate, sort).iterator());
	}

	@Override
	public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
		return IteratorUtils.toList(getRepo().findAll(predicate, orders).iterator());
	}

	@Override
	public List<T> findAll(OrderSpecifier<?>... orders) {
		return IteratorUtils.toList(getRepo().findAll(orders).iterator());
	}

	@Override
	public Page<T> findAll(Predicate predicate, Pageable pageable) {
		return getRepo().findAll(predicate, pageable);
	}

	@Override
	public long count(Predicate predicate) {
		return getRepo().count(predicate);
	}

	@Override
	public boolean exists(Predicate predicate) {
		return getRepo().exists(predicate);
	}
	
// Protected Methods
	/**
	 * Get the BaseRepository implementation responsible for entity type T.
	 * @return
	 */
	protected abstract BaseRepository<T, ID> getRepo();

// Getters & Setters

// Attributes
	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(AbstractEntityService.class);
}
