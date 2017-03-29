package org.bib.tbase.domain.display;

/**
 * Three options - Yes, No, or Any (Either yes or no, doesn't matter)
 */
public enum YesNoAnyDisplayable implements Displayable {
	YES("Yes"), NO("No"), ANY("Either");
	
// Constructor
	private YesNoAnyDisplayable(String displayName) {
		this.displayName = displayName;
	}
	
// Getters
	@Override
	public String getDisplayName() {
		return displayName;
	}
	
	@Override
	public YesNoAnyDisplayable getValue() {
		return this;
	}
	
// Attributes
	private final String displayName;
}
