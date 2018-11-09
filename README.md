# Pagination
Pagination is a small library built to simplify the creation of pagination links when using Spring Data's PageRequest to page/sort a dataset.

# Quick Start
In your Spring @Config class, register an implementation of the PaginationCalculator.

```java
	@Bean("paginationCalc")
	public PaginationCalculator paginationCalc() {
		// 10 = Maximum/preferred number of page links to show in a pagination component
		PaginationCalculator pageCalc = new FixedWidthPaginationCalculator(10);
		
		return pageCalc;
	}
```

To simplify the creation of PageRequest instances that you can pass to Spring Data's query methods, register a PageRequestFactory in your Spring config.

```java
	@Bean("pageRequestFactory")
	public PageRequestFactory pageRequestFactory() {
		// 20 = Default number of elements to display in a page if not otherwise specified
		PageRequestFactory prFactory = new PageRequestFactory(20);
		
		return prFactory;
	}
```

In your Spring MVC Controller Methods that use Spring Data's PageRequest binding, you can then use your PaginationCalc to calculate a PaginationWindow that your page can use to back pagination links.  This is particularly handy for the Bootstrap Pagination component.  Here's a sample Spring MVC controller method:

```java
	@PostMapping
	public String search(CrimJimsCitationSearchForm searchForm, PageRequest pageRequest, ModelMap modelMap) {
		// Execute the paged query
		Page<MyDataObj> pageData = myService.findAll(pageRequest);
		
		// Calc the pagination window
		PaginationWindow pageWindow = pagerCalc.calculateWindow(pageData);
		
		// Put data and window in model
		modelMap.put("page", pageData);
		modelMap.put("window", pageWindow);
		
		return "myViewName";
	}
```

In your view, if using Bootstrap (with Thymeleaf and Spring WebFlow in this case), you can then use your PaginationWindow to back your links:
```html
	<ul class="pagination text-center">
			<li th:classappend="${page.hasPrevious()} ? _ : disabled">
				<a class="pager-link" th:classappend="${page.hasPrevious()} ? _ : disabled" th:href="@{'~' + ${flowExecutionUrl}(_eventId=pageRequest,ajaxSource='true',pageNumber=${page.previousPageable()?.pageNumber},sort=${page.sort})}">&laquo;</a>
			</li>
			
			<li th:each="i, iterStat : ${#numbers.sequence(window.startIndex, window.endIndex)}" th:classappend="${window.currentIndex eq i} ? active : _">
				<a class="pager-link" th:href="@{'~' + ${flowExecutionUrl}(_eventId=pageRequest,ajaxSource='true',pageNumber=${i},sort=${page.sort})}" th:text="${i + 1}"></a>
			</li>
			
			<li th:classappend="${page.hasNext()} ? _ : disabled">
				<a class="pager-link" th:classappend="${page.hasNext()} ? _ : disabled" th:href="@{'~' + ${flowExecutionUrl}(_eventId=pageRequest,ajaxSource='true',pageNumber=${page.nextPageable()?.pageNumber},sort=${page.sort})}">&raquo;</a>
			</li>
		</ul>
```

You can also use the PageRequestFactory to simplify turning request parameters into PageRequest instances.  In the example below, I'm using Spring WebFlow to handle the AJAX request from the pagination component above:

```xml
	<transition on="pageRequest" bind="false">
		<render fragments="my-table-fragment"></render>
		
		<evaluate expression="pageRequestFactory.newPageRequest(requestParameters.pageNumber, requestParameters.sort)" result="flowScope.pageRequest" />
		
		<evaluate expression="mySearchAction.search(searchForm, pageRequest, flowRequestContext)" />
	</transition>
```

# Class Overview
- **PaginationCalculator**: Implement this interface to provide your own calculation of PaginationWindow spans.  Basically, given a Page of data, your algorithm will compute the start page index (zero based, inclusive) and the end page index (zero based, inclusive).  You can then use iteration to layout the number of page links you want to show in your pagination component.  There are many styles of pagination, so it's up to you how you'd like to shift the window left and right as the user clicks the page links.

- **PaginationWindow**: A POJO that represents the span of page links to display.  The start index (zero based, inclusive) represents the first page and the end index (zero based, inclusive) represents the end index.  So a PaginationWindow with start=5 and end=10, could be used to display page links like "6 7 8 9 10".  When the user clicks the link for page 9, for example, you'd recalculate the pagination window to show links "8 9 10 11 12".  The implementation of how to move the window is up to your implementation of the PaginationCalculator.  PaginationWindow also holds the current page, so you can easily modify the style of the current page's link.  Don't forget that the Window is zero based and you'll mostly like want to show your users "one" based links.

- **FixedWidthPaginationCalculator**: A simple implementation of a PaginationCalculator that always tries to show up to x number of page links, regardless of the current page.  So if the user is on the last page of the dataset, and the width is specified as 5, then links to the prior four pages will be displayed.

- **PageRequestFactory**: A helper class that can create Spring Data PageRequest instances.  It knows how to parse the sort parameters as long as they are provided in the format "property:direction, property:direction".  For example, a single column sort should be passed as, "myProp:ASC" or "myProp:DESC".  A two column sort should be passed as "myPropOne:ASC, myPropTwo:DESC".
