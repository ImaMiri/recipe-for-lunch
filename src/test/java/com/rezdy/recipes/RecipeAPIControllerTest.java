package com.rezdy.recipes;

import static org.junit.Assert.assertEquals;

import com.rezdy.recipe.model.Recipe;
import com.rezdy.recipe.service.RecipeService;
import com.rezdy.recipe.utils.HttpClientProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RecipeAPIControllerTest {

	@Mock
	private HttpClientProvider provider;
	@InjectMocks
	private RecipeService recipeService;
	String recipes;
	String ingredients;

	@BeforeEach
	public void setUp() throws IOException {
		recipes = new String(this.getClass().getResourceAsStream("/recipes.json").readAllBytes());
        ingredients = new String(this.getClass().getResourceAsStream("/ingredients.json").readAllBytes());

	}

	@Test
    public void testGetRecipes() throws Exception {
		Mockito.when(provider.sendGET("https://www.mocky.io/v2/5c85f7a1340000e50f89bd6c")).thenReturn(recipes);
		Mockito.when(provider.sendGET("https://www.mocky.io/v2/5e3126c832000055008882ce")).thenReturn(ingredients);

		List<Recipe> recipes = recipeService.getRecipes();

		assertEquals(2, recipes.size());
		assertEquals("Ham and Cheese Toastie", recipes.get(0).getTitle());
		assertEquals(4, recipes.get(0).getIngredients().length);
		assertEquals("Hotdog", recipes.get(1).getTitle());
		assertEquals(4, recipes.get(1).getIngredients().length);
    }

	@Test
	public void testGetRecipesWhenDataIsEmpty() throws Exception {
		recipes = "";
		ingredients = "";
		Mockito.when(provider.sendGET("https://www.mocky.io/v2/5c85f7a1340000e50f89bd6c")).thenReturn(recipes);
		Mockito.when(provider.sendGET("https://www.mocky.io/v2/5e3126c832000055008882ce")).thenReturn(ingredients);

		List<Recipe> recipes = recipeService.getRecipes();

		assertEquals(0, recipes.size());
	}

	@Test
	public void testGetRecipesWhenDataIsNull() throws Exception {
		recipes = null;
		ingredients = null;
		Mockito.when(provider.sendGET("https://www.mocky.io/v2/5c85f7a1340000e50f89bd6c")).thenReturn(recipes);
		Mockito.when(provider.sendGET("https://www.mocky.io/v2/5e3126c832000055008882ce")).thenReturn(ingredients);

		List<Recipe> recipes = recipeService.getRecipes();

		assertEquals(0, recipes.size());
	}
}
