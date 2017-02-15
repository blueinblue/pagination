package org.bib.pagination;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class FixedWidthPaginationCalculatorTest {
// Constructors

// Public Methods
	@Test
	public void testConstructors() {
		// Default width should be greater than 0
		FixedWidthPaginationCalculator calc = new FixedWidthPaginationCalculator();
		assertTrue(calc.getWidth() > 0);
		
		// Width is set
		calc = new FixedWidthPaginationCalculator(15);
		assertEquals(15, calc.getWidth());
	}
	
	@Test
	public void testGettersAndSetters() {
		FixedWidthPaginationCalculator calc = new FixedWidthPaginationCalculator(20);
		assertEquals(20, calc.getWidth());
		
		calc.setWidth(10);
		assertEquals(10, calc.getWidth());
	}
	
	@Test
	public void testCalculateWindow() {
		/*
		 * For this test, we want a maximum of 5 page links to show, so the user would see 1 2 3 4 5, or 2 3 4 5 6, etc.
		 * The current page index is 2, which semantically corresponds to page 3 for the user.  Indexes are 0 based, pages are displayed 1 based.
		 * The page size, max elements per page, is 10.
		 * The total number of elements in complete result set is 50.
		 */
		final int MAX_PAGE_LINKS_TO_SHOW = 5;
		final int CURRENT_PAGE_INDEX = 2;
		final int PAGE_SIZE = 10;
		final int TOTAL_ELEMENTS = 50;
		
		PaginationWindow pWindow = calcPageWindow(MAX_PAGE_LINKS_TO_SHOW, CURRENT_PAGE_INDEX, PAGE_SIZE, TOTAL_ELEMENTS);
		
		assertEquals(0, pWindow.getStartIndex());
		assertEquals(2, pWindow.getCurrentIndex());
		assertEquals(4, pWindow.getEndIndex());
	}
	
	@Test
	public void testCalculateWindow_SinglePage() {
		final int MAX_PAGE_LINKS_TO_SHOW = 5;
		final int CURRENT_PAGE_INDEX = 0; 
		final int PAGE_SIZE = 20;
		final int TOTAL_ELEMENTS = 15;
		
		PaginationWindow pWindow = calcPageWindow(MAX_PAGE_LINKS_TO_SHOW, CURRENT_PAGE_INDEX, PAGE_SIZE, TOTAL_ELEMENTS);
		
		assertEquals(0, pWindow.getStartIndex());
		assertEquals(0, pWindow.getCurrentIndex());
		assertEquals(0, pWindow.getEndIndex());
	}
	
// Private Methods
	private PaginationWindow calcPageWindow(int maxPageLinksToShow, int currentPageIndex, int pageSize, int totalElements) {
		FixedWidthPaginationCalculator calc = new FixedWidthPaginationCalculator(maxPageLinksToShow);
		
		PageRequestFactory pageRequestFactory = new PageRequestFactory();
		PageRequest pageRequest = pageRequestFactory.newPageRequest(currentPageIndex, pageSize, "name:ASC,age:DESC");
		
		// Page Data
		Page<String> page = new PageImpl<String>(new LinkedList<String>(), pageRequest, totalElements);
		
		// Calculate the Window
		return calc.calculateWindow(page);
	}

// Getters & Setters

// Attributes
}
