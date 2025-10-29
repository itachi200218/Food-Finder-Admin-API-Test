package org.API;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ApiResuable {
    private String endpoint;
    private String method;
    private String payload;

    public ApiResuable(String endpoint, String method, String payload) {
        this.endpoint = endpoint;
        this.method = method;
        this.payload = payload;
    }

    public String getEndpoint() { return endpoint; }
    public String getMethod() { return method; }
    public String getPayload() { return payload; }

    public static void uri(String baseURI) {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI = baseURI;
    }

    public static Response execute(ApiResuable api, Map<String, String> headers) {
        RequestSpecification request = RestAssured.given().headers(headers);
        if (api.getPayload() != null) request.body(api.getPayload());
        return request.when().request(Method.valueOf(api.getMethod()), api.getEndpoint()).then().extract().response();
    }
}
