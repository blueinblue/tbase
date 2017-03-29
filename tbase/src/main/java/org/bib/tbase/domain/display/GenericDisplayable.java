package org.bib.tbase.domain.display;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

/**
 * A Generic Displayable implementation for easy use in drop downs.
 */
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public class GenericDisplayable implements Displayable, Serializable {
// Constructors
	public GenericDisplayable(Object value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}

// Public Methods
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}
	
	@Override
	public int hashCode() {
		return Pojomatic.hashCode(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return Pojomatic.equals(this, obj);
	}
	
// Getters & Setters
	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(GenericDisplayable.class);

	/**
	 * Value
	 */
	@Property(policy=PojomaticPolicy.ALL)
	private Object value;
	
	/**
	 * Display Name
	 */
	private String displayName;
}
