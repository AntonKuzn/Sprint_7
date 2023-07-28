import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.example.Courier;
import org.example.ActionWithCourier;
import org.example.CourierCredentials;
import org.example.CourierGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class CourierLoginTest {

    private ActionWithCourier courierStep;
    private Courier courier;
    private Integer courierId;
    private Courier notExistCourier = CourierGenerator.getRandom();

    @Before
    public void setUp() {
        courierStep = new ActionWithCourier();
        courier = CourierGenerator.getRandom();
        courierStep.create(courier);
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierStep.delete(courierId);
        }
    }

    @Test
    @DisplayName("Успешная авторизация")
    @Description("Проверяется авторизации с валидными данными")
    public void successLoginCourierTest(){
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        loginResponse
                .statusCode(200)
                .assertThat()
                .body("id",is(notNullValue()));
    }

    @Test
    @DisplayName("Авторизации без логина")
    @Description("Проверяется невозможность авторизации курьера без логина")
    public void loginCourierWithoutLoginTest(){
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setLogin("")));
        loginResponse
                .statusCode(400)
                .assertThat()
                .body("code",equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизации без пароля")
    @Description("Проверяется невозможность авторизации курьера без пароля")
    public void loginCourierWithoutPasswordTest(){
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setPassword("")));
        loginResponse
                .statusCode(400)
                .assertThat()
                .body("code",equalTo(400))
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным логином")
    @Description("Проверяется невозможность авторизации курьера c некорректным логином")
    public void loginWithIncorrectLoginTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setLogin("UNKNOWN_LOGIN")));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code",equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера c некорректным паролем")
    @Description("Проверяется невозможность авторизации курьера c некорректным паролем")
    public void loginWithIncorrectPasswordTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(courier.setPassword("UNKNOWN_PASSWORD")));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code",equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация несуществующего курьера")
    @Description("Проверяется невозможность авторизации курьера с несуществующей парой логин+пароль")
    public void loginUnknownCourierTest() {
        courierId = courierStep.login(CourierCredentials.from(courier)).extract().path("id");
        ValidatableResponse loginResponse = courierStep.login(CourierCredentials.from(notExistCourier));
        loginResponse
                .statusCode(404)
                .assertThat()
                .body("code", equalTo(404))
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

}