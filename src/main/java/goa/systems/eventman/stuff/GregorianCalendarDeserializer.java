package goa.systems.eventman.stuff;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

public class GregorianCalendarDeserializer implements JsonDeserializer<GregorianCalendar> {

	private static Logger logger = LoggerFactory.getLogger(GregorianCalendarDeserializer.class);

	@Override
	public GregorianCalendar deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) {

		String date = element.getAsString();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(formatter.parse(date));
			return gc;
		} catch (ParseException e) {
			logger.error("Error", e);
			return null;
		}
	}
}