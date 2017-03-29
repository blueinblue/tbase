package org.bib.tbase.domain.message;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.User;
import org.joda.time.DateTime;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;

/**
 * A message between two users.
 */
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
@Entity
@Table(name="user_message")
public class UserMessage extends Message implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public UserMessage() {
		
	}
	
	/**
	 * Full
	 */
	public UserMessage(String subject, String body, User sender, User recipient) {
		super(subject, body);
		
		this.sender = sender;
		this.recipient = recipient;
	}
	
// Public Methods
	public boolean isVisible() {
		return (DateTime.now().isAfter(visibleOn.getTime()));
	}

// Getters & Setters
	@Override
	public String getDisplayName() {
		return super.getSubject();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	protected void setId(Long id) {
		this.id = id;
	}
	
	public Date getVisibleOn() {
		return visibleOn;
	}
	public void setVisibleOn(Date visibleOn) {
		this.visibleOn = visibleOn;
	}

	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}
	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserMessage.class);
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Date/time when the message becomes visible to the recipient
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="visible_on", nullable=false)
	@NotNull
	private Date visibleOn = new Date();
	
// Associations
	/**
	 * User who sent the message.
	 * 
	 * User must exist in the datastore before saving message.
	 */
	@ManyToOne
	@JoinColumn(name="sender_id", nullable=false)
	@NotNull
	private User sender;
	
	/**
	 * User who received the message
	 * 
	 * User must exist in the datastore before saving message.
	 */
	@ManyToOne
	@JoinColumn(name="recipient_id", nullable=false)
	@NotNull
	private User recipient;
}
