package org.bib.pagination;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaginationWindowTest {
// Constructors

// Public Methods
	@Test
	public void testConstructors() {
		PaginationWindow pWin = new PaginationWindow(10, 20, 15);
		
		assertEquals(10, pWin.getStartIndex());
		assertEquals(20, pWin.getEndIndex());
		assertEquals(15, pWin.getCurrentIndex());
	}
	
	@Test(expected=AssertionError.class)
	public void testContructorsAssertionError() {
		new PaginationWindow(20, 10, 15);
	}
	
	@Test
	public void testGetters() {
		PaginationWindow pWin = new PaginationWindow(0, 10, 4);
		
		assertEquals(0, pWin.getStartIndex());
		assertEquals(10, pWin.getEndIndex());
		assertEquals(4, pWin.getCurrentIndex());
	}

// Getters & Setters

// Attributes
}
