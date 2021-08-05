package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Tag;
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
		this.recipeRepository.delete(r);
	}
	
	/**
	 * Returns count of all recipes in database
	 * @return number of recipes in database
	 */
	public long count() {
		return this.recipeRepository.count();
	}
}
