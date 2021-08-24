package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.RecipeAccess;
import com.github.norwick.reciperodeo.domain.RecipeAccess.Access;
import com.github.norwick.reciperodeo.domain.Tag;
import com.github.norwick.reciperodeo.domain.User;
import com.github.norwick.reciperodeo.repository.RecipeAccessRepository;
import com.github.norwick.reciperodeo.repository.RecipeRepository;

/**
 * Service used to interact with recipe
 * @author Norwick Lee
 */
@Service
public class RecipeService {
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private RecipeAccessRepository recipeAccessRepository;
	
	@Autowired
	private TagService tagService;
	
	/**
	 * Searches for all recipes containing provided substring in title
	 * @param title the substring to search titles for
	 * @return list of recipes with titles containing the provided substring
	 */
	public List<Recipe> findByTitleContaining(String title) {
		return this.recipeRepository.findByTitleContaining(title);
	}
	
	/**
	 * Finds a recipe by id
	 * @param id the uuid identifying a recipe
	 * @return an optional containing a matching recipe or nothing if no id match
	 */
	public Optional<Recipe> findById(UUID id) {
		return this.recipeRepository.findById(id);
	}
	
	/**
	 * Finds whether a recipe with provided id exists
	 * @param id the uuid identifying a recipe
	 * @return true if recipe with id exists
	 */
	public boolean existsById(UUID id) {
		return this.recipeRepository.existsById(id);
	}
	
	/**
	 * Saves recipe to database. Replaces existing recipe if present.
	 * @param r recipe to be saved
	 * @return the updated reference to the saved recipe
	 */
	public Recipe saveRecipe(Recipe r) {
		if (r == null) throw new NullPointerException("Recipe is Null");
		return this.recipeRepository.save(r);
	}
	
	/**
	 * Deletes recipe from database.
	 * @param r recipe to be deleted
	 */
	public void removeRecipe(Recipe r) {
		if (r == null) throw new NullPointerException("Recipe is Null");
		List<Tag> tags = r.getTags();
		for(Tag t : tags) {
			t.removeRecipe(r);
			tagService.saveTag(t);
		}
		Set<RecipeAccess> ras = r.getRecipeAccesses();
		for (RecipeAccess ra : ras) {
			recipeAccessRepository.delete(ra);
		}
		this.recipeRepository.delete(r);
	}
	
	/**
	 * Sets user access level for recipe
	 * @param r recipe to be affected
	 * @param u user to be set
	 * @param a level of access
	 * @return an object representing the user's access to the recipe that contains the current version of the user and recipe
	 */
	public RecipeAccess setUser(Recipe r, User u, Access a) {
		if (u == null) throw new NullPointerException("User is null");
		if (r == null) throw new NullPointerException("Recipe is null");
		Optional<RecipeAccess> ora = recipeAccessRepository.findByUserAndRecipe(u, r);
		RecipeAccess ra;
		if (ora.isEmpty()) {
			ra = new RecipeAccess();
			ra.setRecipe(r);
			ra.setUser(u);
		} else {
			ra = ora.get();
		}
		ra.setRelationship(a);
		return recipeAccessRepository.save(ra);
	}
	
	/**
	 * Removes a user's access to the recipe
	 * @param r provided recipe
	 * @param u provided user
	 */
	public void removeUser(Recipe r, User u) {
		if (u == null) throw new NullPointerException("User is null");
		if (r == null) throw new NullPointerException("Recipe is null");
		Optional<RecipeAccess> ora = recipeAccessRepository.findByUserAndRecipe(u, r);
		if (ora.isPresent()) {
			recipeAccessRepository.delete(ora.get());
		}
	}
	
	/**
	 * Returns count of all recipes in database
	 * @return number of recipes in database
	 */
	public long count() {
		return this.recipeRepository.count();
	}
}
