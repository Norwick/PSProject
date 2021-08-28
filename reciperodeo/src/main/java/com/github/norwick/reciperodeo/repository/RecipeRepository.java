package com.github.norwick.reciperodeo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.Recipe.Visibility;
import com.github.norwick.reciperodeo.domain.Tag;

/**
 * Repository for Recipe implemented by Spring
 */
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, UUID> {

	/**
	 * Finds Recipes with a substring that matches title
	 * @param title substring to match in title
	 * @return all recipes that match title substring
	 */
	List<Recipe> findByTitleContaining(String title);
	
	/**
	 * Paginated search for recipe by title (ordered by date created)
	 * @param state public or private currently
	 * @param title substring to match to title
	 * @param p page wanted
	 * @return all recipes matching state and title on selected page
	 */
	Page<Recipe> findByStateAndTitleContainingOrderByCreationTimestampDesc(Visibility state, String title, Pageable p);

	List<Recipe> findAll();
	
	/**
	 * Used to get a list of the most recent recipes with provided state
	 * @param state visibility level
	 * @param p the page number and size requested (e.g. zero'th page, 28 results)
	 * @return all recipes matching state and given page info
	 */
	Page<Recipe> findByStateOrderByCreationTimestampDesc(Visibility state, Pageable p);
	
	/**
	 * Gets all recipes matching visibility and tag (ordered by dated created)
	 * @param state visibility of recipe
	 * @param tag tag recipe contains
	 * @return recipes matching state and tag criteria
	 */
	List<Recipe> findByStateAndTagsContainingOrderByCreationTimestampDesc(Visibility state, Tag tag);
	
}
