package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import devtrack.exception.InvalidIssueStateException;
import devtrack.model.Bug;
import devtrack.model.Issue;
import devtrack.model.User;
import devtrack.model.Enums.Status;

class IssueTest {
	// Tests

	@Test
	void testStatusChange() {
		
		Issue bug = new Bug("novo bug");
		bug.start();
		
		assertEquals( Status.IN_PROGRESS, bug.getStatus());
		
		assertThrows(InvalidIssueStateException.class, () -> {
			bug.start();
		});
		
		bug.complete();
		
		assertEquals( Status.DONE, bug.getStatus());
		
		assertThrows(InvalidIssueStateException.class, () -> {
			bug.start();
		});
		assertThrows(InvalidIssueStateException.class, () -> {
			bug.complete();
		});
		
		bug.reOpen();
		assertEquals( Status.IN_PROGRESS, bug.getStatus());
		
		assertThrows(InvalidIssueStateException.class, () -> {
			bug.start();
		});
		
		bug.restart();
		assertEquals( Status.OPEN, bug.getStatus());
		
		assertThrows(InvalidIssueStateException.class, () -> {
			bug.reOpen();
		});
		assertThrows(InvalidIssueStateException.class, () -> {
			bug.complete();
		});
		

	}


	@Test
	void testRemoveAssignable() {
		Issue bug = new Bug("novo bug");
		User user1 = new User("Vasco", "email@email.com");
		User user2 = new User("Maria", "email@email.com");
		bug.assignTo(user1);
		
		assertEquals( user1, bug.getAssignedUser());
		
		bug.assignTo(user2);
		assertEquals( user2, bug.getAssignedUser());
		
		bug.removeAssignedUser();
		
		assertNull(bug.getAssignedUser());
	}
	
	
}
