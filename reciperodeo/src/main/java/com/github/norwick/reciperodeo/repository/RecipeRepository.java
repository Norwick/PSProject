package com.github.norwick.reciperodeo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Recipe.Visibility;

/**
 * Repository for Recipe implemented by Spring
 */
public interface RecipeRepository extends CrudRepository<Recipe, UUID> {

	/**
	 * Finds Recipes with a substring that matches title
	 * @param title substring to match in title
	 * @return all recipes that match title substring
	 */
	List<Recipe> findByTitleContaining(String title);

	List<Recipe> findAll();
	
	/**
	 * Used to get a list of the 28 most recent public recipes, but technically you could also get the 28 most recent private recipes
	 * @param state visibility level
	 * @return list of 28 most recent recipes of provided state
	 */
	List<Recipe> findTop28ByStateOrderByCreationTimestampDesc(Visibility state);
}
