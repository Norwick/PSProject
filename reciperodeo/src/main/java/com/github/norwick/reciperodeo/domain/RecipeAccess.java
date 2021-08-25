package com.github.norwick.reciperodeo.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity representing level of access user has to recipe
 * @author Norwick Lee
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeAccess {
	
	//hibernate really hates composite keys x(
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "BINARY(16)")
	@ColumnTransformer(write="uuid_to_bin(?)")
	private UUID id;
	
	@ManyToOne
	User user;
	
	@ManyToOne
	Recipe recipe;
	
	/**
	 * Relationship of user to recipe
	 */
	public enum Access {
		/**
		 * User owns the recipe
		 */
		OWNER,
		/**
		 * User can edit the recipe
		 */
		EDITOR,
		/**
		 * User can view the recipe
		 */
		VIEWER
	}
	
	private Access relationship;
	
	//ignores internal id
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof RecipeAccess)) return false;
		RecipeAccess ra = (RecipeAccess) obj;
		return this.id.equals(ra.id);
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
