package logincourier;

import api.CourierApi;
import api.Sprint_7_utils;
import createcourier.BestTest;
import dto.Courier;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class LoginCourierTest extends BestTest {

    Integer idLogin;

    @Test
    public void loginCourier() {
        Courier courier = new Courier(
                Sprint_7_utils.generateRandomString(),
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode(SC_CREATED);

        Response response2 = CourierApi.loginCourier(courier);
        response2.then().log().all()
                .statusCode(SC_OK);

        JSONObject responseBody = new JSONObject(response2.getBody().asString());
        idLogin = responseBody.getInt("id");

    }

    @Test
    public void loginCourierWithoutLogin(){
        Courier courier = new Courier(
                Sprint_7_utils.generateRandomString(),
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode(SC_CREATED);


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
                Sprint_7_utils.generateRandomString(),
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode(SC_CREATED);

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
        String login = Sprint_7_utils.generateRandomString();
        Courier courier = new Courier(
                login,
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode(SC_CREATED);

        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body("{    \"login\": \"" + login +"\",\n" +
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
                Sprint_7_utils.generateRandomString(),
                "4321",
                "kapusta"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode(SC_CREATED);

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
    @After
    public void CleanUp(){
        if (idLogin == null)
            return;
        CourierApi.deleteCourier(idLogin);
    }
}
