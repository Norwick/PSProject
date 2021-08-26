package com.github.norwick.reciperodeo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.github.norwick.reciperodeo.domain.User;

/**
 * Spring repository for interacting with JPA Object User
 */
public interface UserRepository extends CrudRepository<User, UUID> {
	
	List<User> findAll();
	
	/**
	 * Finds User with provided username
	 * @param username provided username
	 * @return optional containing found user or null if not found
	 */
	Optional<User> findByUsername(String username);
	
	/**
	 * Finds User with provided email
	 * @param email provided email
	 * @return list containing found user or null if not found
	 */
	List<User> findByEmail(String email);
	
	/**
	 * Searches for user by username if they're discoverable
	 * @param username string representing fragment of username
	 * @return list of first three results for discoverable matches
	 */
	List<User> findFirst3BySearchableTrueAndUsernameContaining(String username);
	
	/**
	 * Searches for user by email if they're discoverable
	 * @param email email of user
	 * @return all users that use the provided email and are discoverable
	 */
	List<User> findBySearchableTrueAndEmail(String email);
}
