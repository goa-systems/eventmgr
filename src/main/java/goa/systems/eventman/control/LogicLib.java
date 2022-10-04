package goa.systems.eventman.control;

import java.util.List;

import org.springframework.stereotype.Component;

import goa.systems.eventman.exceptions.InconsistentEventException;
import goa.systems.eventman.exceptions.IntersectionException;
import goa.systems.eventman.model.Event;

@Component
public class LogicLib {

	public boolean isIntersecting(Event newevent, Event existingevent)
			throws NullPointerException, InconsistentEventException {

		// If new or existing event or one of the start and end dates is null
		if (newevent == null || existingevent == null || newevent.getStart() == null || newevent.getEnd() == null
				|| existingevent.getStart() == null || existingevent.getEnd() == null) {
			throw new NullPointerException("Events or embedded dates are null.");
		}
		// Inconsistent dates like start is after the end date
		else if (newevent.getStart().compareTo(newevent.getEnd()) > 0
				|| existingevent.getStart().compareTo(existingevent.getEnd()) > 0) {
			throw new InconsistentEventException("Start and end dates are inconsistent.");
		}
		// Case 1: New event lies within existing event.
		else if (newevent.getStart().compareTo(existingevent.getStart()) >= 0
				&& newevent.getStart().compareTo(existingevent.getEnd()) <= 0
				&& newevent.getEnd().compareTo(existingevent.getStart()) >= 0
				&& newevent.getEnd().compareTo(existingevent.getEnd()) <= 0) {
			return true;
		}
		// Case 2: New event covers existing event completely.
		else if (newevent.getStart().compareTo(existingevent.getStart()) <= 0
				&& newevent.getEnd().compareTo(existingevent.getEnd()) >= 0) {
			return true;
		}
		// Case 3: New event intersects the beginning of an existing event.
		else if (newevent.getStart().compareTo(existingevent.getStart()) <= 0
				&& newevent.getEnd().compareTo(existingevent.getStart()) >= 0
				&& newevent.getEnd().compareTo(existingevent.getEnd()) <= 0) {
			return true;
		}
		// Case 4: New event intersects the end of an existing event.
		else if (newevent.getStart().compareTo(existingevent.getStart()) >= 0
				&& newevent.getStart().compareTo(existingevent.getEnd()) <= 0
				&& newevent.getEnd().compareTo(existingevent.getEnd()) >= 0) {
			return true;
		}

		return false;
	}

	public List<Event> addEvent(Event event, List<Event> events)
			throws InconsistentEventException, IntersectionException {

		boolean isintersecting = false;

		for (Event ce : events) {
			try {
				isintersecting |= isIntersecting(event, ce);
			} catch (NullPointerException e) {
				throw new NullPointerException(e.getMessage());
			} catch (InconsistentEventException e) {
				throw new InconsistentEventException("An event has inconsistent dates.");
			}
		}

		if (!isintersecting) {
			events.add(event);
		} else {
			throw new IntersectionException("New event intersects with existing events.");
		}

		return events;
	}
}
