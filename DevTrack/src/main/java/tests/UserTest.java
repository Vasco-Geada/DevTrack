package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import devtrack.exception.InvalidEntityException;
import devtrack.model.User;

class UserTest {
	// Attributes
	User user = new User("nome", "teste@email.com");

	// Tests
	@Test
	void testUser() {
		assertThrows(InvalidEntityException.class, () -> {
			new User("", "");
		});

	}
	@Test
	void testSetName() {
		assertThrows(InvalidEntityException.class, () -> {
			user.setName(null);
		});
		
	}
	@Test
	void testSetEmail() {
		assertThrows(InvalidEntityException.class, () -> {
			user.setEmail("teste");
		});
		
	}
}
