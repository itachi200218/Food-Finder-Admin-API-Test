package org.API;

import io.restassured.response.Response;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Full Stack Food Finder Admin Management System")
@Feature("API Automation with Allure Reports")
public class FoodFinderTest {

    Response res;
    Map<String, String> headers;

    @BeforeClass
    @Description("Setup Base URI and Headers")
    public void setup() {
        ApiResuable.uri("http://localhost:8080"); // your Spring Boot port
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
    }

    // -------------------- AUTH CONTROLLER TESTS --------------------

    @Test(description = "POST - Register a new user")
    @Severity(SeverityLevel.CRITICAL)
    public void registerUser() {
        String payload = """
                {
                    "username":"chetan1111",
                    "email":"chetan1111@example.com",
                    "password":"1111"
                }
                """;
        executeAndVerify(new ApiResuable("/api/auth/register", "POST", payload));
    }

    @Test(description = "POST - Login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    public void loginUser() {
        String payload = """
                {
                    "username":"chetan",
                    "password":"1111"
                }
                """;
        executeAndVerify(new ApiResuable("/api/auth/login", "POST", payload));
    }

    @Test(description = "GET - Check Login Status")
    @Severity(SeverityLevel.NORMAL)
    public void checkLogin() {
        executeAndVerify(new ApiResuable("/api/auth/check", "GET", null));
    }

    @Test(description = "GET - Logout user")
    @Severity(SeverityLevel.MINOR)
    public void logoutUser() {
        executeAndVerify(new ApiResuable("/api/auth/logout", "GET", null));
    }

    @Test(description = "GET - Fetch All Users (Admin)")
    @Severity(SeverityLevel.CRITICAL)
    public void getAllUsersAuth() {
        executeAndVerify(new ApiResuable("/api/auth/users", "GET", null));
    }

    @Test(description = "POST - Forgot Username by Email")
    @Severity(SeverityLevel.NORMAL)
    public void forgotUsername() {
        String payload = """
                {
                    "email":"chetan@gmail.com"
                }
                """;
        executeAndVerify(new ApiResuable("/api/auth/forgot-username", "POST", payload));
    }

    @Test(description = "POST - Reset Password")
    @Severity(SeverityLevel.NORMAL)
    public void resetPassword() {
        String payload = """
                {
                    "username":"chetan",
                    "currentPassword":"1111",
                    "newPassword":"1111"
                }
                """;
        executeAndVerify(new ApiResuable("/api/auth/reset-password", "POST", payload));
    }

    @Test(description = "PUT - Update Password without Old Password")
    @Severity(SeverityLevel.NORMAL)
    public void updatePasswordWithoutOld() {
        String payload = """
                {
                    "password":"1111"
                }
                """;
        executeAndVerify(new ApiResuable("/api/auth/updatePassword/testUser", "PUT", payload));
    }

    @Test(description = "GET - Get Profile by Username")
    @Severity(SeverityLevel.NORMAL)
    public void getProfile() {
        executeAndVerify(new ApiResuable("/api/auth/profile/testUser", "GET", null));
    }

    // -------------------- ADMIN CONTROLLER --------------------

    @Test(description = "GET - Get All Users (AdminController)")
    @Severity(SeverityLevel.NORMAL)
    public void getAllUsersFromUserController1() {
        executeAndVerify(new ApiResuable("/api/users", "GET", null));
    }

    // -------------------- DASHBOARD CONTROLLER --------------------

    @Test(description = "GET - Dashboard Statistics")
    @Severity(SeverityLevel.NORMAL)
    public void getDashboardStats() {
        executeAndVerify(new ApiResuable("/api/dashboard/stats", "GET", null));
    }

    // -------------------- GEMINI CONTROLLER --------------------

    @Test(description = "GET - Gemini AI Ask Endpoint")
    @Severity(SeverityLevel.CRITICAL)
    public void geminiAsk1() {
        executeAndVerify(new ApiResuable("/api/admin/gemini/ask?query=write a code in java for palindrome", "GET", null));
    }
    @Test(description = "GET - Gemini AI Ask Endpoint for DB Questions")
    @Severity(SeverityLevel.CRITICAL)
    public void geminiAsk2() {
        executeAndVerify(new ApiResuable("/api/admin/gemini/ask?query=Show database structure", "GET", null));
    }
    @Test(description = "GET - Gemini AI Ask Endpoint for Creating recipe")
    @Severity(SeverityLevel.CRITICAL)
    public void geminiAsk3() {
        executeAndVerify(new ApiResuable("/api/admin/gemini/ask?query=Create recipe butter naan category 1", "GET", null));
    }
    @Test(description = "GET - Get All Users (GeminiController)")
    @Severity(SeverityLevel.NORMAL)
    public void getAllGeminiUsers() {
        executeAndVerify(new ApiResuable("/api/admin/gemini/users", "GET", null));
    }

    // -------------------- USER CONTROLLER --------------------

    @Test(description = "GET - Get All Users (UserController)")
    @Severity(SeverityLevel.NORMAL)
    public void getAllUsersFromUserController() {
        executeAndVerify(new ApiResuable("/api/users", "GET", null));
    }

    @Test(description = "GET - Get User by ID")
    @Severity(SeverityLevel.NORMAL)
    public void getUserById() {
        executeAndVerify(new ApiResuable("/api/users/1", "GET", null));
    }

//    @Test(description = "POST - Create a New User")
//    @Severity(SeverityLevel.CRITICAL)
//    public void createUser() {
//        String payload = """
//                {
//                    "name":"John Doe",
//                    "category":"Veg"
//                }
//                """;
//        executeAndVerify(new ApiResuable("/api/users", "POST", payload));
//    }

//    @Test(description = "PUT - Update Existing User")
//    @Severity(SeverityLevel.NORMAL)
//    public void updateUser() {
//        String payload = """
//                {
//                    "name":"John Doe Updated",
//                    "category":"Non-Veg"
//                }
//                """;
//        executeAndVerify(new ApiResuable("/api/users/1", "PUT", payload));
//    }

    @Test(description = "DELETE - Delete User by ID")
    @Severity(SeverityLevel.MINOR)
    public void deleteUser() {
        executeAndVerify(new ApiResuable("/api/users/1", "DELETE", null));
    }

    // -------------------- GENERIC EXECUTION METHOD --------------------

    @Step("Execute {api.method} request for endpoint: {api.endpoint}")
    public void executeAndVerify(ApiResuable api) {
        res = ApiResuable.execute(api, headers);
        int statusCode = res.getStatusCode();
        String body = res.getBody().asPrettyString();

        Allure.step("Method: " + api.getMethod());
        Allure.step("Endpoint: " + api.getEndpoint());
        Allure.step("Status Code: " + statusCode);
        Allure.addAttachment("Response for " + api.getEndpoint(), "application/json", body, ".json");

        System.out.println("➡️ " + api.getMethod() + " " + api.getEndpoint());
        System.out.println("Status Code: " + statusCode);
        System.out.println("Response: " + body);

        Assert.assertTrue(statusCode == 200 || statusCode == 201 || statusCode == 204,
                "❌ Expected 200/201/204 but got " + statusCode + " for " + api.getEndpoint());
    }
}
