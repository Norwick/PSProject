package com.github.norwick.reciperodeo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.RecipeAccess;
import com.github.norwick.reciperodeo.domain.RecipeAccess.Access;
import com.github.norwick.reciperodeo.domain.User;

/**
 * Spring repository for interacting with recipe access JPA object
 */
public interface RecipeAccessRepository extends CrudRepository<RecipeAccess, UUID> {
	
	//TODO might need to change to find by User_Id and Recipe_Id if bad bugs
	/**
	 * Searches for RecipeAccess that represents the relationship betweeen provided user and recipe
	 * @param u provided user
	 * @param r provided recipe
	 * @return optional containing object representing access of user to recipe or null if not found
	 */
	Optional<RecipeAccess> findByUserAndRecipe(User u, Recipe r);
	
	/**
	 * Searches for RecipeAccess of a recipe with specific access level
	 * @param r provided recipe
	 * @param a provided access level
	 * @return list of all relationships to provided recipe that have provided access
	 */
	List<RecipeAccess> findByRecipeAndRelationship(Recipe r, Access a);
}
