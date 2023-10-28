package testcase;

//import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
//import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
//import java.util.concurrent.TimeUnit;

public class ReadOneProduct {
	SoftAssert softassert = new SoftAssert();
	String baseUri="https://techfios.com/api-prod/api/product";
	@Test
	public void readingOneProduct() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type","application/json")
			.header("Authorization","Bearer bxTRYTEmvdjfhDFDGF")
			.queryParam("id", "5381").
		when()
			.get("/read_one.php").
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
		
		String responseHeader=response.getHeader("Content-Type");
		System.out.println("Response header Content=type "+responseHeader);
		softassert.assertEquals(responseHeader, "application/json","Response header not matching");
		
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
		
		String productName= jsonPath.getString("name");
		System.out.println("Product Name "+productName);
		softassert.assertEquals(productName, "Amazing jewel pieces By SS 2.0","Product name not matching");
		
		String productDescription= jsonPath.getString("description");
		System.out.println("Product Description "+productDescription);
		softassert.assertEquals(productDescription, "The best Diamond for everyone.","Product description not matching");
		
		String productPrice= jsonPath.getString("price");
		System.out.println("Product price "+productPrice);
		softassert.assertEquals(productPrice, "1000","Product price not matching");
		softassert.assertAll();
		
		
		
	
	}

}
