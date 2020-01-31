package com.rezdy.recipe.controller;

import com.rezdy.recipe.model.Recipe;
import com.rezdy.recipe.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Recipe API to return recipes with available ingredients
 */
@RestController
public class RecipeAPIController {

	private static final Logger LOG = LoggerFactory.getLogger(RecipeAPIController.class);

	private RecipeService recipeService;

	@Autowired
	public RecipeAPIController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@RequestMapping("/")
	public String index() {
		return "Welcome to lunch recipe app!";
	}

	@RequestMapping("/lunch")
	public @ResponseBody List<Recipe> listLunchRecipes(){
		return recipeService.getRecipes();
	}

}
