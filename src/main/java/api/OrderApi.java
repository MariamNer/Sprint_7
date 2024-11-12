package api;

import dto.Order;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class OrderApi {
    public static Response createOrder(Order order){
        JSONObject jsonObject = new JSONObject(order);
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(jsonObject)
                .when().log().all()
                .post("/api/v1/orders");
        return response;
    }
}
