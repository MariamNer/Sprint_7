package api;

import dto.Courier;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class LoginCourierApi {
    public static Response loginCourier(Courier courier) {
        JSONObject jsonObject = new JSONObject(courier);
        jsonObject.remove("firstName");
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(jsonObject.toString())
                .when().log().all()
                .post("/api/v1/courier/login");
        return response;
    }
}