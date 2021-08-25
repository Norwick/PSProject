package com.github.norwick.reciperodeo.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.norwick.reciperodeo.domain.User;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class UserServiceTest {

	@Autowired
	UserService us;
	
	@ParameterizedTest
	@CsvFileSource(resources = "saveUserTest.csv", numLinesToSkip=1)
	@Order(1)
	void saveUserTest(String email, String username, boolean isSearchable, String hash) {
		User u = new User();
		u.setEmail(email);
		u.setUsername(username);
		u.setIsSearchable(isSearchable);
		u.setHash(hash);
		u = us.saveUser(u);
		Assertions.assertTrue(us.findByUsername(username).isPresent());
	}
	
	@ParameterizedTest
	@CsvSource({"Mary,true","Marty,true","Manny,true","Marisa,true","Janet,false","J,false","Man,false"})
	@Order(2)
	void findByUsernameTest(String username, boolean isPresent) {
		Optional<User> ou = us.findByUsername(username);
		Assertions.assertEquals(isPresent,ou.isPresent());
	}

	
	@ParameterizedTest
	@CsvSource({"a@b.c,1","nom@gmail.com,1","zeg@z.z,2","ya@yu.c,0"})
	@Order(3)
	void findByEmailTest(String email, int numUser) {
		List<User> ol = us.findByEmail(email);
		Assertions.assertEquals(numUser, ol.size());
	}
	
	@Test
	@Order(4)
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
	@CsvSource({"a@b.c,1","nom@gmail.com,0","zeg@z.z,1","ya@yu.c,0"})
	@Order(5)
	void findBySearchableEmailTest(String email, int numUser) {
		List<User> ol = us.findBySearchableEmail(email);
		Assertions.assertEquals(numUser, ol.size());
	}
	
	//note not exhaustive test because only 4 users in db
	@ParameterizedTest
	@CsvSource({"Mary,0","Marty,1","Manny,0","Marisa,1","Janet,0","J,0","Man,0","Mar,2","r,2"})
	@Order(6)
	void findBySearchableUsernameNonExhaustiveTest(String username, int numUser) {
		List<User> ol = us.findBySearchableUsernameNonExhaustive(username);
		Assertions.assertEquals(numUser, ol.size());
	}
	
	@ParameterizedTest
	@CsvSource({"Mary,blar,false","Marisa,'',false","Johny,wak,false","Manny,yumyum,true"})
	@Order(7)
	void authenticate(String username, String hash, boolean isPresent) {
		Assertions.assertEquals(isPresent,us.authenticate(username, hash).isPresent());
	}
	
	@ParameterizedTest
	@ValueSource(strings={"Mary","Marty","Manny","Marisa"})
	@Order(8)
	void removeUserTest(String username) {
		Optional<User> ou = us.findByUsername(username);
		Assertions.assertTrue(ou.isPresent());
		User u = ou.get();
		us.removeUser(u);
		ou = us.findByUsername(username);
		Assertions.assertTrue(ou.isEmpty());
	}
}
