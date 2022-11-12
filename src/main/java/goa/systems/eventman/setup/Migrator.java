package goa.systems.eventman.setup;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import goa.systems.commons.io.InputOutput;
import goa.systems.eventman.exceptions.DataUpdateException;

@Component
public class Migrator {

	private static final Logger logger = LoggerFactory.getLogger(Migrator.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	/**
	 * Reads a list of SQL commands from a SQL script file. Commands need to be
	 * separated by double '\n'.
	 * 
	 * @param version Definition of version to load SQL commands from.
	 * @return List of SQL commands as strings.
	 */
	public List<String> getSqlCommands(String version) {
		List<String> l = new ArrayList<>();
		String sqlscript = InputOutput
				.readString(Migrator.class.getResourceAsStream(String.format("/migrations/%s.sql", version)));
		String[] sqlcommands = sqlscript.split("\n--\n");
		for (String command : sqlcommands) {
			l.add(command.trim());
		}

		return l;
	}

	/**
	 * Reads the defined versions from "migrations/versions
	 * 
	 * @return List of defined versions.
	 */
	public List<String> getVersions() {
		List<String> v = new ArrayList<>();
		String sqlscript = InputOutput.readString(Migrator.class.getResourceAsStream("/migrations/versions.txt"));
		String[] versions = sqlscript.split("\n");
		for (String version : versions) {
			v.add(version.trim());
		}
		return v;
	}

	/**
	 * Reads the current application version from the resource and returns the
	 * value.
	 * 
	 * @return current application version.
	 * @throws IOException if error occurs during reading of file containing
	 *                     versions.
	 */
	public String getVersion() throws IOException {
		return InputOutput.readString(Migrator.class.getResourceAsStream("/version.txt"));
	}

	/**
	 * Executes the given SQL commands against the configured database.
	 * 
	 * @param sqls List of SQL commands
	 * @throws DataAccessException if executing SQL commands fails.
	 */
	public void executeSqls(List<String> sqls) throws DataAccessException {
		for (String sql : sqls) {
			executeSql(sql);
		}
		logger.debug("Execution successful.");
	}

	/**
	 * Executes the given SQL command against the configured database.
	 * 
	 * @param sql Command to execute.
	 * @throws DataAccessException if executing SQL command fails.
	 */
	public void executeSql(String sql) throws DataAccessException {
		jdbcTemplate.getJdbcTemplate().execute(sql);
		logger.debug("Execution successful.");
	}

	/**
	 * Reads the current version string from the database.
	 * 
	 * @return The current database versions.
	 */
	public String getDatabaseVersion() {
		String sql = "SELECT `value` FROM `preferences` WHERE `key` = 'dbversion'";

		try {
			return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
				@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return rs.getString(1);
					}
					return "";
				}
			});
		} catch (DataAccessException e) {
			logger.error("Can not get current version from database. Assuming empty database.", e);
			return "0.0.0";
		}
	}

	/**
	 * Checks if the currently connected database is a production, test or
	 * development database.
	 * 
	 * @return The current database versions.
	 */
	public boolean isProduction() {
		String sql = "SELECT `value` FROM `preferences` WHERE `key` = 'dbenv'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<Boolean>() {
			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					return "prod".compareTo(rs.getString(1)) == 0;
				}
				/* Return true in case of error to prevent data loss. */
				return true;
			}
		});
	}

	/**
	 * Updates the given preference.
	 * 
	 * @param key name of key value pair. Data type VARCHAR(255)
	 * @param val value of key value pair. Data type TEXT
	 * @throws DataUpdateException in case value for key can not be updated.
	 */
	public void updatePrefs(String key, String val) throws DataUpdateException {
		String sql = "UPDATE `preferences` SET `value` = :val WHERE `key` = :key";
		MapSqlParameterSource p = new MapSqlParameterSource();
		p.addValue("key", key);
		p.addValue("val", val);
		if (jdbcTemplate.update(sql, p) == 0) {
			throw new DataUpdateException(String.format("Can not update key %s with value %s.", key, val));
		}
	}

	/**
	 * Migrates the database to the latest version.
	 * 
	 * @throws IOException         if current application version can not be loaded.
	 * @throws DataUpdateException if database environment can not be updated.
	 */
	public void migrate() throws IOException, DataUpdateException {
		migrate("dev");
	}

	/**
	 * Migrates the database to the latest version.
	 * 
	 * @throws IOException         if current application version can not be loaded.
	 * @throws DataUpdateException if database environment can not be updated.
	 */
	public void migrate(String dbenv) throws IOException, DataUpdateException {
		String applicationVersion = getVersion();
		String databaseVersion = getDatabaseVersion();
		List<String> availableVersions = getVersions();
		boolean execute = false;
		if (!applicationVersion.equals(databaseVersion)) {

			if ("0.0.0".equals(databaseVersion)) { // Database not initialized.
				execute = true;
			}

			for (String availableVersion : availableVersions) {
				if (execute) {
					executeSqls(getSqlCommands(availableVersion));
				} else if (availableVersion.equals(databaseVersion)) {
					execute = true;
				}
			}

		} else {
			logger.info("No migration needed. Application version is {}, Database version is {}.", applicationVersion,
					databaseVersion);
		}
		updatePrefs("dbenv", dbenv);
	}

	public List<String> getTables() {
		String sql = "SHOW TABLES";
		return jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		});
	}

	public void drop() {

		String database = jdbcTemplate.query("SELECT DATABASE()", new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs != null && rs.next() ? rs.getString(1) : "";
			}
		});
		Map<String, String> params = new HashMap<>();
		params.put("db", database);
		jdbcTemplate.execute("DROP DATABASE :db", params, new PreparedStatementCallback<String>() {

			@Override
			public String doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.execute();
				return null;
			}
		});

		logger.info("Database {} dropped successfully.", database);
	}
}
