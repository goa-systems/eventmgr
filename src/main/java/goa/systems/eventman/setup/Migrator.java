package goa.systems.eventman.setup;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import goa.systems.commons.io.InputOutput;

@Component
public class Migrator {

	@Autowired
	JdbcTemplate jdbctemplate;

	private Migrator() {
	}

	private static final Logger logger = LoggerFactory.getLogger(Migrator.class);

	public String[] getVersions() {
		String s = InputOutput.readString(Migrator.class.getResourceAsStream("/versions.txt"));
		return s == null ? null : s.split("\n");
	}

	public String getDatabaseVersion() {

		String dbversion = null;

		try {
			dbversion = jdbctemplate.query("SELECT `value` FROM `preferences` WHERE `key` = 'dbversion'",
					new ResultSetExtractor<String>() {

						@Override
						public String extractData(ResultSet rs) throws SQLException, DataAccessException {
							if (rs != null && rs.next()) {
								return rs.getString(1);
							}
							return "0.0.0";
						}
					});
		} catch (

		BadSqlGrammarException e) {
			logger.error("Can not determine database version.", e);
			dbversion = "0.0.0";
		}

		return dbversion;
	}
}
