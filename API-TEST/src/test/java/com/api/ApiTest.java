package com.api;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiTest {

    private static final String BASE_URL = "https://bfhldevapigw.healthrx.co.in/automation-campus";
    private static final String CREATE_USER_ENDPOINT = "/create/user";
    
    public static void main(String[] args) {
        // Set base URI
        RestAssured.baseURI = BASE_URL;

        // Test cases
        testValidUserCreation();
        testDuplicatePhoneNumber();
        testDuplicateEmailId();
        testMissingRollNumber();
        testMissingFirstName();
        testInvalidPhoneNumber();
        testInvalidEmailId();
    }

    private static void testValidUserCreation() {
        System.out.println("Running test: testValidUserCreation");
        Response response = given()
                .header("roll-number", "1")
                .header("Content-Type", "application/json")
                .body("{\"firstName\": \"Test\", \"lastName\": \"User\", \"phoneNumber\": 8888888888, \"emailId\": \"test.user@example.com\"}")
                .when()
                .post(CREATE_USER_ENDPOINT);

        response.then().statusCode(200); // Expecting 200 OK
        System.out.println("Response: " + response.asString());
    }

    private static void testDuplicatePhoneNumber() {
        System.out.println("Running test: testDuplicatePhoneNumber");
        Response response = given()
                .header("roll-number", "1")
                .header("Content-Type", "application/json")
                .body("{\"firstName\": \"Duplicate\", \"lastName\": \"Phone\", \"phoneNumber\": 8888888888, \"emailId\": \"duplicate.phone@example.com\"}")
                .when()
                .post(CREATE_USER_ENDPOINT);

        response.then().statusCode(400); // Expecting 400 Bad Request
        System.out.println("Response: " + response.asString());
    }

    private static void testDuplicateEmailId() {
        System.out.println("Running test: testDuplicateEmailId");
        Response response = given()
                .header("roll-number", "1")
                .header("Content-Type", "application/json")
                .body("{\"firstName\": \"Duplicate\", \"lastName\": \"Email\", \"phoneNumber\": 7777777777, \"emailId\": \"test.user@example.com\"}")
                .when()
                .post(CREATE_USER_ENDPOINT);

        response.then().statusCode(400); // Expecting 400 Bad Request
        System.out.println("Response: " + response.asString());
    }

    private static void testMissingRollNumber() {
        System.out.println("Running test: testMissingRollNumber");
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{\"firstName\": \"Missing\", \"lastName\": \"Roll\", \"phoneNumber\": 6666666666, \"emailId\": \"missing.roll@example.com\"}")
                .when()
                .post(CREATE_USER_ENDPOINT);

        response.then().statusCode(401); // Expecting 401 Unauthorized
        System.out.println("Response: " + response.asString());
    }

    private static void testMissingFirstName() {
        System.out.println("Running test: testMissingFirstName");
        Response response = given()
                .header("roll-number", "1")
                .header("Content-Type", "application/json")
                .body("{\"lastName\": \"NoFirstName\", \"phoneNumber\": 5555555555, \"emailId\": \"nofirstname@example.com\"}")
                .when()
                .post(CREATE_USER_ENDPOINT);

        response.then().statusCode(400); // Expecting 400 Bad Request
        System.out.println("Response: " + response.asString());
    }

    private static void testInvalidPhoneNumber() {
        System.out.println("Running test: testInvalidPhoneNumber");
        Response response = given()
                .header("roll-number", "1")
                .header("Content-Type", "application/json")
                .body("{\"firstName\": \"Invalid\", \"lastName\": \"Phone\", \"phoneNumber\": \"12345\", \"emailId\": \"invalid.phone@example.com\"}")
                .when()
                .post(CREATE_USER_ENDPOINT);

        response.then().statusCode(400); // Expecting 400 Bad Request
        System.out.println("Response: " + response.asString());
    }

    private static void testInvalidEmailId() {
        System.out.println("Running test: testInvalidEmailId");
        Response response = given()
                .header("roll-number", "1")
                .header("Content-Type", "application/json")
                .body("{\"firstName\": \"Invalid\", \"lastName\": \"Email\", \"phoneNumber\": 4444444444, \"emailId\": \"invalid-email\"}")
                .when()
                .post(CREATE_USER_ENDPOINT);

        response.then().statusCode(400); // Expecting 400 Bad Request
        System.out.println("Response: " + response.asString());
    }
}

