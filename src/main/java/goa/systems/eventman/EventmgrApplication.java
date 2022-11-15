package goa.systems.eventman;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import goa.systems.eventman.exceptions.DataUpdateException;
import goa.systems.eventman.setup.Migrator;

@SpringBootApplication
public class EventmgrApplication {

	private static final Logger logger = LoggerFactory.getLogger(EventmgrApplication.class);

	@Autowired
	Migrator mc;

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(EventmgrApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {

		/* If it is a unit test, drop the database first. */
		if (!List.of(this.environment.getActiveProfiles()).contains("test")) {
			try {
				mc.migrate();
			} catch (IOException | DataUpdateException e) {
				logger.error("Error in database migration.", e);
			}
		}
	}
}
