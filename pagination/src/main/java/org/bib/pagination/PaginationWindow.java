package org.bib.pagination;

import java.io.Serializable;

/**
 * In a pagination component, a series of numbers are displayed representing data pages.  When the user clicks one the numbers, they are taken to that page.  This
 * class encapsulates attributes of the pager component - such as start index, current index, and end index.  It's a zero based construct, useful for the Spring Data Page type.
 * An index of 0 represents page 1 - keep that in mind for UI display purposes.
 * 
 * Start index is inclusive.  End index is inclusive.
 * 
 */
public class PaginationWindow implements Serializable {
// Constructors
	/**
	 * Construct a new PaginationWindow instance.
	 * 
	 * @param startIndex The start index, inclusive
	 * @param endIndex The end index, inclusive, must be greater than or equal to the start index
	 * @param currentIndex The current index, must fall within the start and end index
	 */
	public PaginationWindow(int startIndex, int endIndex, int currentIndex) {
		assert(startIndex <= endIndex);
		assert(currentIndex >= startIndex);
		assert(currentIndex <= endIndex);
		
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.currentIndex = currentIndex;
	}

// Public Methods
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("PaginationWindow [")
		.append("startIndex = ").append(startIndex).append(", ")
		.append("endIndex = ").append(endIndex).append(", ")
		.append("currentIndex = ").append(currentIndex)
		.append("]");
		
		return sb.toString();
	}
	
// Getters & Setters
	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Start index - zero based starting point representing first page point.
	 */
	private final int startIndex;
	
	/**
	 * End index - zero based ending point representing the last page point.  Should be used inclusively for consistency.
	 */
	private final int endIndex;
	
	/**
	 * Current index - zero based current point within the series of pages.
	 */
	private final int currentIndex;
}
