package com.github.norwick.reciperodeo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
//	
//	/**
//	 * Visible to owner, editors, and friends
//	 */
//	FRIEND, 
	
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
	
	@NotNull
	@Size(min=1, max=2)
	private String emoji = "????";
	
	@Lob
	private String recipeJSON = "{}";
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy="recipes")
	private Set<Tag> tags = new HashSet<>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	/**
	 * Creates a recipe with a generated id and timestamps, default state, empty recipeJSON, provided title, and empty tags
	 * @param title name of the created recipe
	 */
	public Recipe(String title) {
		this.title = title;
	}
	
	
	/**
	 * Gets three or less tags of recipe and returns string representing it
	 * @return string representing three or less tags the recipe has
	 */
	public String getFirstThreeTagString() {
		if (tags.isEmpty()) return "None";
		StringJoiner sj = new StringJoiner(", ");
		int i = 0;
		for (Tag t : tags) {
			if (i > 2) break;
			i++;
			sj.add(t.getName());
		}
		return sj.toString();
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
	
	@Override
	public String toString() {
		return this.title + ": Created on " + this.creationTimestamp;
	}
}
