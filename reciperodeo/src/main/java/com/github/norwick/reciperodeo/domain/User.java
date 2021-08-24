package com.github.norwick.reciperodeo.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
	private Set<RecipeAccess> recipeAccesses = new HashSet<>();
	
//	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//	private Password password;
//	
//	@ManyToOne(fetch = FetchType.EAGER)
//	private Theme theme;
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable
//	private Set<Tag> tags = new HashSet<>();
//	
//	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable
//	private Set<User> friends = new HashSet<>();
//	
//	@ManyToMany(fetch = FetchType.EAGER, mappedBy="friends")
//	private Set<User> friendOf = new HashSet<>();
//	
//	/**
//	 * Creates password for user from hash and sets password
//	 * @param hash hashed password to use
//	 * @return created password
//	 */
//	public Password createPassword(String hash) {
//		Password p = new Password();
//		p.setHash(hash);
//		this.setPassword(p);
//		return p;
//	}
//	
//	/**
//	 * Adds provided user to friends list
//	 * @param u provided user
//	 */
//	public void addFriend(User u) {
//		this.friends.add(u);
//	}
//	
//	/**
//	 * Removes provided user from friends list
//	 * @param u provided user
//	 */
//	public void removeFriend(User u) {
//		this.friends.remove(u);
//	}
//	
//	/**
//	 * Adds provided tag to tags user tracks
//	 * @param t provided tag
//	 */
//	public void addTag(Tag t) {
//		this.tags.add(t);
//	}
//	
//	/**
//	 * Removes provided tag from tags user tracks
//	 * @param t provided tag
//	 */
//	public void removeTag(Tag t) {
//		this.tags.remove(t);
//	}
	
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
