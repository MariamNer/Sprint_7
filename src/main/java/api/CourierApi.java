package api;

import dto.Courier;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.json.JSONObject;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;


public class CourierApi {

    @Step
    public static Response createCourier(Courier courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        return response;
    }

    @Step
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

    @Step
    public static void deleteCourier(Integer idLogin) {
        given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .when().log().all()
                .delete("/api/v1/courier/" + idLogin)
                .then().log().all()
                .statusCode(SC_OK);
    }
}
