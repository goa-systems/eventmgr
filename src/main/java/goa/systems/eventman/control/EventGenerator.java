package goa.systems.eventman.control;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import goa.systems.eventman.model.Event;
import goa.systems.eventman.stuff.Tooling;

@Component
public class EventGenerator {

	@Autowired
	LogicLib logiclib;

	public List<Event> generateExampleEvents() {
		Type t = new TypeToken<List<Event>>() {
		}.getType();
		return Tooling.getGson().fromJson(
				new JsonReader(new InputStreamReader(EventGenerator.class.getResourceAsStream("/example.json"))), t);
	}
}
