package org.bib.tbase.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.BaseBO;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;

/**
 * An administrative note attached to a user account.
 */
@Entity
@Table(name="user_note")
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public class UserNote extends BaseBO<Long> implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public UserNote() {
	}
	
	/**
	 * Full
	 */
	public UserNote(String text) {
		setText(text);
	}

// Public Methods

// Getters & Setters
	@Override
	public String getDisplayName() {
		return "";
	}

	@Override
	public Long getId() {
		return id;
	}
	@Override
	protected void setId(Long id) {
		this.id = id;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
// Association Getters & Setters
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		if(this.user != null) {
			this.user.internalRemoveNote(this);
		}
		
		this.user = user;
		
		if(user != null) {
			user.internalAddNote(this);
		}
	}

// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserNote.class);
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Text of the note.
	 */
	@Column(columnDefinition="CLOB")
	private String text;
	
// Associations
	/**
	 * The user account targeted by this note.
	 * 
	 * This side owns the relationship.  User must exist in datastore before saving note.
	 */
	@ManyToOne
	@JoinColumn(name="user_id", insertable=true, nullable=false, updatable=false)
	@Property(policy=PojomaticPolicy.NONE)
	private User user;
}
