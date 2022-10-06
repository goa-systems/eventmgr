package goa.systems.eventman;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import goa.systems.eventman.exceptions.DataUpdateException;
import goa.systems.eventman.setup.Migrator;

@SpringBootApplication
public class EventmgrApplication {

	private static final Logger logger = LoggerFactory.getLogger(EventmgrApplication.class);

	@Autowired
	Migrator mc;

	public static void main(String[] args) {
		SpringApplication.run(EventmgrApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		try {
			mc.migrate();
		} catch (IOException | DataUpdateException e) {
			logger.error("Error in database migration.", e);
		}
	}
}
