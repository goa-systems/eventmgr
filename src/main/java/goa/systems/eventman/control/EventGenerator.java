package goa.systems.eventman.control;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import goa.systems.eventman.model.Event;
import goa.systems.eventman.model.Genre;
import goa.systems.eventman.model.Location;
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
		String sql = "SELECT `events`.`id`, `events`.`location_id`, `events`.`start`, `events`.`end`, `locations`.`id`, `locations`.`name` FROM `events` LEFT OUTER JOIN `locations` ON `events`.`location_id` = `locations`.`id`";
		return jdbcTemplate.query(sql, new RowMapper<Event>() {

			@Override
			public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
				Event e = new Event();
				e.setId(BigInteger.valueOf(rs.getLong(1)));
				Location l = new Location();
				l.setId(BigInteger.valueOf(rs.getLong("locations.id")));
				l.setName(rs.getString("locations.name"));
				e.setLocation(l);
				e.setStart(GregorianCalendar
						.from(rs.getTimestamp("events.start").toLocalDateTime().atZone(ZoneId.systemDefault())));
				e.setEnd(GregorianCalendar
						.from(rs.getTimestamp("events.end").toLocalDateTime().atZone(ZoneId.systemDefault())));
				e.setGenres(getGenres(e));
				return e;
			}
		});
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
