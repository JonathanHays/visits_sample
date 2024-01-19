package org.launchcode.docvisiting;

import org.junit.jupiter.api.Test;
import org.launchcode.docvisiting.models.Location;
import org.launchcode.docvisiting.models.User;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UnitTesting {
	private static final Location location = new Location("Location 1","L1");
	private static final Offender offender = new Offender(55555,"Test","Offender");
	@Test
	public void testMatchingPassword() {
		String password = "password123";
		User user = new User("username", password);

		assertTrue(user.isMatchingPassword(password));
	}

	@Test
	public void testNonMatchingPassword() {
		String password = "password123";
		String wrongPassword = "wrongPassword";
		User user = new User("username", password);

		assertFalse(user.isMatchingPassword(wrongPassword));
	}

	@Test
	public void testLoginTypeToString() {
		User adminUser = new User("admin", "adminPassword", "John", "Doe", "john.doe@example.com", "admin");
		assertEquals("Admin", adminUser.loginTypeToString());

		User staffUser = new User("staff", "staffPassword", "Jane", "Doe", "jane.doe@example.com", "staff");
		assertEquals("Staff", staffUser.loginTypeToString());

		User visitRoomUser = new User("visitingroom", "visitRoomPassword", "Alice", "Wonderland", "alice@example.com", "visitRoom");
		assertEquals("Visiting Room Staff", visitRoomUser.loginTypeToString());

		// For any other login type, it should return the original login type
		User otherUser = new User("visitor", "otherPassword", "Bob", "Builder", "bob@example.com", "otherType");
		assertEquals("otherType", otherUser.loginTypeToString());
	}

	@Test
	public void testFullName() {
		User user = new User("john.doe", "password", "John", "Doe", "john.doe@example.com", "visitor");

		assertEquals("John Doe", user.getFullName());
	}

	@Test
	public void testIsActive() {
		Location activeLocation = new Location();
		activeLocation.setActive(true);
		assertEquals("Active", activeLocation.getIsActive());

		Location inactiveLocation = new Location();
		inactiveLocation.setActive(false);
		assertEquals("Inactive", inactiveLocation.getIsActive());
	}

	@Test
	public void testLocationGetName() {
		Location location = new Location();
		location.setName("Full Location");
		location.setAbbreviatedName("FL");

		assertEquals("Full Location", location.getName());
	}

	@Test
	public void testLocationGetAbbr() {
		Location location = new Location();
		location.setName("Full Location");
		location.setAbbreviatedName("FL");

		assertEquals("FL", location.getAbbreviatedName());
	}

	@Test
	public void testLocationIsActive() {
		Location location = new Location();
		location.setName("Full Location");
		location.setAbbreviatedName("FL");
		location.setActive(false);
		assertEquals(false, location.getActive());
		assertEquals("Inactive", location.getIsActive());
	}
	@Test
	public void testOffenderGetFullName() {
		Offender offender = new Offender();
		offender.setFirstName("John");
		offender.setLastName("Doe");

		assertEquals("John Doe", offender.getFullName());
	}
	@Test
	public void testOffenderLocation() {
		Offender offender = new Offender();
		offender.setFirstName("John");
		offender.setLastName("Doe");
		offender.setLocation(location);

		assertEquals(offender.getLocation(), location);
	}

}
