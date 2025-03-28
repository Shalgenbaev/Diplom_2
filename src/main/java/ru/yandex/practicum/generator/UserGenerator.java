package ru.yandex.practicum.generator;

import io.qameta.allure.Step;  // Правильный импорт для Allure
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practicum.model.user.User;
import ru.yandex.practicum.model.user.UserWithoutEmail;
import ru.yandex.practicum.model.user.UserWithoutName;
import ru.yandex.practicum.model.user.UserWithoutPassword;

public class UserGenerator {

    @Step("Создание пользователя с полными данными")
    public User createUser() {
        return new User(
                createUniqueEmail(),
                createPassword(),
                createName());
    }

    @Step("Создание пользователя без имени")
    public UserWithoutName createUserWithoutName() {
        return new UserWithoutName(
                createUniqueEmail(),
                createPassword());
    }

    @Step("Создание пользователя с именем null")
    public User createUserWithNameNull() {
        return new User(
                createUniqueEmail(),
                createPassword(),
                null);
    }

    @Step("Создание пользователя без пароля")
    public UserWithoutPassword createUserWithoutPassword() {
        return new UserWithoutPassword(
                createUniqueEmail(),
                createName());
    }

    @Step("Создание пользователя с паролем null")
    public User createUserWithPasswordNull() {
        return new User(
                createUniqueEmail(),
                null,
                createName());
    }

    @Step("Создание пользователя без электронной почты")
    public UserWithoutEmail createUserWithoutEmail() {
        return new UserWithoutEmail(
                createName(),
                createPassword());
    }

    @Step("Создание пользователя с электронной почтой null")
    public User createUserWithEmailNull() {
        return new User(
                null,
                createPassword(),
                createName());
    }

    @Step("Генерация уникального электронного адреса")
    private String createUniqueEmail() {
        return String.format("%s@%s.ru", RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(3));
    }

    @Step("Генерация пароля")
    private String createPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    @Step("Генерация имени")
    private String createName() {
        return RandomStringUtils.randomAlphabetic(6);
    }
}
