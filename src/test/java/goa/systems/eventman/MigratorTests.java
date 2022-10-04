package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import goa.systems.eventman.setup.Migrator;

@SpringBootTest
class MigratorTests {

	@Autowired
	Migrator migrator;

	@Test
	void testVersions() {

		assertDoesNotThrow(() -> {
			String[] versions = migrator.getVersions();
			assertEquals(1, versions.length);
			assertEquals("0.0.1", versions[0]);
		});
	}

	@Test
	void testDatabaseVersion() {
		assertDoesNotThrow(() -> {
			String version = migrator.getDatabaseVersion();
			assertEquals("0.0.1", version);
		});
	}
}
