package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import goa.systems.eventman.control.EventGenerator;
import goa.systems.eventman.model.Event;

@SpringBootTest
class EventGeneratorTests {

	@Autowired
	EventGenerator eg;

	@Test
	void testEventsLoading() {
		assertDoesNotThrow(() -> {
			List<Event> events = eg.getEvents();
			assertTrue(events.size() > 0);
		});
	}
}
