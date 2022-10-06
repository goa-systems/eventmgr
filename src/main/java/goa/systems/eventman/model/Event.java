package goa.systems.eventman.model;

import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.List;

public class Event {

	private BigInteger id;
	private Location location;
	private GregorianCalendar start;
	private GregorianCalendar end;
	private List<Genre> genres;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public GregorianCalendar getStart() {
		return start;
	}

	public void setStart(GregorianCalendar start) {
		this.start = start;
	}

	public GregorianCalendar getEnd() {
		return end;
	}

	public void setEnd(GregorianCalendar end) {
		this.end = end;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> styles) {
		this.genres = styles;
	}
}
