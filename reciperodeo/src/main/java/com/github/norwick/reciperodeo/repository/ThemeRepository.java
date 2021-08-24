package com.github.norwick.reciperodeo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.norwick.reciperodeo.domain.Theme;

/**
 * Spring repository for interacting with JPA representation of CSS theme choice
 */
public interface ThemeRepository extends CrudRepository<Theme, Integer> {
	/**
	 * Searches for theme by name and returns it
	 * @param name name of the theme
	 * @return optional that contains the theme if found
	 */
	Optional<Theme> findByName(String name);
	
	List<Theme> findAll();
}
