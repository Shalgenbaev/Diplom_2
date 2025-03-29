import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Test;
import ru.yandex.practicum.client.order.OrderClient;
import ru.yandex.practicum.generator.IngredientsGenerator;
import ru.yandex.practicum.model.ingredient.Ingredient;
import ru.yandex.practicum.model.order.OrderAfterCreate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Slf4j
public class CreateOrderWithLoginTest extends BaseTest {
    public static final String SUCCESS = "success";
    public static final String ORDER = "order";
    public static final String MESSAGE = "message";
    public static final String TEXT_MESSAGE_INGREDIENT_IDS_MUST_BE_PROVIDED = "Ingredient ids must be provided";
    public static final String INGREDIENTS = "ingredients";

    private final OrderClient orderClient = new OrderClient();
    private final IngredientsGenerator ingredientsGenerator = new IngredientsGenerator();
    private OrderAfterCreate orderAfterCreate;

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createCorrectOrderWithLogin() {
        // Получаем корректные ингредиенты
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getCorrectIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);

        // Создаем заказ с логином
        Response response = orderClient.createOrderWithLogin(ingredientsMap, accessToken);
        log.info("Получен ответ от сервера: {}", response.body().asString());

        // Проверяем код ответа
        response.then()
                .statusCode(HttpStatus.SC_OK)
                .and().body(SUCCESS, equalTo(true));

        // Безопасное получение объекта заказа
        orderAfterCreate = response.body().jsonPath().getObject(ORDER, OrderAfterCreate.class);

        // Проверка, что объект заказа не null
        assertNotNull("Созданный заказ не должен быть null", orderAfterCreate);

        // Проверка ингредиентов
        assertNotNull("Список ингредиентов не должен быть null", orderAfterCreate.getIngredients());

        List<String> expected = new ArrayList<>(Arrays.asList(ingredientsMap.get(INGREDIENTS)));
        List<String> actual = getActual(orderAfterCreate);

        assertEquals("Списки ингредиентов должны совпадать", expected, actual);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, без ингредиентов")
    public void createOrderWithoutIngredients() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getEmptyIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);

        Response response = orderClient.createOrderWithLogin(ingredientsMap, accessToken);
        log.info("Получен ответ от сервера: {}", response.body().asString());

        response.then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .and().body(SUCCESS, equalTo(false))
                .and().body(MESSAGE, equalTo(TEXT_MESSAGE_INGREDIENT_IDS_MUST_BE_PROVIDED));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, с некорректным хэшем ингредиентов")
    public void createOrderWithIncorrectIngredients() {
        Map<String, String[]> ingredientsMap = ingredientsGenerator.getIncorrectIngredients();
        log.info("Список ингредиентов: {}", ingredientsMap);

        Response response = orderClient.createOrderWithLogin(ingredientsMap, accessToken);
        log.info("Получен ответ от сервера: {}", response.body().asString());

        response.then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    private List<String> getActual(OrderAfterCreate orderAfterCreate) {
        List<String> actual = new ArrayList<>();
        for (Ingredient ingredient : orderAfterCreate.getIngredients()) {
            actual.add(ingredient.get_id());
        }
        return actual;
    }
}
