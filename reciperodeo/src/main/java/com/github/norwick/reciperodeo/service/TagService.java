package com.github.norwick.reciperodeo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.norwick.reciperodeo.domain.Tag;
import com.github.norwick.reciperodeo.repository.TagRepository;

/**
 * Service to interact with tags
 * @author Norwick Lee
 */
@Service
public class TagService {

	@Autowired
	TagRepository tagRepository;
	
	/**
	 * Searches for all tag containing provided substring in name
	 * @param name the substring to search names for
	 * @return list of tags with names containing the provided substring
	 */
	public List<Tag> findByNameContaining(String name) {
		return this.tagRepository.findByNameContaining(name);
	}
	
	/**
	 * Finds tag by tag name
	 * @param name name of tag, case insensitive
	 * @return optional that contains tag if found with name or null otherwise
	 */
	public Optional<Tag> findByName(String name) {
		return tagRepository.findOneByNameIgnoreCase(name);
	}

	/**
	 * Finds a tag by id
	 * @param id the uuid identifying a tag
	 * @return an optional containing a matching tag or nothing if no id match
	 */
	public Optional<Tag> findById(int id) {
		return this.tagRepository.findById(id);
	}
	
	/**
	 * Finds whether a tag with provided id exists
	 * @param id the uuid identifying a tag
	 * @return true if tag with id exists
	 */
	public boolean existsById(int id) {
		return this.tagRepository.existsById(id);
	}
	
	/**
	 * Saves tag to database. Replaces existing tag if present.
	 * @param t tag to be saved
	 * @return the updated reference to the saved tag
	 * @throws IllegalArgumentException if user tries to save a tag with the same name as another tag
	 */
	public Tag saveTag(Tag t) throws IllegalArgumentException {
		if (t == null) throw new NullPointerException("Tag is Null");
		String name = t.getName();
		Optional<Tag> to = this.findByName(name);
		if (to.isPresent() && to.get().getId() != t.getId()) {
			throw new IllegalArgumentException("Tag name not unique");
		}
		return this.tagRepository.save(t);
	}
	
	/**
	 * Deletes tag from database.
	 * @param t tag to be deleted
	 */
	public void removeTag(Tag t) {
		if (t == null) throw new NullPointerException("Tag is Null");
		t.setRecipes(new ArrayList<>());
		t = this.tagRepository.save(t);
		this.tagRepository.delete(t);
	}
	
	/**
	 * Returns count of all tags in database
	 * @return number of tags in database
	 */
	public long count() {
		return this.tagRepository.count();
	}
}
