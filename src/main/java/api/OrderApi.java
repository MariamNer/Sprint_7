package api;

import dto.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class OrderApi {
    @Step
    public static Response createOrder(Order order){
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(order)
                .when().log().all()
                .post("/api/v1/orders");
        return response;
    }

//    @Step
//    public static Response getOrders(Integer track){
//                Response response2 = given()
//                .header("Content-type", "application/json")
//                .header("api_key", "special-key")
//                .get("/v1/orders/track?t" + track);
//        response2.then().log().all()
//                .statusCode(SC_OK);
//        return response2;
//    }

    @Step
    public static Response cancelOrder(Integer track){

        JSONObject jsonObject = new JSONObject("track");
        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(jsonObject)
                .when().log().all()
                .put("/api/v1/orders/cancel");

        return response2;
    }

}
