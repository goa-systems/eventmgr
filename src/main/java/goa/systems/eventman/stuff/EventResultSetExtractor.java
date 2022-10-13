package goa.systems.eventman.stuff;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import goa.systems.eventman.model.Event;
import goa.systems.eventman.model.Genre;
import goa.systems.eventman.model.Location;

public class EventResultSetExtractor implements ResultSetExtractor<List<Event>> {

	Map<BigInteger, Location> locations;
	Map<BigInteger, Genre> genres;
	Map<BigInteger, Event> events;

	public EventResultSetExtractor() {
		locations = new HashMap<>();
		genres = new HashMap<>();
		events = new HashMap<>();
	}

	@Override
	public List<Event> extractData(ResultSet rs) throws SQLException, DataAccessException {

		while (rs.next()) {
			Event e = events.get(BigInteger.valueOf(rs.getLong("events.id")));
			if (e == null) {
				e = new Event();
				e.setGenres(new ArrayList<>());
				e.setId(BigInteger.valueOf(rs.getLong("events.id")));
				e.setStart(GregorianCalendar
						.from(rs.getTimestamp("events.start").toLocalDateTime().atZone(ZoneId.systemDefault())));
				e.setEnd(GregorianCalendar
						.from(rs.getTimestamp("events.end").toLocalDateTime().atZone(ZoneId.systemDefault())));
				Location l = locations.get(BigInteger.valueOf(rs.getLong("locations.id")));
				if (l == null) {
					l = new Location();
					l.setId(BigInteger.valueOf(rs.getLong("locations.id")));
					l.setName(rs.getString("locations.name"));
					locations.put(l.getId(), l);
				}
				e.setLocation(l);
				events.put(e.getId(), e);
			}
			Genre g = getGenre(rs);
			if (!e.getGenres().contains(g)) {
				e.getGenres().add(g);
			}
		}
		return new ArrayList<>(events.values());
	}

	private Genre getGenre(ResultSet rs) throws SQLException {
		Genre g = genres.get(BigInteger.valueOf(rs.getLong("genres.id")));
		if (g == null) {
			g = new Genre();
			g.setId(BigInteger.valueOf(rs.getLong("genres.id")));
			g.setName(rs.getString("genres.name"));
			genres.put(g.getId(), g);
		}
		return g;
	}

}
