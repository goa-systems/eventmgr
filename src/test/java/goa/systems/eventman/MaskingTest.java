package goa.systems.eventman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MaskingTest {

	private static final int ISACTIVE = 16;

	@Test
	void test1() {

		int valfromdb = 146;
		int result = valfromdb & ISACTIVE;
		assertEquals(ISACTIVE, result);
		valfromdb = 162;
		result = valfromdb & ISACTIVE;
		assertNotEquals(ISACTIVE, result);

	}
}
