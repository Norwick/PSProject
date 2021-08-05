package com.github.norwick.reciperodeo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

/**
 * JPA class for Tag with int id, string representing actual tag name, and list of recipes applied to tag
 * @author Norwick Lee
 */
@Entity
public class Tag {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NotNull
	@Column(unique=true)
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable
	private List<Recipe> recipes = new ArrayList<>();
	
	/**
	 * Creates tag with auto-generated id, unitialized name, and empty list of recipes
	 */
	public Tag() {
		//empty constructor for spring
	}
	
	/**
	 * Creates tag with auto-generated id and provided name, and empty list of recipes
	 * @param name tag name
	 */
	public Tag(String name) {
		this.setName(name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Tag)) return false;
		Tag t = (Tag) obj;
		return this.id == t.id;
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
	
	/**
	 * Returns whether the tag has the provided tag name
	 * @param name provided tag name
	 * @return if tag name is same as provided name
	 */
	public boolean sameName(String name) {
		return this.name.equals(name);
	}

	/**
	 * @return id of tag
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Changes id to provided id
	 * @param id new tag id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return name of tag
	 */
	public String getName() {
		return name;
	}

	/**
	 * Changes name to provided name
	 * @param name new name of tag
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return recipes that tag applies to
	 */
	public List<Recipe> getRecipes() {
		return this.recipes;
	}
	
	/**
	 * Changes recipes in tag to provided recipes
	 * @param recipes provided recipes
	 */
	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	/**
	 * Adds provided recipe to recipes tag applies to
	 * @param r provided recipe
	 */
	public void addRecipe(Recipe r) {
		this.recipes.add(r);
	}
	
	/**
	 * Removes provided recipe from recipes tag applies to
	 * @param r provided recipe
	 */
	public void removeRecipe(Recipe r) {
		this.recipes.remove(r);
	}
}
