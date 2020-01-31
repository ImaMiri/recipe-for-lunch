package com.rezdy.recipes;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import com.rezdy.recipe.controller.RecipeAPIController;
import com.rezdy.recipe.model.Recipe;
import com.rezdy.recipe.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeAPIControllerIT {

	@LocalServerPort
	private int port;
	private URL base;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private RecipeService recipeService;
    private RecipeAPIController recipeAPIController;

    @BeforeEach
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.recipeAPIController = new RecipeAPIController(recipeService);
    }

    @Test
    public void index() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody().equals("Welcome to lunch recipe app!"));
    }

    @Test
    public void listLunchRecipes() throws Exception {
        List<Recipe> recipes = recipeAPIController.listLunchRecipes();
        assertEquals(2, recipes.size());

        // If the endpoints changed, the following tests need to be revisited
        assertEquals("Ham and Cheese Toastie", recipes.get(0).getTitle());
        assertEquals(4, recipes.get(0).getIngredients().length);
        assertEquals("Hotdog", recipes.get(1).getTitle());
        assertEquals(4, recipes.get(1).getIngredients().length);
    }

}
