package org.example;

import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class ActionWithCourier extends ClientRest {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";
    private static final String COURIER_DELETE_PATH = "/api/v1/courier";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }
    @Step("Логин курьера в системе")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse delete (int courierId) {
        return given()
                .spec(getBaseSpec())
                .delete(COURIER_DELETE_PATH + courierId)
                .then();
    }
}