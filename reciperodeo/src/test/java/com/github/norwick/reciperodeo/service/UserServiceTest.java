package com.github.norwick.reciperodeo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.Password;
import com.github.norwick.reciperodeo.domain.User;
import com.github.norwick.reciperodeo.repository.PasswordRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	UserService us;
	
	@Autowired
	PasswordRepository pr;
	
	@ParameterizedTest
	@CsvFileSource(resources = "saveUserTest.csv", numLinesToSkip=1)
	void saveUserTest(String email, String username, boolean isSearchable, String hash) {
		User u = new User();
		u.setEmail(email);
		u.setUsername(username);
		u.setIsSearchable(isSearchable);
		u.createPassword(hash);
		u = us.saveUser(u);
		Assertions.assertTrue(us.findByUsername(username).isPresent());
		Password p = u.getPassword();
		Assertions.assertTrue(pr.findById(p.getId()).isPresent());
	}
	
	@Test
	void findByIdTest() {
		List<User> users = us.findAll();
		Assertions.assertTrue(users.size() > 0);
		Set<UUID> present = new HashSet<>();
		for (User u : users) {
			UUID id = u.getId();
			present.add(id);
			Assertions.assertTrue(us.findById(id).isPresent());
		}
		UUID nonExist = UUID.randomUUID();
		while (present.contains(nonExist)) {
			nonExist = UUID.randomUUID();
		}
		Assertions.assertTrue(us.findById(nonExist).isEmpty());
	
	}
	
	@ParameterizedTest
	@CsvSource({"a@b.c,1","nom@gmail.com,1","zeg@z.z,2","ya@yu.c,0"})
	void findByEmailTest(String email, int numUser) {
		List<User> ol = us.findByEmail(email);
		Assertions.assertEquals(numUser, ol.size());
	}
	
	@ParameterizedTest
	@CsvSource({"Mary,true","Marty,true","Manny,true","Marisa,true","Janet,false","J,false","Man,false"})
	void findByUsernameTest(String username, boolean isPresent) {
		Optional<User> ou = us.findByUsername(username);
		Assertions.assertEquals(isPresent,ou.isPresent());
	}
	
	void findBySearchableEmailTest() {
		
	}
	
	void findBySearchableUsernameNonExhaustiveTest() {
		
	}
	
	void authenticate() {
		
	}
	
	@ParameterizedTest
	@ValueSource(strings={"Mary","Marty","Manny","Marisa"})
	void removeUserTest(String username) {
		Optional<User> ou = us.findByUsername(username);
		Assertions.assertTrue(ou.isPresent());
		User u = ou.get();
		us.removeUser(u);
		ou = us.findByUsername(username);
		Assertions.assertTrue(ou.isEmpty());
	}
}
