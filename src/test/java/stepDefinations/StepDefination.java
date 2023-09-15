package stepDefinations;

import static io.restassured.RestAssured.given;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.TestDataBuild;
import resources.Utils;

public class StepDefination extends Utils {
	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;
	TestDataBuild data = new TestDataBuild();
	static String product_id;

	@Given("An Apple Product {string} with the spec {string} and price {string} is created")
	public void productCreation(String name, String CPU_model, String price) throws Exception {

		System.out.println("Scenario: Verify an item can be created");
		res = given().spec(requestSpecification()).body(data.addProductPayLoad(name, CPU_model, price));
	}

	@Given("a product id {string} to view the details")
	public void viewProduct(String id) throws Exception {
		System.out.println("");
		System.out.println("Scenario: Verify ability to return an item using Product ID");
		product_id = id;
	}

	@Given("a request to view multiple details")
	public void viewMultipleProduct() throws Exception {
		System.out.println("");
		System.out.println("Scenario: Verify ability to list multiple items");
		res = given().spec(requestSpecification());
	}

	@When("the request to add the item {string} is made")
	public void addProduct(String method) throws IOException {

		resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

		if (method.equalsIgnoreCase("POST")) {			
			response = res.when().post("/objects");
			System.out.println("Post Request Successful");
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody().asString());
		}

		else if (method.equalsIgnoreCase("GET")) {
			res = given().spec(requestSpecification());
			response = res.when().get("/objects/" + product_id + "");
			System.out.println("Get Request Successful");
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody().asString());
		} else if (method.equalsIgnoreCase("DELETE")) {
			response = res.when().delete("/objects/" + product_id + "");
			System.out.println("Delete Request Successful");
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody().asString());
		} else if (method.equalsIgnoreCase("LIST ITEMS")) {
			response = res.when().get("/objects");
			System.out.println("Get Request Successful to view Multiple Items");
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody().asString());
			String resp = response.getBody().asString();
			System.out.println("");
			ObjectMapper objectMapper = new ObjectMapper();

			try {
				// Parse the JSON array
				JsonNode jsonArray = objectMapper.readTree(resp);

				JsonNode jsonNode = jsonArray.get(12);
				String id = jsonNode.get("id").asText();
				product_id = id;
				System.out.println("Viewng Specific Item from the list of Items using Product Id");
				addProduct("GET");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Then("a {string} response code is returned")
	public void successfulResponse(String status) {
		int statuscode = Integer.parseInt(status);
		assertEquals(response.getStatusCode(), statuscode);
	}
	
	@Then("a {string} response code is not returned")
	public void unsuccessfulResponse(String status) {
		int statuscode = Integer.parseInt(status);
		assertEquals(response.getStatusCode(), statuscode);
	}

	@Then("verify product {string} created with the value {string}")
	public void verify_value_in_response_body_is(String keyValue, String Expectedvalue) {
		assertEquals(getJsonPath(response, keyValue), Expectedvalue);
	}

	@Then("delete the created product")
	public void deleteProduct() throws Exception {
		System.out.println("");
		System.out.println("Verify created item is deleted:");
		product_id = getJsonPath(response, "id");
	
		res = given().spec(requestSpecification()).queryParam("product_id", product_id);
		addProduct("DELETE");
	}

}
