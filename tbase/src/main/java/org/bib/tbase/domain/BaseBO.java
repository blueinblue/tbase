package org.bib.tbase.domain;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.display.Displayable;
import org.bib.tbase.domain.user.Role;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;
import org.pojomatic.annotations.PojomaticPolicy;
import org.pojomatic.annotations.Property;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseBO<ID extends Serializable> implements Serializable, Displayable {
// Constructors

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
		/*
		 * This is a workaround for an issue with the way Spring EL handles equality.  Here's an example:
		 * 
		 *  class MyEntity {
		 *  	private Set<MyOtherEntity> otherEntities;
		 *  }
		 *  
		 *  This assumes that a formatter has been registered to convert a String representation of MyOtherEntity to an
		 *  instance of MyOtherEntity.
		 *  
		 *  Now assume you have a checkbox group on a UI page that shows all possible MyOtherEntity instances.  The idea
		 *  is that the user checks each MyOtherEntity instance that should be associated with the MyEntity being edited.
		 *  
		 *  When you submit the form, the Spring SelectValueComparator executes to determine which instances are selected.
		 *  Because we've got a Formatter registered, the SelectValueComparator uses the exhuastive compare method along
		 *  with the PropertyEditor (really a wrapped version of the Formatter).  Look at the method:
		 *  
		 *  exhaustiveCompare(Object boundValue, Object candidate, PropertyEditor editor, Map<PropertyEditor, Object> convertedValueCache)
		 *  
		 *  The boundValue points to an instance in the otherEntities attribute of the MyEntity instance.  The candidate argument points to a single
		 *  selected MyOtherEntity instance (which was checked by the user on the UI).  The PropertyEditor, for whatever reason,
		 *  sees that the otherEntities attribute is a Set, so it wraps the boundValue in a LinkedHashSet collection wrapper.  It then
		 *  passes that LinkedHashSet into the MyOtherEntity's equals method.  Now, the linked hash set may contain an instance
		 *  equal to the candidateValue, but the equals method will always return false because the instance is wrapped in the
		 *  LinkedHashSet.  It doesn't work and the checkbox isn't interrpreted as checked.
		 *  
		 *  To get around this, we check to see if the argument to equals is a collection wrapper (meaning that it is a collection type
		 *  with a single instance).  We then extract this instance and use that for the equality check.
		 */
		
		Object targetObj = obj;
		
		if(isCollection(obj) && CollectionUtils.size(obj) == 1) {
			targetObj = CollectionUtils.get(obj, 0);
		}
		
		return Pojomatic.equals(this, targetObj);
	}
	
// Displayable Implementation
	@Override
	public ID getValue() {
		return getId();
	}
	
	@Override
	public abstract String getDisplayName();
	
// Private Methods
	private static boolean isCollection(Object ob) {
		return ob != null && isClassCollection(ob.getClass());
	}
	
	private static boolean isClassCollection(Class<?> c) {
		return Collection.class.isAssignableFrom(c) || Map.class.isAssignableFrom(c);
	}
	
// Getters & Setters
	/**
	 * Get the unique key for this entity.
	 * @return Unique key, may be null
	 */
	public abstract ID getId();
	/**
	 * Set the unique key for this entity - should not be called directly
	 * @param id The id to set, may be null
	 */
	protected abstract void setId(ID id);
	
	/**
	 * Get the object identifier for this entity
	 * @return A String, never empty/null
	 */
	public String getObjectId() {
		return objectId;
	}
	/**
	 * Set the object identifer for this entity - should not be called directly
	 * @param objectId A String, never empty/null
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	
	/**
	 * Get the version of this entity
	 * @return A Long, never null
	 */
	public Long getVersion() {
		return version;
	}
	
	/**
	 * Get the unique key of the User who created this entity
	 * @return A Long, may be null
	 */
	public Long getCreatedBy() {
		return createdBy;
	}
	
	/**
	 * Get the date the entity was created.
	 * @return A Date, never null
	 */
	public Date getCreatedOn() {
		return createdOn;
	}
	
	/**
	 * Get the unique key of the User who last modified this entity
	 * @return A Long, may be null
	 */
	public Long getUpdatedBy() {
		return updatedBy;
	}
	
	/**
	 * Get the date the entity was last modified
	 * @return A Date, may be null
	 */
	public Date getUpdatedOn() {
		return updatedOn;
	}

// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(BaseBO.class);
	
	/**
	 * Optimistic locking version
	 */
	@Version
	private Long version;
	
	/**
	 * Object Identifier
	 */
	@Property(policy=PojomaticPolicy.ALL)
	@Column(name="object_id", insertable=true, nullable=false, updatable=false, unique=true)
	private String objectId = UUID.randomUUID().toString();
	
	/**
	 * User primary key who first persisted this instance
	 */
	@Column(name="created_by", nullable=true, insertable=true, updatable=false)
	@CreatedBy
	private Long createdBy;
	
	/**
	 * When was this instance first persisted?
	 */
	@Column(name="created_on", nullable=false, insertable=true, updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdOn = new Date();
	
	/**
	 * User primary key who last persisted this instance
	 */
	@Column(name="updated_by", nullable=true, insertable=true, updatable=true)
	@LastModifiedBy
	private Long updatedBy;
	
	/**
	 * When was this instance last persisted?
	 */
	@Column(name="updated_on", nullable=true, insertable=true, updatable=true)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedOn = new Date();
}
