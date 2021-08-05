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

/**
 * JPA class for Recipe which contains id, visibility, timestamps for creation and editing, and a JSON String of the actual recipe content
 * @author Norwick Lee
 */
@Entity
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
	 * Creates a recipe with a generated id and timestamps, default state, empty recipeJSON, empty title, and empty tags
	 */
	public Recipe() {
	}
	
	/**
	 * Creates a recipe with a generated id and timestamps, default state, empty recipeJSON, provided title, and empty tags
	 * @param title name of the created recipe
	 */
	public Recipe(String title) {
		this.title = title;
	}
	
	/**
	 * @return id of recipe
	 */
	public UUID getId() {
		return this.id;
	}
	
	/**
	 * @return visibility state (private, friend, or public)
	 */
	public Visibility getState() {
		return this.state;
	}
	
	/**
	 * @return timestamp for when recipe was created
	 */
	public Date getCreationTimestamp() {
		return this.creationTimestamp;
	}
	
	/**
	 * @return timestamp for when recipe was last edited
	 */
	public Date getEditTimestamp() {
		return this.editTimestamp;
	}
	
	/**
	 * @return name of recipe
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @return string representing json of recipe, which contains actual recipe content
	 */
	public String getRecipeJSON() {
		return this.recipeJSON;
	}
	
	/**
	 * @return the tags that apply to the recipe
	 */
	public List<Tag> getTags() {
		return tags;
	}
	
	/**
	 * changes recipe id
	 * @param id the new recipe id
	 */
	public void setId(UUID id) {
		this.id = id;
	}
	
	/**
	 * changes visibility state
	 * @param state the new visibility state
	 */
	public void setState(Visibility state) {
		this.state = state;
	}
	
	/**
	 * Should never be used. Changes creation timestamp
	 * @param creationTimeStamp the new timestamp for when the recipe was created
	 * @param creationTimestamp 
	 */
	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	
	/**
	 * Changes edit timestamp
	 * @param editTimestamp the new timestamp for when the recipe was last edited
	 */
	public void setEditTimestamp(Date editTimestamp) {
		this.editTimestamp = editTimestamp;
	}
	
	/**
	 * Changes title
	 * @param title new name of recipe
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Changes recipeJSON
	 * @param recipeJSON the new string containing JSON body of recipe with instructions and ingredients
	 */
	public void setRecipeJSON(String recipeJSON) {
		this.recipeJSON = recipeJSON;
	}

	/**
	 * Changes tags to provided tags (not persist-affecting)
	 * @param tags the new list of tags that apply to the recipe
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
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
