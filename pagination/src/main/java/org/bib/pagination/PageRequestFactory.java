package org.bib.pagination;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

/**
 * A Factory for creating Spring Data PageRequest instances based on page number, page size, and an optional sort string.
 * 
 * This class is threadsafe and can be registered as a singleton to be used throughout the code.
 */
public class PageRequestFactory implements Serializable {
// Constructors
	/**
	 * Default
	 */
	public PageRequestFactory() {
		
	}
	
	/**
	 * Create with non-default page size.
	 */
	public PageRequestFactory(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

// Public Methods
	/**
	 * Create a PageRequest with an optional sortStr.  The sortStr, if provided, must be in the format: propertyName: direction,propertyName: direction
	 * Example: id: ASC,name: DESC
	 * 
	 * @param pageNumber The page number to request
	 * @param pageSize The size of the page
	 * @param sortStr The sort string, may be empty/null
	 * @return A PageRequest, never null
	 */
	public PageRequest newPageRequest(int pageNumber, int pageSize, String sortStr) {
		PageRequest pageRequest = null;
		
		/*
		 * Create sort if necessary.
		 * 
		 * Sort params come in as: propertyName: direction,propertyName: direction
		 */
		List<Order> orderList = new LinkedList<Order>();
		
		Sort sort = null;
		String[] propertySorts = StringUtils.split(sortStr, ',');
		
		if(ArrayUtils.isNotEmpty(propertySorts)) {
			for(String propertySort : propertySorts) {
				String[] sortSplit = StringUtils.split(propertySort, ':');
				
				if(ArrayUtils.getLength(sortSplit) == 2) {
					String propertyName = StringUtils.trimToEmpty(sortSplit[0]);
					String direction = StringUtils.trimToEmpty(sortSplit[1]);
					
					Order order = new Order(Direction.fromString(direction), propertyName);
					
					orderList.add(order);
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(orderList)) {
			sort = new Sort(orderList);
		}
		
		if(sort == null) {
			pageRequest = new PageRequest(pageNumber, pageSize);
		}
		else {
			pageRequest = new PageRequest(pageNumber, pageSize, sort);
		}
		
		return pageRequest;
	}
	
	/**
	 * Calls newPageRequest(int, int, String) but uses default page size.
	 * 
	 * @param pageNumber The page number to request
	 * @param sortStr The sort string, may be empty/null
	 * @return A PageRequest instance, never null
	 */
	public PageRequest newPageRequest(int pageNumber, String sortStr) {
		return newPageRequest(pageNumber, defaultPageSize, sortStr);
	}
	
// Getters & Setters
	public int getDefaultPageSize() {
		return defaultPageSize;
	}
	public void setDefaultPageSize(int defaultPageSize) {
		this.defaultPageSize = defaultPageSize;
	}

// Attributes
	/**
	 * Serialziation
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default page size to use if caller doesn't specify
	 */
	private int defaultPageSize = 10;
}
