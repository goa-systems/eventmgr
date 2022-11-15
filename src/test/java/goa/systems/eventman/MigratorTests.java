package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import goa.systems.eventman.setup.Migrator;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class MigratorTests {

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
	void testVersions() {

		assertDoesNotThrow(() -> {
			List<String> versions = m.getVersions();
			assertEquals(1, versions.size());
			assertEquals("0.0.1", versions.get(0));
		});
	}

	@Test
	void testDatabaseVersion() {
		assertDoesNotThrow(() -> {
			String version = m.getDatabaseVersion();
			assertEquals("0.0.1", version);
		});
	}

	@Test
	void testSqlCommands() {
		assertDoesNotThrow(() -> {
			List<String> statements = m.getSqlCommands("0.0.1");
			assertEquals(7, statements.size());
		});
	}

	@Test
	void getTablesTest() {
		assertDoesNotThrow(() -> {
			List<String> tables = m.getTables();
			assertEquals(5, tables.size());
		});
	}
}
