package testcase;

//import org.testng.Assert;
//import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
//import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

//import java.io.File;
import java.util.HashMap;
import java.util.Map;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeUnit;

public class CreateOneProduct {
	SoftAssert softassert = new SoftAssert();
	String baseUri="https://techfios.com/api-prod/api/product";
	String createProductRequestheader= "application/json; charset=UTF-8";
	String bearerToken= "Bearer bxTRYTEmvdjfhDFDGF";
	String createProductResponseHeader;
	String createProductActualMessage;
	String createProductExpectedMessage="Product was created.";
	String firstProductId;
	HashMap<String,String> createPayLoadMap;
	
	public Map <String,String> getCreatePayLoadMap(){
		/*
		 {
		"name": "New perfume 1.5 By Syed",
		"price": "175",
		"description": "The best perfume",
		"category_id": "1",
		"category_name": "Fashion"
		}
		 */
		createPayLoadMap= new HashMap<String,String>();
		createPayLoadMap.put("name", "New perfume 1.5 By Syed");
		createPayLoadMap.put("price", "175");
		createPayLoadMap.put("description", "The best perfume");
		createPayLoadMap.put("category_id", "1");
		createPayLoadMap.put("category_name", "Fashion");
		return createPayLoadMap;
		
	}
	
	@Test(priority=1)
	public void creatingOneProduct() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type",createProductRequestheader)
			.header("Authorization",bearerToken)
			//.body(new File("src\\test\\resources\\data\\CreatePayload.json")).
			.body(getCreatePayLoadMap()).
		when()
			.post("/create.php").
		then()
			.extract().response();
		int statusCode=response.getStatusCode();
		System.out.println("Status code is "+statusCode);
		//Assert.assertEquals(statusCode, 201);
		softassert.assertEquals(statusCode, 201,"Status code not matching");
		
		/*long responseTime=response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response time is "+responseTime);
		
		if(responseTime<=2000) {
			System.out.println("Response time is within the range");
		}else {
			System.out.println("Response time is out of range");
		}*/
		
		createProductResponseHeader=response.getHeader("Content-Type");
		System.out.println("Response header Content=type "+createProductResponseHeader);
		softassert.assertEquals(createProductResponseHeader, createProductRequestheader,"Response header not matching");
		
		String responseBody=response.getBody().asString();
		//System.out.println("Response body: "+responseBody);
		
		/*
		    "id": "5381",
            "name": "Amazing jewel pieces By SS 2.0",
            "description": "The best Diamond for everyone.",
            "price": "1000",
            "category_id": "1",
            "category_name": "Fashion"
		 */
		
		JsonPath jsonPath = new JsonPath(responseBody);
		
		createProductActualMessage= jsonPath.getString("message");
		System.out.println("Product Message "+createProductActualMessage);
		softassert.assertEquals(createProductActualMessage, createProductExpectedMessage,"Product messages not matching");
		
		softassert.assertAll();
	
	}
	
	@Test(priority=2)
	public void readingAllProducts() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type","application/json; charset=UTF-8")
			.auth().preemptive().basic("demo@techfios.com", "abc123").
		when()
			.get("/read.php").
		then()
			.extract().response();
		
		
		String responseBody=response.getBody().asString();
		
		JsonPath jsonPath = new JsonPath(responseBody);
		firstProductId= jsonPath.getString("records[0].id");
		System.out.println("First Product Id is "+firstProductId);
		
		
	}
	@Test(priority=3)
	public void readingOneProduct() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type","application/json")
			.header("Authorization","Bearer bxTRYTEmvdjfhDFDGF")
			.queryParam("id", firstProductId).
		when()
			.get("/read_one.php").
		then()
			.extract().response();
		
		
		String responseBody=response.getBody().asString();
		//System.out.println("Response body: "+responseBody);
		
		
		
		JsonPath jsonPath = new JsonPath(responseBody);
		
		String actualproductNameFromResponse= jsonPath.getString("name");
		System.out.println("Actual Product Name "+actualproductNameFromResponse);
		softassert.assertEquals(actualproductNameFromResponse, getCreatePayLoadMap().get("name"),"Product name not matching");
		
		String productDescription= jsonPath.getString("description");
		System.out.println("Product Description "+productDescription);
		softassert.assertEquals(productDescription, getCreatePayLoadMap().get("description"),"Product description not matching");
		
		String productPrice= jsonPath.getString("price");
		System.out.println("Product price "+productPrice);
		softassert.assertEquals(productPrice, getCreatePayLoadMap().get("price"),"Product price not matching");
		softassert.assertAll();
		
	}

}
