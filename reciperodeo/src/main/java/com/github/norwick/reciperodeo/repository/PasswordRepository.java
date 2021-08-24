package com.github.norwick.reciperodeo.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.github.norwick.reciperodeo.domain.Password;

/**
 * Spring repository for interacting with JPA object Password
 */
public interface PasswordRepository extends CrudRepository<Password, UUID>{

}
