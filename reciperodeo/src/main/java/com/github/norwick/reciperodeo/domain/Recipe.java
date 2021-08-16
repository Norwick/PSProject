package com.github.norwick.reciperodeo.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA class for Recipe which contains id, visibility, timestamps for creation and editing, and a JSON String of the actual recipe content
 * @author Norwick Lee
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe {

	/**
	 * Visibility of recipe
	 */
	public enum Visibility {
	/**
	 * Visible to owner and editors
	 */
	PRIVATE, 
	
	/**
	 * Visible to owner, editors, and friends
	 */
	FRIEND, 
	
	/**
	 * Visible to anyone
	 */
	PUBLIC}
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "BINARY(16)")
	@ColumnTransformer(write="uuid_to_bin(?)")
	private UUID id;
	
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private Visibility state = Visibility.PRIVATE;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTimestamp;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date editTimestamp;
	
	@NotNull
	private String title = "";
	
	private String recipeJSON = "{}";
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy="recipes")
	private List<Tag> tags = new ArrayList<>();
	
	//TODO add connection to join table for user and recipe
	
	/**
	 * Creates a recipe with a generated id and timestamps, default state, empty recipeJSON, provided title, and empty tags
	 * @param title name of the created recipe
	 */
	public Recipe(String title) {
		this.title = title;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Recipe)) return false;
		Recipe r = (Recipe) obj;
		return this.id.equals(r.id);
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
