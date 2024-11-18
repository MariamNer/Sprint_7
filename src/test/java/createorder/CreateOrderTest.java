package createorder;

import api.OrderApi;
import createcourier.BestTest;
import dto.Order;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateOrderTest extends BestTest {
    private final String firstColour;
    private final String secondColour;
    Integer track;
    JSONObject jsonObject;

    public CreateOrderTest(String firstColour, String secondColour) {
        this.firstColour = firstColour;
        this.secondColour = secondColour;
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
        Response response = OrderApi.createOrder(order);
        response.then().log().all()
                .statusCode(SC_CREATED);

        jsonObject = new JSONObject(response.getBody().asString());
        track = jsonObject.getInt("track");

    }

    @After
    public void tearDown() {
        Response response2 = OrderApi.cancelOrder(track);
        response2
                .then().log().all()
                .statusCode(SC_OK);
    }
}