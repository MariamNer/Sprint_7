package createcourier;

import api.CourierApi;
import dto.Courier;
import api.Sprint_7_utils;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class CreateCourierTest extends BestTest{

    Integer idLogin;

    @Test
    public void createNewCourier(){
        Courier courier = new Courier(
                Sprint_7_utils.generateRandomString(),
                "1234",
                "morkovka"

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
    public void createTwoIdenticalCourier() {
        Courier courier = new Courier(
                Sprint_7_utils.generateRandomString(),
                "12345",
                "svekla"

        );
        Response response = CourierApi.createCourier(courier);
        response.then().log().all()
                .statusCode(SC_CREATED);

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
                Sprint_7_utils.generateRandomString(),
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
                Sprint_7_utils.generateRandomString(),
                "123456",
                ""

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

    @After
    public void CleanUp(){
        if (idLogin == null)
            return;
        CourierApi.deleteCourier(idLogin);
    }

}
