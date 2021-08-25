package com.github.norwick.reciperodeo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity representing a user of Recipe Rodeo. Has id, username, email, join date, searchability, theme, tracked tags, recipe relationships, password, and friends
 * @author Norwick Lee
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
	
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
	@Column(unique=true)
	private String username;
	
	@NotNull
	@Email
	private String email;
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date joinTimestamp;
	
	@NotNull
	private Boolean isSearchable;
	
	@NotNull
	private String hash;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="user")
	private Set<Recipe> recipes = new HashSet<>();
	
	/**
	 * Initialize new user with all non-generated fields set
	 * @param username username of user
	 * @param email email of user
	 * @param isSearchable whether user is discoverable by other users
	 * @param hash password hash
	 */
	public User(String username, String email, Boolean isSearchable, String hash) {
		this.username = username;
		this.email = email;
		this.isSearchable = isSearchable;
		this.hash = hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof User)) return false;
		User u = (User) obj;
		return this.id.equals(u.id);
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
