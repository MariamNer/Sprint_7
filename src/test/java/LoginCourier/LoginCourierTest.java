package LoginCourier;

import dto.Courier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.oneOf;

public class LoginCourierTest {
    @Before
    public void setUP() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void loginCourier() {
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .statusCode((oneOf(201, 409)));

        JSONObject jsonObject = new JSONObject(courier);
        jsonObject.remove("firstName");
        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(jsonObject.toString())
                .when().log().all()
                .post("/api/v1/courier/login");
        response2.then().log().all()
                .statusCode(200);

        JSONObject responseBody = new JSONObject(response2.getBody().asString());
        given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(responseBody)
                .when().log().all()
                .delete("/api/v1/courier/" + responseBody.getInt("id"))
                .then().log().all()
                .statusCode(200);

    }

    @Test
    public void loginCourierWithoutLogin(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
//                .assertThat().body("ok",equalTo("true"))
                .statusCode((oneOf(201, 409)));

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
                .statusCode(400);

    }

    @Test
    public void loginCourierWithoutPassword(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .statusCode((oneOf(201, 409)));

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
                .statusCode(504);

    }

    @Test
    public void loginCourierIncorrectPassword(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .statusCode((oneOf(201, 409)));

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
                .statusCode(404);

    }

    @Test
    public void loginCourierIncorrectLogin(){
        Courier courier = new Courier(
                "kapusta",
                "4321",
                "kapusta"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .statusCode((oneOf(201, 409)));

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
                .statusCode(404);

    }


}
