package org.bib.pagination;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class PageRequestFactoryTest {
// Constructors

// Public Methods
	@Test
	public void testConstructors() {
		PageRequestFactory factory = new PageRequestFactory();
		assertTrue(factory.getDefaultPageSize() > 0);
		
		factory = new PageRequestFactory(32767);
		assertEquals(32767, factory.getDefaultPageSize());
	}
	
	@Test
	public void testGettersAndSetters() {
		PageRequestFactory factory = new PageRequestFactory();
		factory.setDefaultPageSize(309);
		
		assertEquals(309, factory.getDefaultPageSize());
	}
	
	@Test
	public void testNewPageRequest_NoSort() {
		// Number of elements per page = 10
		PageRequestFactory factory = new PageRequestFactory(10);
		
		PageRequest pageRequest = factory.newPageRequest(2, null);
		
		assertEquals(10, pageRequest.getPageSize());
		assertEquals(2, pageRequest.getPageNumber());
		assertNull(pageRequest.getSort());
	}
	
	@Test
	public void testNewPageRequest_WithSort() {
		PageRequestFactory factory = new PageRequestFactory();
		
		PageRequest pageRequest = factory.newPageRequest(3, "id:ASC,name:DESC");
		
		assertEquals(factory.getDefaultPageSize(), pageRequest.getPageSize());
		assertEquals(3, pageRequest.getPageNumber());
		
		Sort sort = pageRequest.getSort();
		assertNotNull(sort);
		
		Order idOrder = sort.getOrderFor("id");
		assertNotNull(idOrder);
		assertEquals("id", idOrder.getProperty());
		assertEquals(Direction.ASC, idOrder.getDirection());
		
		Order nameOrder = sort.getOrderFor("name");
		assertNotNull(nameOrder);
		assertEquals("name", nameOrder.getProperty());
		assertEquals(Direction.DESC, nameOrder.getDirection());
	}

// Getters & Setters

// Attributes
}
