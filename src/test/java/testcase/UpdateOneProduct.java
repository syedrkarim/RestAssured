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

public class UpdateOneProduct {
	SoftAssert softassert = new SoftAssert();
	String baseUri="https://techfios.com/api-prod/api/product";
	String createProductRequestheader= "application/json; charset=UTF-8";
	String updateProductRequestheader= "application/json; charset=UTF-8";
	String bearerToken= "Bearer bxTRYTEmvdjfhDFDGF";
	String createProductResponseHeader;
	String updateProductResponseHeader;
	String createProductActualMessage;
	String createProductExpectedMessage="Product was created.";
	String updateProductExpectedMessage="Product was updated.";
	String firstProductId;
	String updateProductId;
	HashMap<String,String> createPayLoadMap;
	HashMap<String,String> updatePayLoadMap;
	
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
	
	public Map <String,String> getUpdatePayLoadMap(){
		/*
		 {
		"name": "New perfume 1.5 By Syed",
		"price": "175",
		"description": "The best perfume",
		"category_id": "1",
		"category_name": "Fashion"
		}
		 */
		updatePayLoadMap= new HashMap<String,String>();
		updatePayLoadMap.put("id", updateProductId);
		updatePayLoadMap.put("name", "New perfume 2.5 By Syed");
		updatePayLoadMap.put("price", "275");
		updatePayLoadMap.put("description", "The best perfume");
		updatePayLoadMap.put("category_id", "1");
		updatePayLoadMap.put("category_name", "Fashion");
		return updatePayLoadMap;
		
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
		updateProductId= firstProductId;
		
		
	}
	
	@Test(priority=3)
	public void updatingOneProduct() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type",createProductRequestheader)
			.header("Authorization",bearerToken)
			//.body(new File("src\\test\\resources\\data\\CreatePayload.json")).
			.body(getUpdatePayLoadMap()).
		when()
			.put("/update.php").
		then()
			.extract().response();
		int statusCode=response.getStatusCode();
		System.out.println("Status code is "+statusCode);
		//Assert.assertEquals(statusCode, 201);
		softassert.assertEquals(statusCode, 200,"Status code not matching");
		
		/*long responseTime=response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response time is "+responseTime);
		
		if(responseTime<=2000) {
			System.out.println("Response time is within the range");
		}else {
			System.out.println("Response time is out of range");
		}*/
		
		updateProductResponseHeader=response.getHeader("Content-Type");
		System.out.println("Response header Content=type "+updateProductResponseHeader);
		softassert.assertEquals(updateProductResponseHeader, updateProductRequestheader,"Response header not matching");
		
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
		
		String updateProductActualMessage= jsonPath.getString("message");
		System.out.println("Product Message "+updateProductActualMessage);
		softassert.assertEquals(updateProductActualMessage, updateProductExpectedMessage,"Product messages not matching");
		
		softassert.assertAll();
	
	}
	@Test(priority=4)
	public void readingOneProduct() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type","application/json")
			.header("Authorization","Bearer bxTRYTEmvdjfhDFDGF")
			.queryParam("id", updateProductId).
		when()
			.get("/read_one.php").
		then()
			.extract().response();
		
		
		String responseBody=response.getBody().asString();
		//System.out.println("Response body: "+responseBody);
		
		
		
		JsonPath jsonPath = new JsonPath(responseBody);
		
		String actualproductNameFromResponse= jsonPath.getString("name");
		System.out.println("Actual Product Name "+actualproductNameFromResponse);
		softassert.assertEquals(actualproductNameFromResponse, getUpdatePayLoadMap().get("name"),"Product name not matching");
		
		String productDescription= jsonPath.getString("description");
		System.out.println("Product Description "+productDescription);
		softassert.assertEquals(productDescription, getUpdatePayLoadMap().get("description"),"Product description not matching");
		
		String productPrice= jsonPath.getString("price");
		System.out.println("Product price "+productPrice);
		softassert.assertEquals(productPrice, getUpdatePayLoadMap().get("price"),"Product price not matching");
		softassert.assertAll();
		
	}
	
	

}
