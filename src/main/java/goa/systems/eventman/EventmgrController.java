package goa.systems.eventman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import goa.systems.eventman.control.EventGenerator;
import goa.systems.eventman.stuff.Tooling;

@RestController
public class EventmgrController {

	@Autowired
	EventGenerator eg;

	@GetMapping(path = "/example1")
	public ResponseEntity<String> getExample() {
		HttpHeaders httpheaders = new HttpHeaders();
		httpheaders.setContentType(MediaType.APPLICATION_JSON);
		return ResponseEntity.ok().headers(httpheaders).body(Tooling.getGson().toJson(eg.generateExampleEvents()));
	}
}
