import org.example.Order;
import org.junit.Before;
import org.example.OrderAction;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private OrderAction orderStep;
    private final String[] color;
    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name= "color = {0}")
    public static Object[][] colorData() {
        Object[][] objects;
        objects = new Object[][]{
                {new String[]{"GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY", "BLACK"}},
                {null},
        };
        return objects;
    }

    @Before
    public void setUp(){
        orderStep = new OrderAction();
    }

    @DisplayName("Создание заказа")
    @Description("Проверяется создание заказа на: серый цвет, черный цвет, два цвета, без выбора цвета")
    @Test
    public void checkCreateOrderTest() {
        Order order = new Order("Антон", "Кузнецов", "Москварекская, 123", "321", "79777777777", 3, "2023-07-28", "Вперёд!", color);
        ValidatableResponse response = orderStep.createOrder(order);
        response
                .assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
    }
}