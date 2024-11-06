package CreateCourier;

import dto.Courier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.oneOf;

public class CreateCourierTest {
    @Before
    public void setUP(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createNewCourier(){
        Courier courier = new Courier(
                "morkovka",
                "1234",
                "morkovka"

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
        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(jsonObject.toString())
                .when().log().all()
                .post("/api/v1/courier/login");
        response2.then().log().all()
                .statusCode(200);
        System.out.println("DEBUG: toString = " + response2.getBody().toString());
        System.out.println("DEBUG: asString = " + response2.getBody().asString());
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
    public void createTwoIdenticalCourier() {
        Courier courier = new Courier(
                "svekla",
                "12345",
                "svekla"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .statusCode((oneOf(201, 409)));

        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response2.then().log().all()
                .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .statusCode(409);
    }

    @Test
    public void createNewCourierWithoutLogin(){
        Courier courier = new Courier(
                "",
                "123456",
                "kartoshka"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message",equalTo("Недостаточно данных для создания учетной записи"))
        ;
    }

    @Test
    public void createNewCourierWithoutPassword(){
        Courier courier = new Courier(
                "kartoshka",
                "",
                "kartoshka"

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"))
                .statusCode(400);
    }

    @Test
    public void createNewCourierWithoutFirstName(){
        Courier courier = new Courier(
                "kartoshka",
                "123456",
                ""

        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key","special-key")
                .and()
                .body(courier)
                .when().log().all()
                .post("/api/v1/courier");
        response.then().log().all()
//                .assertThat().body("message",equalTo("Недостаточно данных для создания учетной записи"))
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

}
