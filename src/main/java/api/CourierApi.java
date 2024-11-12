package api;

import dto.Courier;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.oneOf;


public class CourierApi {

    public static Response createCourier(Courier courier){
        JSONObject jsonObject = new JSONObject(courier);
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(jsonObject.toString())
                .when().log().all()
                .post("/api/v1/courier");
        return response;
    }

}
