package org.bib.tbase.domain.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.BaseBO;
import org.hibernate.validator.constraints.NotBlank;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;

/**
 * A registered user of the system.
 */
@Entity
@Table(name="user")
@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public class User extends BaseBO<Long> implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public User() {
	}
	
	/**
	 * Full
	 */
	public User(String email, String password, String name, List<Role> roles) {
		this.email = email;
		this.name = name;
		this.password = password;
		this.roles.addAll(roles);
	}

// Public Methods
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}
	
// Displayable
	@Override
	public String getDisplayName() {
		return email;
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
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Collection<Role> roles) {
		this.roles.clear();
		
		if(CollectionUtils.isNotEmpty(roles)) {
			this.roles.addAll(roles);
		}
	}
	public void addRole(Role role) {
		roles.add(role);
	}
	public void removeRole(Role role) {
		roles.remove(role);
	}
	
	public String getRolesAsString() {
		StringBuilder sb = new StringBuilder();
		
		for(Role role : roles) {
			if(sb.length() > 0) {
				sb.append(", ");
			}
			
			sb.append(role.getName());
		}
		
		return sb.toString();
	}
	
// Association Getters & Setters
	public Set<UserNote> getNotes() {
		return Collections.unmodifiableSet(notes);
	}
	public void removeNote(UserNote note) {
		note.setUser(null);
	}
	public void addNote(UserNote note) {
		note.setUser(this);
	}
	
	void internalAddNote(UserNote note) {
		notes.add(note);
	}
	void internalRemoveNote(UserNote note) {
		notes.remove(note);
	}
	
// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(User.class);
	
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	/**
	 * User name = email
	 */
	@Column(nullable=false, unique=true)
	@NotBlank(message="Required field")
	private String email;
	
	/**
	 * Display name - how the user appears to other users
	 */
	@Column(name="display_name", nullable=false, unique=true)
	@NotBlank(message="Required field")
	private String name;
	
	/**
	 * Password
	 */
	@Column(nullable=false)
	@NotBlank(message="Required field")
	private String password;
	
	/**
	 * Birthday
	 */
	@Column(nullable=true)
	@Temporal(TemporalType.DATE)
	private Date birthday;
	
	/**
	 * Is enabled?
	 */
	private boolean enabled = true;
	
// Associations
	/**
	 * Roles assigned to this user
	 */
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(joinColumns=@JoinColumn(name="user_id"), inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<Role>();
	
	/**
	 * Notes attached to this user account
	 */
	@OneToMany(mappedBy="user", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Set<UserNote> notes = new HashSet<UserNote>();
}
