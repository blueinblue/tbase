package org.bib.tbase.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.BaseBO;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name="role")
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public class Role extends BaseBO<Long> implements Serializable {
// Constructors

// Public Methods
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}

	/**
	 * Create a new GrantedAuthority instance based on this Role instance.
	 * @return A GrantedAuthority, never null
	 */
	public GrantedAuthority toGrantedAuthority() {
		final String roleName = StringUtils.startsWith(name, "ROLE_") ? name : "ROLE_" + name;
		
		return new SimpleGrantedAuthority(roleName);
	}
	
// Displayable
	@Override
	public String getDisplayName() {
		return name;
	}
	
// Getters & Setters
	@Override
	public Long getId() {
		return id;
	}
	@Override
	protected void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(Role.class);
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	/**
	 * Role name
	 */
	@Column(nullable=false, unique=true)
	private String name;
}
