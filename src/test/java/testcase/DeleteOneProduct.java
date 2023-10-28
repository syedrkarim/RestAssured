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

public class DeleteOneProduct {
	SoftAssert softassert = new SoftAssert();
	String baseUri="https://techfios.com/api-prod/api/product";
	
	String deleteProductRequestheader= "application/json; charset=UTF-8";
	String bearerToken= "Bearer bxTRYTEmvdjfhDFDGF";
	String deleteProductResponseHeader;
	String deleteProductExpectedMessage="Product was deleted.";
	
	
	HashMap<String,String> deletePayLoadMap;
	public Map <String,String> getDeletePayLoadMap(){
		deletePayLoadMap =new HashMap<String,String>();
		deletePayLoadMap.put("id", "5333");
		return deletePayLoadMap;
		
	}
	
	@Test(priority=1)
	public void deletingOneProduct() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type",deleteProductRequestheader)
			.header("Authorization",bearerToken)
			//.body(new File("src\\test\\resources\\data\\CreatePayload.json")).
			.body(getDeletePayLoadMap()).
		when()
			.delete("/delete.php").
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
		
		String deleteProductResponseHeader=response.getHeader("Content-Type");
		System.out.println("Response header Content=type "+deleteProductResponseHeader);
		softassert.assertEquals(deleteProductResponseHeader, deleteProductRequestheader,"Response header not matching");
		
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
		
		String deleteProductActualMessage= jsonPath.getString("message");
		System.out.println("Product Message "+deleteProductActualMessage);
		softassert.assertEquals(deleteProductActualMessage, deleteProductExpectedMessage,"Product messages not matching");
		
		softassert.assertAll();
	
	}
	
	
	@Test(priority=2)
	public void readingOneProduct() {
		
		
		Response response=
		given()
			.baseUri(baseUri)
			.header("Content-Type","application/json")
			.header("Authorization","Bearer bxTRYTEmvdjfhDFDGF")
			.queryParam("id", getDeletePayLoadMap().get("id")).
		when()
			.get("/read_one.php").
		then()
			.extract().response();
		
		int statusCode=response.getStatusCode();
		System.out.println("Status code is "+statusCode);
		softassert.assertEquals(statusCode, 404,"Status code not matching");
		
		String responseBody=response.getBody().asString();
		//System.out.println("Response body: "+responseBody);
		
		JsonPath jsonPath = new JsonPath(responseBody);
		
		String actualProductMessage= jsonPath.getString("message");
		System.out.println("Product Message "+actualProductMessage);
		softassert.assertEquals(actualProductMessage, "Product does not exist.","Product messages not matching");
		softassert.assertAll();
		
	}
	
	

}
