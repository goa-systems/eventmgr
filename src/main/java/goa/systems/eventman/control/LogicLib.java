package goa.systems.eventman.control;

import org.springframework.stereotype.Component;

import goa.systems.eventman.exceptions.InconsistentEventException;
import goa.systems.eventman.model.Event;

@Component
public class LogicLib {

	public boolean isIntersecting(Event newevent, Event existingevent)
			throws NullPointerException, InconsistentEventException {

		if (newevent == null || existingevent == null || newevent.getStart() == null || newevent.getEnd() == null
				|| existingevent.getStart() == null || existingevent.getEnd() == null) {
			throw new NullPointerException("Events or embedded dates are null.");
		}
		// Inconsistent dates: Start >= End
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
}
