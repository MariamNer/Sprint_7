package createcourier;

import api.CourierApi;
import api.LoginCourierApi;
import dto.Courier;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.oneOf;
import static org.apache.http.HttpStatus.*;

public class CreateCourierTest extends BestTest{

    Integer idLogin;

    @Test
    public void createNewCourier(){
        Courier courier = new Courier(
                "morkovka",
                "1234",
                "morkovka"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED, SC_CONFLICT)));

        Response response2 = LoginCourierApi.loginCourier(courier);
        response2.then().log().all()
                .statusCode(SC_OK);
        JSONObject responseBody = new JSONObject(response2.getBody().asString());
        idLogin = responseBody.getInt("id");
    }

    @Test
    public void createTwoIdenticalCourier() {
        Courier courier = new Courier(
                "svekla",
                "12345",
                "svekla"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED, SC_CONFLICT)));

        Response response2 = CourierApi.createCourier(courier);
        response2.then().log().all()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .statusCode(SC_CONFLICT);
    }

    @Test
    public void createNewCourierWithoutLogin(){
        Courier courier = new Courier(
                "",
                "123456",
                "kartoshka"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void createNewCourierWithoutPassword(){
        Courier courier = new Courier(
                "kartoshka",
                "",
                "kartoshka"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"))
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    public void createNewCourierWithoutFirstName(){
        Courier courier = new Courier(
                "kartoshka",
                "123456",
                ""

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode((oneOf(SC_CREATED,SC_CONFLICT)));

        Response response2 = LoginCourierApi.loginCourier(courier);
        response2.then().log().all()
                .statusCode(SC_OK);

        JSONObject responseBody = new JSONObject(response2.getBody().asString());
        idLogin = responseBody.getInt("id");
    }

    @After
    public void CleanUp(){
        if (idLogin == null)
            return;
        given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .when().log().all()
                .delete("/api/v1/courier/" + idLogin)
                .then().log().all()
                .statusCode(SC_OK);
    }

}
