package com.github.norwick.reciperodeo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.norwick.reciperodeo.domain.Tag;

/**
 * Tag Repository implemented by Spring
 */
public interface TagRepository extends CrudRepository<Tag, Integer> {

	/**
	 * Finds all tags containing passed string in their name
	 * @param name substring to search for in tag name
	 * @return all tags that contain the substring in their name
	 */
	List<Tag> findByNameContaining(String name);

	/**
	 * Find single Tag corresponding to name
	 * @param name tag name to find
	 * @return optional that contains tag or null if not present
	 */
	Optional<Tag> findOneByNameIgnoreCase(String name);

}