package org.bib.tbase.domain.message;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.Role;
import org.joda.time.DateTime;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;

/**
 * A message displayed to users that are logged into the system.
 */
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
@Entity
@Table(name="system_message")
public class SystemMessage extends Message implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public SystemMessage() {
		
	}
	
	/**
	 * Full
	 */
	public SystemMessage(String subject, String body, Date displayStart, Date displayEnd) {
		super(subject, body);
	}

// Public Methods
	public boolean isVisible(Collection<Role> viewerRoles) {
		boolean isVisible = false;
		
		/*
		 * Check dates
		 */
		final boolean startNull = (displayStart == null);
		final boolean endNull = (displayEnd == null);
		final DateTime now = DateTime.now();
		
		if(!startNull & !endNull) {
			isVisible = (now.isAfter(displayStart.getTime()) && now.isBefore(displayEnd.getTime()));
		}
		else if(!startNull & endNull) {
			isVisible = now.isAfter(displayStart.getTime());
		}
		else if(startNull & !endNull) {
			isVisible = now.isBefore(displayEnd.getTime());
		}
		else {
			isVisible = true;
		}
		
		/*
		 * Check roles (only if we're in the visible time frame)
		 */
		if(isVisible) {
			for(Role role: roles) {
				if(viewerRoles.contains(role)) {
					isVisible = true;
					break;
				}
			}
		}
		
		return isVisible;
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

	public Date getDisplayStart() {
		return displayStart;
	}
	public void setDisplayStart(Date displayStart) {
		this.displayStart = displayStart;
	}

	public Date getDisplayEnd() {
		return displayEnd;
	}
	public void setDisplayEnd(Date displayEnd) {
		this.displayEnd = displayEnd;
	}

	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles.clear();
		
		if(CollectionUtils.isNotEmpty(roles)) {
			this.roles.addAll(roles);
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
	private static final Logger logger = LogManager.getLogger(SystemMessage.class);
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Display start
	 */
	@Column(name="display_start", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date displayStart;
	
	/**
	 * Display end
	 */
	@Column(name="display_end", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date displayEnd;
	
// Associations
	/**
	 * Only display to these roles
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(joinColumns=@JoinColumn(name="sys_msg_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<Role>();
}
