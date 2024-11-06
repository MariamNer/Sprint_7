package Api;

import dto.Courier;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;

public class CourierApi {

    public static void createCourier(Courier courier){
        JSONObject jsonObject = new JSONObject(courier);
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(jsonObject.toString())
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .assertThat()
                .statusCode(201);
    }

}
