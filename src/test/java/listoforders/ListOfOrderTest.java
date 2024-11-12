package listoforders;

import createcourier.BestTest;
import dto.Order;
import dto.Orders;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotEquals;
import static org.apache.http.HttpStatus.*;


public class ListOfOrderTest extends BestTest {

    @Test
    public void getListOfOrders(){
        Order order = new Order(
                "Ivan",
                "Ivan",
                "BOLSHAYA, 142",
                "zvezdnaya",
                "+7 900 900 90 90",
                2,
                "2024-11-01",
                "Ostavte u dveri",
                new String[]{ new String("BLACK")
                }
        );

        Response response = given()
                .header("Content-type", "application/json")
                .header("api_key", "special-key")
                .get("/api/v1/orders");

        Orders orders = response
                .then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .extract().body().as(Orders.class);
        Integer amountOfOrders = orders.getOrders().length;
        assertNotEquals(amountOfOrders, Integer.valueOf(0));
    }
}
