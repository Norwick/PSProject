package com.github.norwick.reciperodeo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA object to represent a user theme e.g. dark mode vs light mode.
 * Contains id and name of theme.
 * @author Norwick Lee
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Theme {

	@Id
	private int id;

	@NotNull
	@Column(unique=true)
	private String name;
	
	/**
	 * Returns whether the theme has the provided theme name
	 * @param name provided theme name
	 * @return if theme name is same as provided name
	 */
	public boolean sameName(String name) {
		return this.name.equals(name);
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Tag)) return false;
		Theme t = (Theme) obj;
		return this.id == t.id;
	}
}
