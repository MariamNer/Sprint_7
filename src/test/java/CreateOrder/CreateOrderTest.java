package CreateOrder;

import dto.Order;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String firstColour;
    private final String secondColour;

    public CreateOrderTest(String firstColour, String secondColourr) {
        this.firstColour = firstColour;
        this.secondColour = secondColourr;
    }

    @Parameterized.Parameters
    public static Object[][] getColor(){
        return new Object[][]{
                {"BLACK", ""},
                {"GREY", ""},
                {"BLACK", "GREY"},
                {"",""},
        };
    }

    @Before
    public void setUP(){
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createNewOrder() {
        Order order = new Order(
                "Ivan",
                "Ivan",
                "BOLSHAYA, 142",
                "zvezdnaya",
                "+7 900 900 90 90",
                2,
                "2024-11-01",
                "Ostavte u dveri",
                new String[]{ new String(firstColour), new String(secondColour)
                }
        );
        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(order)
                .when().log().all()
                .post("/api/v1/orders");
        response.then().log().all()
//                .assertThat().body("ok",equalTo("true"))
                .statusCode(201);

        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        Response response2 = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .get("/v1/orders/track?t" + jsonObject.getInt("track"));
        response2.then().log().all()
                .statusCode(200);
        given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .and()
                .body(jsonObject)
                .when().log().all()
                .put("/api/v1/orders/cancel");
        response2
                .then().log().all()
                .statusCode(200);
    }
}