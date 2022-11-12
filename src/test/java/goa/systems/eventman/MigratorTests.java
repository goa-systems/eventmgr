package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import goa.systems.eventman.setup.Migrator;

@SpringBootTest
@ActiveProfiles("test")
class MigratorTests {

	@Autowired
	Migrator migrator;

	@Test
	void testVersions() {

		assertDoesNotThrow(() -> {
			List<String> versions = migrator.getVersions();
			assertEquals(1, versions.size());
			assertEquals("0.0.1", versions.get(0));
		});
	}

	@Test
	void testDatabaseVersion() {
		assertDoesNotThrow(() -> {
			String version = migrator.getDatabaseVersion();
			assertEquals("0.0.1", version);
		});
	}

	@Test
	void testSqlCommands() {
		assertDoesNotThrow(() -> {
			List<String> statements = migrator.getSqlCommands("0.0.1");
			assertEquals(6, statements.size());
		});
	}

	@Test
	void getTablesTest() {
		assertDoesNotThrow(() -> {
			List<String> tables = migrator.getTables();
			assertEquals(5, tables.size());
		});
	}
}
