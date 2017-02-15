package org.bib.pagination;

import java.io.Serializable;

import org.springframework.data.domain.Page;

/**
 * Calculates a PaginationWindow starting and ending indexes while maintaining a fixed number of pages.
 * 
 * For example, if the calc's width attribute is 10, then the calculator will always return starting and ending indexes that are at most 10 units apart.  If there are
 * 15 pages, then a sliding window of 10 pages will be returned.  If there are fewer then 10 pages, then the page count defines the window size.
 * 
 * This class is thread safe and can be registered as a singleton to be used throughout the code.
 * 
 * PageWindow calculations will have the start and end index as inclusive values.  For example, if you had 30 elements, 10 per page, with a current page of 0, then
 * the PageWindow would have: startIndex = 0, endIndex = 2, currentIndex = 0.  The code for generating the page links should treat the start and end as inclusive points,
 * thus showing page 1, 2, 3, corresponding to index 0, 1, 2. 
 */
public class FixedWidthPaginationCalculator implements PaginationCalculator, Serializable {
// Constructors
	/**
	 * Default
	 */
	public FixedWidthPaginationCalculator() {
	}
	
	/**
	 * Create a calculator with the specified width.
	 * @param width An integer greater than 0 representing the number of page links to always represent
	 */
	public FixedWidthPaginationCalculator(int width) {
		this.width = width;
	}
	
// Public Methods
	/* (non-Javadoc)
	 * @see org.bib.pagination.PaginationCalculator#calculateWindow(org.springframework.data.domain.Page)
	 */
	public PaginationWindow calculateWindow(Page<?> page) {
		int leftIndex = page.getNumber();
		int rightIndex = page.getNumber();
		int slotsRemaining = width - 1;
		
		while(slotsRemaining > 0) {
			boolean movedLeft = false;
			boolean movedRight = false;
			
			if(leftIndex - 1 >= 0) {
				leftIndex--;
				slotsRemaining--;
				movedLeft = true;
			}
			
			if(slotsRemaining > 0 && rightIndex + 1 < page.getTotalPages()) {
				rightIndex++;
				slotsRemaining--;
				movedRight = true;
			}
			
			if(movedLeft || movedRight) {
				continue;
			}
			else {
				break;
			}
		}
		
		return new PaginationWindow(leftIndex, rightIndex, page.getNumber());
	}
	
// Getters & Setters
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Width - maximum/preferred number of page links to display.  Defaults to 10.
	 */
	private int width = 10;
}
