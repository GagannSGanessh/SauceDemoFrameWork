package tests;

import base.ApiBaseClass;
import endpoints.Routes;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payloads.LoginCredentials;

import static org.hamcrest.Matchers.lessThan;

public class ComplexApiTests extends ApiBaseClass {

    // 1. Complex Data-Driven Provider for multiple payloads
    @DataProvider(name = "InvalidLoginProvider")
    public Object[][] invalidLoginData() {
        return new Object[][] {
            { "peter@klaven", "" }, 
            { "", "cityslicka" },
            { "invalid@user.com", "randompass" }
        };
    }

    // 2. Data Driven Auth Test Loop
    @Test(dataProvider = "InvalidLoginProvider", priority = 1)
    public void authDataDrivenTest(String email, String password) {
        LoginCredentials payload = new LoginCredentials(email, password);

        RestAssured.given()
                .spec(requestSpec)
                .body(payload)
                .when()
                .post(Routes.POST_LOGIN)
                .then()
                .statusCode(400); // Reqres standard bad request for invalid auth
    }

    // 3. Sever Latency Hard-constraint
    @Test(priority = 2)
    public void serverTimeoutThresholdTest() {
        // Enforces a failure if the server breaches threshold limit
        RestAssured.given()
                .spec(requestSpec)
                .queryParam("delay", "3")
                .when()
                .get(Routes.GET_USERS_DELAYED)
                .then()
                .statusCode(200)
                .time(lessThan(5000L)); // Must return within 5 seconds despite 3s synthetic delay
    }

    // 4. Advanced JSON Schema Validation
    @Test(priority = 3)
    public void jsonSchemaStructuralIntegrityTest() {
        // Reqres /api/users?page=2
        String expectedSchema = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"required\": [\"page\", \"per_page\", \"total\", \"data\"],\n" +
                "  \"properties\": {\n" +
                "    \"page\": {\"type\": \"integer\"},\n" +
                "    \"total\": {\"type\": \"integer\"}\n" +
                "  }\n" +
                "}";

        RestAssured.given()
                .spec(requestSpec)
                .queryParam("page", "2")
                .when()
                .get(Routes.GET_USERS_DELAYED)
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchema(expectedSchema));
    }

    // 5. Deliberate HTML Request Trace Failure (API Screenshot equivalent)
    @Test(priority = 4)
    public void generateInteractiveApiFailureReportTest() {
        // Attempt a registration logic but deliberately fail the code check expectation 
        // to invoke the TestListener and output an API Request Trace Report.
        LoginCredentials payload = new LoginCredentials("eve.holt@reqres.in", "pistol");

        Response response = RestAssured.given()
                .spec(requestSpec)
                .body(payload)
                // log().ifValidationFails() ensures the raw REST string cascades to the exception!
                .log().ifValidationFails() 
                .when()
                .post("/api/register");
                
        // Intentionally crash: expected 401, but reqres returns 200 properly here.
        response.then()
            .statusCode(401); 
    }
}
