package org.bib.tbase.domain.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.Length;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;

/**
 * A reuseable message fragment for administrative messages.
 */
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
@Entity
@Table(name="stock_message")
public class StockMessage extends Message implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public StockMessage() {
		
	}
	
	/**
	 * Full
	 */
	public StockMessage(String displayName, String subject, String body) {
		super(subject, body);
		
		this.displayName = displayName;
	}

// Public Methods

// Getters & Setters
	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public Long getId() {
		return id;
	}
	@Override
	protected void setId(Long id) {
		this.id = id;
	}
		
// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(StockMessage.class);
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Short description
	 */
	@Column(name="display_name")
	@NotNull
	@Length(min=1, max=30, message="Must be shorter than {max} characters")
	private String displayName;
}
