package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Recipe.Visibility;
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
		if (title == null) throw new NullPointerException("Title is null");
		return this.recipeRepository.findByTitleContaining(title);
	}
	
	/**
	 * Searches for 28 recipes with provided state and orders by newest first.
	 * This oddly specific number is solely because it looks good on my resolution. I apologize.
	 * @param state state to search for
	 * @param pageNum the page number
	 * @param pageSize the number of results per page
	 * @return list of 28 recipes with provided state ordered by newest first
	 */
	public List<Recipe> findByStateOrderByCreationTimestamp(Visibility state, int pageNum, int pageSize) {
		if (state == null) throw new NullPointerException("State is null");
		Pageable p = PageRequest.of(pageNum, pageSize);
		return this.recipeRepository.findByStateOrderByCreationTimestampDesc(state, p).getContent();
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
		Set<Tag> tags = r.getTags();
		for(Tag t : tags) {
			t.removeRecipe(r);
			tagService.saveTag(t);
		}
		r.setUser(null);
		r = recipeRepository.save(r);
		this.recipeRepository.delete(r);
	}
	
	/**
	 * Returns count of all recipes in database
	 * @return number of recipes in database
	 */
	public long count() {
		return this.recipeRepository.count();
	}

	/**
	 * Returns a list of all recipes in database
	 * @return list of recipes
	 */
	public List<Recipe> findAll() {
		return recipeRepository.findAll();
	}
	
	/**
	 * Searches for public recipes by title
	 * @param title substring of title
	 * @param pageNum page number looking up
	 * @param pageSize size of page
	 * @return list of matching recipes
	 */
	public Page<Recipe> searchPublicRecipes(String title, int pageNum, int pageSize) {
		Pageable p = PageRequest.of(pageNum, pageSize);
		return recipeRepository.findByStateAndTitleContainingOrderByCreationTimestampDesc(Recipe.Visibility.PUBLIC, title, p);
	}
	
	/**
	 * Searches for public recipes by list of tags
	 * @param tags string list of tags
	 * @param pageNum number of pages
	 * @param pageSize size of pages
	 * @return list of recipes that contain all valid tags
	 */
	public Page<Recipe> searchByTag(List<Tag> tags, int pageNum, int pageSize) {
		Pageable p = PageRequest.of(pageNum, pageSize);
		List<Recipe> lr = recipeRepository.findByStateAndTagsContainingOrderByCreationTimestampDesc(Recipe.Visibility.PUBLIC, tags.get(0));
		if (tags.size() > 1) {
			lr = lr.stream().filter(r -> {
				Set<Tag> ts = r.getTags();
				return ts.containsAll(tags);
			}).collect(Collectors.toList());
		}
		
		return new PageImpl<>(lr, p, lr.size());
	}
}
