package goa.systems.eventman.control;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
	private NamedParameterJdbcTemplate jt1;

	@Autowired
	private JdbcTemplate jt2;

	public List<Event> generateExampleEvents() {
		Type t = new TypeToken<List<Event>>() {
		}.getType();
		return Tooling.getGson().fromJson(
				new JsonReader(new InputStreamReader(EventGenerator.class.getResourceAsStream("/example.json"))), t);
	}

	public List<Event> getEvents() {
		return jt1.query(InputOutput.readString(EventGenerator.class.getResourceAsStream("/sql/getevents.sql")),
				new EventResultSetExtractor());
	}

	List<Genre> getGenres(Event e) {
		return getGenres(e.getId());
	}

	List<Genre> getGenres(long eventid) {
		return getGenres(BigInteger.valueOf(eventid));
	}

	List<Genre> getGenres(BigInteger eventid) {
		String sql = String.format(
				"SELECT genre_id, name FROM events_have_genres LEFT OUTER JOIN genres ON genre_id = genres.id WHERE event_id = %d",
				eventid);
		return jt1.query(sql, new RowMapper<Genre>() {

			@Override
			public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
				Genre g = new Genre();
				g.setId(BigInteger.valueOf(rs.getLong("genre_id")));
				g.setName(rs.getString("name"));
				return g;
			}
		});
	}

	void updateGenres(Event e) {

		/* Returns a list of genres currently in the database. */
		List<Genre> currentgenres = getGenres(e);
		List<Genre> newgenres = e.getGenres();

		List<Genre> deleteables = new ArrayList<>();
		List<Genre> addables = new ArrayList<>();

		for (Genre genre : currentgenres) {
			if (!newgenres.contains(genre)) {
				deleteables.add(genre);
			}
		}

		for (Genre genre : newgenres) {
			if (!currentgenres.contains(genre)) {
				addables.add(genre);
			}
		}

		if (!deleteables.isEmpty()) {
			StringBuilder ids = new StringBuilder();
			int i;
			for (i = 0; i < deleteables.size() - 1; i++) {
				ids.append(String.format("%d,", deleteables.get(i).getId()));
			}
			ids.append(String.format("%d,", deleteables.get(i).getId()));
			String sql = String.format("DELETE FROM events_have_genres WHERE event_id = %dD genre_id IN (%s)",
					e.getId(), ids);
			jt2.update(sql);
		}

	}
}
