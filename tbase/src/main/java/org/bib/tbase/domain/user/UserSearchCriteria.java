package org.bib.tbase.domain.user;

import java.util.List;

/**
 * User Search Criteria
 */
public interface UserSearchCriteria {
	/**
	 * Get email search criteria
	 * @return A String, may be empty/null
	 */
	public String getEmail();
	
	/**
	 * Get display name search criteria
	 * @return A String, may be empty/null
	 */
	public String getDisplayName();
	
	/**
	 * Get enabled search criteria
	 * @return A Boolean, may be null
	 */
	public Boolean getAccountEnabled();
	
	/**
	 * Get the roles to search by
	 * @return A List, may be empty/null
	 */
	public List<Role> getRoles();
}
