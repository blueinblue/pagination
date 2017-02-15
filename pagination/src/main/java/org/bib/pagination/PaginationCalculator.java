package org.bib.pagination;

import org.springframework.data.domain.Page;

/**
 * Implementations will provide a specific algorithm for calculating a PaginationWindow (start page, end page, current page), given
 * a Page of data.
 */
public interface PaginationCalculator {
	/**
	 * Calculate and return a PaginationWindow defining the start page, end page, and current page, as determined from the
	 * argument Page instance.
	 * 
	 * @param page The Page instance, never null
	 * @return A calculated PaginationWindow, never null
	 */
	public PaginationWindow calculateWindow(Page<?> page);
}
