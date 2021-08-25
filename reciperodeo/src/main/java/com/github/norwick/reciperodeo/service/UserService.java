package com.github.norwick.reciperodeo.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.norwick.reciperodeo.domain.Recipe;
import com.github.norwick.reciperodeo.domain.User;
import com.github.norwick.reciperodeo.repository.UserRepository;

/**
 * Service to interact with JPA object User
 * @author Norwick Lee
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RecipeService recipeService;
	
	/**
	 * Returns all users in database
	 * @return list of all users in database
	 */
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	/**
	 * Saves user and changes made to user in database
	 * @param u provided user
	 * @return database valid version of saved user
	 */
	public User saveUser(User u) {
		if (u == null) throw new NullPointerException("User is null");
		return userRepository.save(u);
	}
	
	/**
	 * Deletes provided user from database
	 * @param u provided user
	 */
	public void removeUser(User u) {
		if (u == null) throw new NullPointerException("User is null");
		u = userRepository.save(u);
		Set<Recipe> rs = u.getRecipes();
		for (Recipe r : rs) {
			r.setUser(null);
			recipeService.saveRecipe(r);
		}
		userRepository.delete(u);
	}
	
	/**
	 * Finds user by id
	 * @param id provided id
	 * @return optional containing user if present or null
	 */
	public Optional<User> findById(UUID id) {
		if (id == null) throw new NullPointerException("Id is null");
		return userRepository.findById(id);
	}
	
	/**
	 * Finds user by email
	 * @param email provided email
	 * @return List of all users under that email
	 */
	public List<User> findByEmail(String email) {
		if (email == null) throw new NullPointerException();
		return userRepository.findByEmail(email);
	}
	
	/**
	 * Searches for user by username
	 * @param username provided username
	 * @return optional containing user if found
	 */
	public Optional<User> findByUsername(String username) {
		if (username == null) throw new NullPointerException();
		return userRepository.findByUsername(username);
	}
	
	/**
	 * Searches for user by email
	 * @param email provided email
	 * @return list containing all public users with matching email
	 */
	public List<User> findBySearchableEmail(String email) {
		if (email == null) throw new NullPointerException();
		return userRepository.findByIsSearchableTrueAndEmail(email);
	}
	
	/**
	 * Searches for first three matches to provided substring of username for searchable users
	 * @param username substring of username
	 * @return list of three or less public users that match provided username substring
	 */
	public List<User> findBySearchableUsernameNonExhaustive(String username) {
		if (username == null) throw new NullPointerException();
		return userRepository.findFirst3ByIsSearchableTrueAndUsernameContaining(username);
	}
	
	/**
	 * Authenticates user
	 * @param username provided username
	 * @param hash provided b-crypt hash of password
	 * @return optional containing user if authentication was a success or null if not
	 */
	public Optional<User> authenticate(String username, String hash) {
		if (username == null) throw new NullPointerException();
		if (hash == null) throw new NullPointerException();
		Optional<User> ou = userRepository.findByUsername(username);
		if (ou.isEmpty()) return ou;
		User u = ou.get();
		if (u.getHash().equals(hash)) {
			return ou;
		}
		return Optional.empty();
	}
}
