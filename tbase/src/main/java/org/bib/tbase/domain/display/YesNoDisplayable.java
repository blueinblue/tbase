package org.bib.tbase.domain.display;

/**
 * Displayable for YES/NO values.
 */
public enum YesNoDisplayable implements Displayable {
	YES(true, "Yes"), NO(false, "No");

	
// Constructors
	private YesNoDisplayable(Boolean value, String displayName) {
		this.value = value;
		this.displayName = displayName;
	}		
	
// Getters
	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}
	
// Attributes
	/**
	 * True/False value
	 */
	private final Boolean value;

	/**
	 * Display String
	 */
	private final String displayName;
}
