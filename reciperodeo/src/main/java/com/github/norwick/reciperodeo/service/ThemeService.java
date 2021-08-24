package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.norwick.reciperodeo.domain.Theme;
import com.github.norwick.reciperodeo.repository.ThemeRepository;

/**
 * Service for interacting with CSS Themes
 * @author Norwick Lee
 */
@Service
public class ThemeService {
	@Autowired
	private ThemeRepository themeRepository;
	
	/**
	 * Returns optional of Theme that shares the provided theme name
	 * @param name name of theme
	 * @return optional of theme that has the provided name
	 */
	public Optional<Theme> findByName(String name) {
		if (name == null) throw new NullPointerException("Name is null");
		return themeRepository.findByName(name);
	}
	
	/**
	 * Finds theme by id and returns it
	 * @param id id of theme
	 * @return optional of theme found
	 */
	public Optional<Theme> findById(int id) {
		return themeRepository.findById(id);
	}
	
	/**
	 * Returns all themes
	 * @return all themes in database
	 */
	public List<Theme> findAll() {
		return themeRepository.findAll();
	}
	
	/**
	 * Adds theme to database
	 * @param t theme to be added to database
	 * @return database valid version of theme
	 */
	public Theme saveTheme(Theme t) {
		if (t == null) throw new NullPointerException("Theme is null");
		return themeRepository.save(t);
	}
	
	/**
	 * Removes theme from database
	 * @param t theme to be removed from database
	 */
	public void removeTheme(Theme t) {
		if (t == null) throw new NullPointerException("Theme is null");
		themeRepository.delete(t);
	}
}
