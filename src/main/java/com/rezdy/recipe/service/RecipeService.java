package com.rezdy.recipe.service;

import com.google.gson.Gson;
import com.rezdy.recipe.model.Ingredient;
import com.rezdy.recipe.model.IngredientList;
import com.rezdy.recipe.model.Recipe;
import com.rezdy.recipe.model.RecipeList;
import com.rezdy.recipe.utils.HttpClientProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  A service that uses HttpClient to connect to third party API to receive recipes and ingredients data
 */
@Service
public class RecipeService {
    private static final Logger LOG = LoggerFactory.getLogger(RecipeService.class);
    private static final String RECIPE_ENDPOINT = "https://www.mocky.io/v2/5c85f7a1340000e50f89bd6c";
    private static final String INGREDIENTS_ENDPOINT = "https://www.mocky.io/v2/5e3126c832000055008882ce";
    private HttpClientProvider httpClientProvider;

    @Autowired
    public RecipeService(HttpClientProvider httpClientProvider) {
        this.httpClientProvider = httpClientProvider;
    }

    public List<Recipe> getRecipes(){

        List<Recipe> lunchRecipes = new ArrayList<>();
        try {
            String recipesJson = httpClientProvider.sendGET(RECIPE_ENDPOINT);
            String ingredientsJson = httpClientProvider.sendGET(INGREDIENTS_ENDPOINT);

            Gson gson = new Gson();
            RecipeList recipeList = gson.fromJson(recipesJson, RecipeList.class);
            IngredientList ingredientList = gson.fromJson(ingredientsJson, IngredientList.class);

            if(recipeList != null && ingredientList != null){

                List<Recipe> recipes = recipeList.getRecipes();
                List<Ingredient> ingredients = ingredientList.getIngredients();

                recipes.forEach(recipe -> {
                    AtomicBoolean addVal = new AtomicBoolean(true);
                    Arrays.stream(recipe.getIngredients()).forEach(ingredient -> {
                        ingredients.stream().filter(ing -> ing.getTitle().equals(ingredient)).filter(ing ->
                                compareDates(ing.getUseBy()) < 0).map(
                                ing -> false).forEachOrdered(addVal::set);
                    });
                    if (addVal.get()) {
                        lunchRecipes.add(recipe);
                    }
                });

                Collections.sort(lunchRecipes, (o1, o2) -> {
                    String[] ings1 = o1.getIngredients();
                    String[] ings2 = o2.getIngredients();

                    if(compareIngredients(ings1, ingredients) < compareIngredients(ings2, ingredients)){
                        return -1;
                    }else if (compareIngredients(ings1, ingredients) == compareIngredients(ings2, ingredients)){
                        return 0;
                    }else{
                        return 1;
                    }
                });
            }

        } catch (IOException | InterruptedException e) {
            LOG.error("An error occurred during reading JSON, ", e);
        }
        return lunchRecipes;
    }

    private int compareIngredients(String[] ings, List<Ingredient> ingredients) {
        AtomicInteger flag = new AtomicInteger();
        Arrays.stream(ings).forEachOrdered(i -> {
            ingredients.forEach(ing -> {
                if (i.equals(ing.getTitle()) && compareDates(ing.getBestBefore()) < 0) {
                    flag.set(-1);
                } else if (i.equals(ing.getTitle()) && compareDates(ing.getBestBefore()) > 0) {
                    flag.set(1);
                }
            });
        });
        return flag.get();
    }

    private int compareDates(String input) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(input);
            return date.compareTo(new Date());
        } catch (ParseException e) {
            LOG.error("There is an error while comparing date,", e);
            return -2;
        }
    }
}
