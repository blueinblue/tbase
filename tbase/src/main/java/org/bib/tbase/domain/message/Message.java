package org.bib.tbase.domain.message;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.BaseBO;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;

@MappedSuperclass
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public abstract class Message extends BaseBO<Long> implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public Message() {
		
	}

	/**
	 * Full
	 */
	public Message(String subject, String body) {
		this.subject = subject;
		this.body = body;
	}
	
// Public Methods
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

// Getters & Setters
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

// Attributes
	/**
	 * Serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(Message.class);
	
	/**
	 * Subject
	 */
	private String subject;
	
	/**
	 * Body
	 */
	private String body;
	
}
