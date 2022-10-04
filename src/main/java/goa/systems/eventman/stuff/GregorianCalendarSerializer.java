package goa.systems.eventman.stuff;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GregorianCalendarSerializer implements JsonSerializer<GregorianCalendar> {

	@Override
	public JsonElement serialize(GregorianCalendar src, Type typeOfSrc, JsonSerializationContext context) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return new JsonPrimitive(formatter.format(src.getTime()));
	}

}