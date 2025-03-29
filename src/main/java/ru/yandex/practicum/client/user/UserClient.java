package ru.yandex.practicum.client.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import ru.yandex.practicum.client.Client;
import ru.yandex.practicum.model.user.User;
import ru.yandex.practicum.model.user.UserWithoutEmail;
import ru.yandex.practicum.model.user.UserWithoutName;
import ru.yandex.practicum.model.user.UserWithoutPassword;
import java.util.Map;

public class UserClient extends Client {
    private static final String ROOT = "/auth";
    private static final String REGISTER = "/register";
    private static final String USER = "/user";
    private static final String LOGIN = "/login";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    @Step("Регистрация пользователя")
    public Response registerUser(Object userData) {
        return specification()
                .body(userData)
                .when()
                .post(ROOT + REGISTER);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(Object userData) {
        return specification()
                .body(userData)
                .post(ROOT + LOGIN);
    }

    // Остальные методы остаются без изменений
    // ...

    @Step("Получение информации о пользователе")
    public Response getUserInfo(String accessToken) {
        return specification()
                .header(HEADER_AUTHORIZATION, accessToken)
                .get(ROOT + USER);
    }

    @Step("Изменение пользователя c авторизацией")
    public Response updateUser(Map<String, String> updateData, String accessToken) {
        return specification()
                .header(HEADER_AUTHORIZATION, accessToken)
                .body(updateData)
                .patch(ROOT + USER);
    }

    @Step("Изменение пользователя без авторизации")
    public Response updateUserWithoutLogin(Map<String, String> updateData) {
        return specification()
                .body(updateData)
                .patch(ROOT + USER);
    }

    @Step("Удаление пользователя")
    public Response deleteUser(User user, String accessToken) {
        return specification()
                .header(HEADER_AUTHORIZATION, accessToken)
                .body(user)
                .delete(ROOT + USER);
    }
}
