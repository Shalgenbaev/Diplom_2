import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.client.user.UserClient;
import ru.yandex.practicum.generator.UserGenerator;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.CoreMatchers.equalTo;

@Slf4j
@RunWith(Parameterized.class)
public class UpdateUserTest extends BaseTest {
    public static final String OLD = "old";
    public static final String NULL = "null";
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    public static final String MESSAGE_YOU_SHOULD_BE_AUTHORISED = "You should be authorised";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    private final UserGenerator userGenerator = new UserGenerator();
    private final UserClient userClient = new UserClient();
    private final String email;
    private final String password;
    private final String name;
    // для простоты введем переменную для отображения имени теста при прогоне параметризованных тестов
    private final String testName;

    public UpdateUserTest(String email, String password, String name, String testName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.testName = testName;
    }

    @Parameterized.Parameters(name = "{index} : update {3}")
    public static Object[][] getParameters() {
        return new Object[][]{
                {createUniqueEmail(), createPassword(), createName(), "all fields"},
                {createUniqueEmail(), OLD, OLD, EMAIL},
                {OLD, createPassword(), OLD, PASSWORD},
                {OLD, OLD, createName(), NAME},
                {createUniqueEmail(), NULL, NULL, "email without another fields"},
                {NULL, createPassword(), NULL, "password without another fields"},
                {NULL, NULL, createName(), "name without another fields"},
                {createUniqueEmail(), createPassword(), NULL, "email + password without name"},
                {NULL, createPassword(), createName(), "password + name without email"},
                {createUniqueEmail(), NULL, createName(), "email + name without password"}
        };
    }

    @Test
    public void updateLoginUser() {
        Map<String, String> updateData = createMap(email, password, name);
        log.info("Обновляемые данные: {}", updateData);

        Response response = userClient.updateUser(updateData, accessToken);
        log.info("Получен ответ от сервера {}", response.body().asString());

        response.then().statusCode(HttpStatus.SC_OK)
                .and().body(SUCCESS, equalTo(true));

    }

    @Test
    public void updateWithoutLogin() {
        Map<String, String> updateData = createMap(email, password, name);
        log.info("Обновляемые данные: {}", updateData);

        Response response = userClient.updateUserWithoutLogin(updateData);
        log.info("Получен ответ от сервера {}", response.body().asString());

        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(MESSAGE_YOU_SHOULD_BE_AUTHORISED));
    }

    private Map<String, String> createMap(String email, String password, String name) {
        Map<String, String> updateData = new HashMap<>();
        if (email.contains(OLD)) {
            updateData.put(EMAIL, user.getEmail());
        } else if (!email.contains(NULL)) {
            updateData.put(EMAIL, email);
        }
        if (password.contains(OLD)) {
            updateData.put(PASSWORD, user.getPassword());
        } else if (!password.contains(NULL)) {
            updateData.put(PASSWORD, password);
        }
        if (name.contains(OLD)) {
            updateData.put(NAME, user.getName());
        } else if (!name.contains(NULL)) {
            updateData.put(NAME, name);
        }

        return updateData;
    }

    private static String createUniqueEmail() {
        return String.format("%s@%s.ru", RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(3));
    }

    private static String createPassword() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    private static String createName() {
        return RandomStringUtils.randomAlphabetic(6);
    }
}
