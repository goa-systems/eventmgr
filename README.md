# GOA systems event manager

## Developer setup

Please use the SQL scripts in the folder `src/main/resources/sql` to setup the database. Copy the file `application.properties.tpl` in the folder `src/main/resources` to `application.properties` and configure the database access. If this is not done the JUnit tests will fail.

## Versions

|Version|Description|
|-|-|
|0.0.1|Basic functionality for API and database.<ul><li>Create events</li><li>Modify events</li><li>Delete events</li><li>Initialize database</li></ul>|