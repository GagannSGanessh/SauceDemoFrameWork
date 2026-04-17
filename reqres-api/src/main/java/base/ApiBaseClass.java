package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApiBaseClass {
    public static RequestSpecification requestSpec;

    static {
        Properties properties = new Properties();
        try {
            FileInputStream input = new FileInputStream("src/test/resources/api-config.properties");
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load api-config.properties");
        }

        RestAssured.baseURI = properties.getProperty("baseUrl");

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "PostmanRuntime/7.28.4") // spoof typical authorized client
                .addFilter(new RequestLoggingFilter()) // Globally log every request natively
                .addFilter(new ResponseLoggingFilter()) // Globally log every response natively
                .build();
    }
}
