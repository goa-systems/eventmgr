package goa.systems.eventman.model;

import java.util.GregorianCalendar;

public class Event {

	private Location location;
	private GregorianCalendar start;
	private GregorianCalendar end;

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
}
