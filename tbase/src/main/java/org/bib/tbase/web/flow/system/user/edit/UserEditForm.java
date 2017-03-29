package org.bib.tbase.web.flow.system.user.edit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections4.ComparatorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bib.tbase.domain.user.User;
import org.bib.tbase.domain.user.UserNote;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;
import org.pojomatic.annotations.DefaultPojomaticPolicy;

@AutoProperty(policy=DefaultPojomaticPolicy.TO_STRING)
public class UserEditForm implements Serializable {
// Constructors

// Public Methods
	@Override
	public String toString() {
		return Pojomatic.toString(this);
	}
	
	/**
	 * Get the notes collection from the current User instance.
	 * 
	 * @param isDesc If true, then the notes are returned in descending order (createdOn attrib), otherwise returned in ascending order
	 * @return A List of UserNote, possibly empty but never null
	 */
	public List<UserNote> getNotes(final boolean isDesc) {
		List<UserNote> noteList = new ArrayList<UserNote>(user.getNotes());
		
		Collections.sort(noteList, isDesc ? createdOnDesc : createdOnAsc);
		
		return noteList;
	}

// Getters & Setters
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user == null ? new User() : user;
	}

	public String getInitialComment() {
		return initialComment;
	}
	public void setInitialComment(String comment) {
		this.initialComment = comment;
	}
	
// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger
	 */
	private static final Logger logger = LogManager.getLogger(UserEditForm.class);
	
	/**
	 * User instance being edited
	 */
	@Valid
	private User user = new User();
	
	/**
	 * Initial comment entered while creating a new user account.
	 */
	private String initialComment = "";
	
// Helper Classes
	/**
	 * Compare UserNote by their createdOn attribute, ascending fashion.
	 */
	private static final Comparator<UserNote> createdOnAsc =  new Comparator<UserNote>() {
		@Override
		public int compare(UserNote o1, UserNote o2) {
			return o1.getCreatedOn().compareTo(o2.getCreatedOn());
		}
	};
	
	/**
	 * Compare UserNote by their createdOn attribute, descending fashion.
	 */
	private static final Comparator<UserNote> createdOnDesc = ComparatorUtils.reversedComparator(createdOnAsc);
}
