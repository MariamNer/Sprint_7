package logincourier;

import api.CourierApi;
import api.LoginCourierApi;
import createcourier.BestTest;
import dto.Courier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.oneOf;
import static org.apache.http.HttpStatus.*;

public class LoginCourierTest extends BestTest {

    @Test
    public void loginCourier() {
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED, SC_CONFLICT)));

        Response response2 = LoginCourierApi.loginCourier(courier);
        response2.then().log().all()
                .statusCode(SC_OK);

        JSONObject responseBody = new JSONObject(response2.getBody().asString());
        given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(responseBody)
                .when().log().all()
                .delete("/api/v1/courier/" + responseBody.getInt("id"))
                .then().log().all()
                .statusCode(SC_OK);

    }

    @Test
    public void loginCourierWithoutLogin(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED, SC_CONFLICT)));


        JSONObject jsonObject = new JSONObject(courier);
        jsonObject.remove("firstName");
        jsonObject.remove("login");
        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(jsonObject.toString())
                .when().log().all()
                .post("/api/v1/courier/login");
        response2.then().log().all()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .statusCode(SC_BAD_REQUEST);

    }

    @Test
    public void loginCourierWithoutPassword(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED, SC_CONFLICT)));

        JSONObject jsonObject = new JSONObject(courier);
        jsonObject.remove("firstName");
        jsonObject.remove("password");
        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(jsonObject.toString())
                .when().log().all()
                .post("/api/v1/courier/login");
        response2.then().log().all()
                .statusCode(SC_GATEWAY_TIMEOUT);

    }

    @Test
    public void loginCourierIncorrectPassword(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED, SC_CONFLICT)));

        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body("{    \"login\": \"kapusta\",\n" +
                        "    \"password\": \"1234\"}")
                .when().log().all()
                .post("/api/v1/courier/login");
        response2.then().log().all()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .statusCode(SC_NOT_FOUND);

    }

    @Test
    public void loginCourierIncorrectLogin(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED, SC_CONFLICT)));

        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body("{    \"login\": \"kapa\",\n" +
                        "    \"password\": \"4321\"}")
                .when().log().all()
                .post("/api/v1/courier/login");
        response2.then().log().all()
                .assertThat().body("message", equalTo("Учетная запись не найдена"))
                .statusCode(SC_NOT_FOUND);

    }


}
