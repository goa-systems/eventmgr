package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import goa.systems.eventman.control.EventGenerator;
import goa.systems.eventman.model.Event;
import goa.systems.eventman.setup.Migrator;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class EventGeneratorTests {

	@Autowired
	EventGenerator eg;

	@Autowired
	Migrator m;

	@BeforeAll
	public void setup() {
		assertDoesNotThrow(() -> {
			m.executeSqls(m.getSqlCommands(Migrator.class.getResourceAsStream("/dropdatabase.sql")));
			m.migrate();
			List<String> sqls = m.getSqlCommands(PropertiesTests.class.getResourceAsStream("/inserttestdata.sql"));
			m.executeSqls(sqls);
		});
	}

	@Test
	void testEventsLoading() {
		assertDoesNotThrow(() -> {
			List<Event> events = eg.getEvents();
			assertTrue(events.size() > 0);
		});
	}
}
