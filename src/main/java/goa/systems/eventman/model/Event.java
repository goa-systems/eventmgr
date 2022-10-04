package goa.systems.eventman.model;

import java.util.GregorianCalendar;
import java.util.List;

public class Event {

	private Location location;
	private GregorianCalendar start;
	private GregorianCalendar end;
	private List<Genre> styles;

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

	public List<Genre> getStyles() {
		return styles;
	}

	public void setStyles(List<Genre> styles) {
		this.styles = styles;
	}
}
