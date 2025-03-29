package ru.yandex.practicum.generator;
import ru.yandex.practicum.model.ingredient.Ingredient;
import ru.yandex.practicum.model.ingredient.IngredientData;
import java.util.*;

public class IngredientsGenerator {
    private Random random = new Random();
    private Map<String, String[]> ingredientsMap = new HashMap<>();
    List<Ingredient> ingredientsData = new IngredientData().getIngredients();

    public Map<String, String[]> getCorrectIngredients() {
        String[] ingredients = new String[random.nextInt(ingredientsData.size() + 1)];
        for (int i = 0; i < ingredients.length; i++) {
            ingredients[i] = ingredientsData.get(random.nextInt(ingredientsData.size())).get_id();
        }
        ingredientsMap.put("ingredients", ingredients);
        return ingredientsMap;
    }

    public Map<String, String[]> getEmptyIngredients() {
        return ingredientsMap;
    }

    public Map<String, String[]> getIncorrectIngredients() {
        ingredientsMap.put("ingredients", new String[] {"incorrect hash"});
        return ingredientsMap;
    }
}