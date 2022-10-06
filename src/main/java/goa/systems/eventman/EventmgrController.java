package goa.systems.eventman;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import goa.systems.eventman.control.EventGenerator;
import goa.systems.eventman.model.Event;
import goa.systems.eventman.stuff.Tooling;

@RestController
public class EventmgrController {

	private static final Logger logger = LoggerFactory.getLogger(EventmgrController.class);

	@Autowired
	EventGenerator eg;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping(path = "/example1")
	public ResponseEntity<String> getExample() {
		HttpHeaders httpheaders = new HttpHeaders();
		httpheaders.setContentType(MediaType.APPLICATION_JSON);
		return ResponseEntity.ok().headers(httpheaders).body(Tooling.getGson().toJson(eg.generateExampleEvents()));
	}

	@PutMapping("/events")
	ResponseEntity<String> replaceEmployee(@RequestBody String event) {
		Event e = Tooling.getGson().fromJson(event, Event.class);
		e.setId(createEvent(e));
		HttpHeaders httpheaders = new HttpHeaders();
		httpheaders.setContentType(MediaType.APPLICATION_JSON);
		return ResponseEntity.ok().headers(httpheaders).body(Tooling.getGson().toJson(e));
	}

	private BigInteger createEvent(Event e) {
		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO `events` (`start`, `end`, `location_id`) VALUES (?, ?, ?)";
		jdbcTemplate.update(conn -> {
			PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setTimestamp(1, new Timestamp(e.getStart().getTimeInMillis()));
			preparedStatement.setTimestamp(2, new Timestamp(e.getEnd().getTimeInMillis()));
			preparedStatement.setBigDecimal(3, new BigDecimal(e.getLocation().getId()));

			return preparedStatement;
		}, generatedKeyHolder);
		try {
			return BigInteger.valueOf(generatedKeyHolder.getKey().longValue());
		} catch (NullPointerException ex) {
			logger.error("Error retrieving generated key.", ex);
			return BigInteger.valueOf(-1);
		}
	}
}
