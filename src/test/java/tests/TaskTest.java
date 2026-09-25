package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import devtrack.exception.InvalidEntityException;
import devtrack.model.Task;
import devtrack.model.Enums.Priority;

class TaskTest {
	// Attributes
	Task task = new Task("Fix login bug", "This is a description", Priority.LOW);

	// Tests

	@Test
	void estimatedHours_True_over0() {
		int number = 5;
		task.setEstimatedHours(number);
		
		assertTrue( task.getEstimatedHours() > 0);

		assertThrows(InvalidEntityException.class, () -> {
			task.setEstimatedHours(-3);
		});
	}
}
