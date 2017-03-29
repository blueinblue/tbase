package org.bib.tbase.domain.display;

/**
 * Implementations will provide a human readable display name (displayName) mapped to a unique value.  Useful for entities that need
 * to be displayed in a drop down list of like entities.
 */
public interface Displayable {
	/**
	 * Get the value of this instance.  Perhaps the ID or the entity itself.
	 * 
	 * @return A value, never null
	 */
	public Object getValue();
	
	/**
	 * A human readable description of this instance.  Should be short as it's used for display purposes in perhaps limited width environments.
	 * @return A String, never empty/null
	 */
	public String getDisplayName();
}
