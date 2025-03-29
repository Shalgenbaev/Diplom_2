import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.practicum.client.user.UserClient;
import ru.yandex.practicum.generator.UserGenerator;
import ru.yandex.practicum.model.user.User;
import ru.yandex.practicum.model.user.UserWithoutEmail;
import ru.yandex.practicum.model.user.UserWithoutName;
import ru.yandex.practicum.model.user.UserWithoutPassword;
import static org.hamcrest.CoreMatchers.equalTo;

@Slf4j
public class CreateInvalidUserTest {
    public static final String SUCCESS = "success";
    public static final String MESSAGE = "message";
    public static final String TEXT_MESSAGE_REQUIRED_FIELDS = "Email, password and name are required fields";
    private final UserClient userClient = new UserClient();
    private final UserGenerator userGenerator = new UserGenerator();

    @Test
    public void registerUserWithoutName() {
        UserWithoutName userWithoutName = userGenerator.createUserWithoutName();

        Response response = userClient.registerUser(userWithoutName);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithNameNull() {
        User user = userGenerator.createUserWithNameNull();

        Response response = userClient.registerUser(user);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithoutPassword() {
        UserWithoutPassword userWithoutPassword = userGenerator.createUserWithoutPassword();

        Response response = userClient.registerUser(userWithoutPassword);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithPasswordNull() {
        User user = userGenerator.createUserWithPasswordNull();

        Response response = userClient.registerUser(user);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithoutEmail() {
        UserWithoutEmail userWithoutEmail = userGenerator.createUserWithoutEmail();

        Response response = userClient.registerUser(userWithoutEmail);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }

    @Test
    public void registerUserWithEmailNull() {
        User user = userGenerator.createUserWithEmailNull();

        Response response = userClient.registerUser(user);

        response.then()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_REQUIRED_FIELDS));
    }
}
