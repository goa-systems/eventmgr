package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import goa.systems.eventman.control.LogicLib;
import goa.systems.eventman.exceptions.InconsistentEventException;
import goa.systems.eventman.model.Event;

@SpringBootTest
class LogicLibTests {

	@Autowired
	LogicLib ll;

	@Test
	void testValidCases() {

		Event newevent = new Event();
		Event existingevent = new Event();

		assertDoesNotThrow(() -> {
			newevent.setStart(new GregorianCalendar(2022, 0, 1, 0, 0, 0));
			newevent.setEnd(new GregorianCalendar(2022, 0, 3, 0, 0, 0));
			existingevent.setStart(new GregorianCalendar(2022, 0, 2, 0, 0, 0));
			existingevent.setEnd(new GregorianCalendar(2022, 0, 4, 0, 0, 0));

			assertTrue(ll.isIntersecting(newevent, existingevent));

			newevent.setStart(new GregorianCalendar(2022, 0, 3, 0, 0, 0));
			newevent.setEnd(new GregorianCalendar(2022, 0, 5, 0, 0, 0));
			existingevent.setStart(new GregorianCalendar(2022, 0, 2, 0, 0, 0));
			existingevent.setEnd(new GregorianCalendar(2022, 0, 4, 0, 0, 0));

			assertTrue(ll.isIntersecting(newevent, existingevent));

			newevent.setStart(new GregorianCalendar(2022, 0, 2, 0, 0, 0));
			newevent.setEnd(new GregorianCalendar(2022, 0, 3, 0, 0, 0));
			existingevent.setStart(new GregorianCalendar(2022, 0, 1, 0, 0, 0));
			existingevent.setEnd(new GregorianCalendar(2022, 0, 5, 0, 0, 0));

			assertTrue(ll.isIntersecting(newevent, existingevent));

			newevent.setStart(new GregorianCalendar(2022, 0, 1, 0, 0, 0));
			newevent.setEnd(new GregorianCalendar(2022, 0, 5, 0, 0, 0));
			existingevent.setStart(new GregorianCalendar(2022, 0, 2, 0, 0, 0));
			existingevent.setEnd(new GregorianCalendar(2022, 0, 3, 0, 0, 0));

			assertTrue(ll.isIntersecting(newevent, existingevent));
		});
	}

	@Test
	void testInvalidCases() {

		Event newevent = new Event();
		Event existingevent = new Event();

		assertThrows(InconsistentEventException.class, () -> {
			newevent.setStart(new GregorianCalendar(2022, 0, 1, 0, 0, 0));
			newevent.setEnd(new GregorianCalendar(2022, 0, 5, 0, 0, 0));
			existingevent.setStart(new GregorianCalendar(2022, 0, 4, 0, 0, 0));
			existingevent.setEnd(new GregorianCalendar(2022, 0, 3, 0, 0, 0));
			ll.isIntersecting(newevent, existingevent);
		});

		assertThrows(InconsistentEventException.class, () -> {
			newevent.setStart(new GregorianCalendar(2022, 0, 5, 0, 0, 0));
			newevent.setEnd(new GregorianCalendar(2022, 0, 1, 0, 0, 0));
			existingevent.setStart(new GregorianCalendar(2022, 0, 2, 0, 0, 0));
			existingevent.setEnd(new GregorianCalendar(2022, 0, 3, 0, 0, 0));
			ll.isIntersecting(newevent, existingevent);
		});

		assertThrows(InconsistentEventException.class, () -> {
			newevent.setStart(new GregorianCalendar(2022, 0, 5, 0, 0, 0));
			newevent.setEnd(new GregorianCalendar(2022, 0, 1, 0, 0, 0));
			existingevent.setStart(new GregorianCalendar(2022, 0, 4, 0, 0, 0));
			existingevent.setEnd(new GregorianCalendar(2022, 0, 3, 0, 0, 0));
			ll.isIntersecting(newevent, existingevent);
		});

		assertThrows(NullPointerException.class, () -> {
			ll.isIntersecting(null, existingevent);
		});

		assertThrows(NullPointerException.class, () -> {
			ll.isIntersecting(newevent, null);
		});

		assertThrows(NullPointerException.class, () -> {
			ll.isIntersecting(null, null);
		});
	}
}
