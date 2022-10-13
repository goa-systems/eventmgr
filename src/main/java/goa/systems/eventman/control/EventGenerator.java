package goa.systems.eventman.control;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import goa.systems.commons.io.InputOutput;
import goa.systems.eventman.model.Event;
import goa.systems.eventman.model.Genre;
import goa.systems.eventman.stuff.EventResultSetExtractor;
import goa.systems.eventman.stuff.Tooling;

@Component
public class EventGenerator {

	@Autowired
	LogicLib logiclib;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<Event> generateExampleEvents() {
		Type t = new TypeToken<List<Event>>() {
		}.getType();
		return Tooling.getGson().fromJson(
				new JsonReader(new InputStreamReader(EventGenerator.class.getResourceAsStream("/example.json"))), t);
	}

	public List<Event> getEvents() {
		return jdbcTemplate.query(
				InputOutput.readString(EventGenerator.class.getResourceAsStream("/sql/getevents.sql")),
				new EventResultSetExtractor());
	}

	List<Genre> getGenres(Event e) {
		String sql = String.format(
				"SELECT genre_id, name FROM events_have_genres LEFT OUTER JOIN genres ON genre_id = genres.id WHERE event_id = %d",
				e.getId());
		return jdbcTemplate.query(sql, new RowMapper<Genre>() {

			@Override
			public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
				Genre g = new Genre();
				g.setId(BigInteger.valueOf(rs.getLong("genre_id")));
				g.setName(rs.getString("name"));
				return g;
			}
		});
	}
}
