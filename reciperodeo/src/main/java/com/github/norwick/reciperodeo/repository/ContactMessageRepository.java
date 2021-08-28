package com.github.norwick.reciperodeo.repository;

import org.springframework.data.repository.CrudRepository;

import com.github.norwick.reciperodeo.domain.ContactMessage;

/**
 * Spring JPA repository for contact message.
 * At the moment only saving is done for contact messages.
 */
public interface ContactMessageRepository extends CrudRepository<ContactMessage, Integer> {

}
