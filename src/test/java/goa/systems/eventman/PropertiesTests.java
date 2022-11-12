package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import goa.systems.eventman.control.LogicLib;

@SpringBootTest
@ActiveProfiles("test")
class PropertiesTests {

	@Autowired
	LogicLib ll;

	@Value("${spring.datasource.url}")
	private String datasourceurl;

	@Test
	void testProperties() {
		assertTrue(datasourceurl.endsWith("_5"));
	}
}
