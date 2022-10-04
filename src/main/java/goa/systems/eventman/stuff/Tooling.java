package goa.systems.eventman.stuff;

import java.util.GregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Tooling {

	private Tooling() {
	}

	public static Gson getGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(GregorianCalendar.class, new GregorianCalendarDeserializer());
		gsonBuilder.registerTypeAdapter(GregorianCalendar.class, new GregorianCalendarSerializer());
		return gsonBuilder.create();
	}
}
