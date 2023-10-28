package testcase;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.path.json.JsonPath;
//import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import java.util.concurrent.TimeUnit;

public class ReadAllProducts {
	String baseUri="https://techfios.com/api-prod/api/product";
	@Test
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
		int statusCode=response.getStatusCode();
		System.out.println("Status code is "+statusCode);
		long responseTime=response.getTimeIn(TimeUnit.MILLISECONDS);
		System.out.println("Response time is "+responseTime);
		
		if(responseTime<=2000) {
			System.out.println("Response time is within the range");
		}else {
			System.out.println("Response time is out of range");
		}
		
		String responseHeader=response.getHeader("Content-Type");
		System.out.println("Response header Content=type "+responseHeader);
		Assert.assertEquals(responseHeader, "application/json; charset=UTF-8");
		
		String responseBody=response.getBody().asString();
		//System.out.println("Response body: "+responseBody);
		
		JsonPath jsonPath = new JsonPath(responseBody);
		String firstProductId= jsonPath.getString("records[0].id");
		System.out.println("First Product Id is "+firstProductId);
		
		if(firstProductId!=null) {
			System.out.println("Records are not null");
		}else {
			System.out.println("Records are null");
		}
		
		
	
	}

}
