  Feature: Validating Product API's
  @Product
  Scenario:  Verify an item can be created
	Given An Apple Product "Apple MacBook Pro 16" with the spec "Intel Core i9" and price "1849.99" is created
	When the request to add the item "POST" is made
	Then a "200" response code is returned
	And verify product "name" created with the value "Apple MacBook Pro 16"
	Then delete the created product

	@Product
	Scenario: Ability to return an item
	Given a product id "3" to view the details 
	When the request to add the item "GET" is made
	Then a "200" response code is returned
	
	@Product
	Scenario: Ability to list multiple items
	Given a request to view multiple details 
	When the request to add the item "LIST ITEMS" is made
	Then a "200" response code is returned
	
	@Product
	Scenario: Ability not to return an item with incorrect ID
	Given a product id "1983" to view the details 
	When the request to add the item "GET" is made
	Then a "200" response code is not returned
	