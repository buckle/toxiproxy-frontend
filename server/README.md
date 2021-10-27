# Toxiproxy Backend - Server

The server framework is a Java Spring Boot application. It's a REST based application that provides an API to the frontend to communicate to 
[Shopify's Toxiproxy](https://github.com/Shopify/toxiproxy) service. 

## Properties
* `toxiproyx.url`
  * The endpoint used by the server to communicate with the running Toxiproxy service. 
  * Defaults: `http://localhost:8474`
* `backup.enabled`
  * Sets if backups are enabled.
  * Defaults: true
  * Type - Boolean
* `backup.cadence`
  * Sets the cadence when the Toxiproxy service is checked for changes and backed up.
  * Defaults: 30000 
  * Type - Long 
* `spring.datasource.url`
  * The JDBC DB url used for backups.
  * Defaults: Uses in memory H2, but not used if backups disabled. 
* `spring.datasource.username`
  * The DB username.
* `spring.datasource.password`
  * The DB password for the supplied username. 
* `server.ssl.keyStore`
  * The location of the keystore if using TLS.
* `server.ssl.keyStorePassword`
  * If needed, the password for the keystore above. 
* `server.ssl.keyStoreType`
  * The type of keystore used. (PKCS12, plus others)
* `logging.file.name`
  * Full path and name of the file
  * Default: /var/log/toxiproxy_frontend.log
* `logging.pattern.file`
  * Logging pattern used in the file
  * Default: `%d{&quot;yyyy-MM-dd'T'HH:mm:ss.SSSXXX&quot;, America/Chicago} %5p %c{1}:%L - %m%n`
 
- There's other properties that can be set in Spring Boot applications but the ones above are the most applicable ones. These properties when used in
a Docker instance will be set as an environment variable with Spring Boot will pick up. 
  
## Running
### Environment

You'll need Java 11 installed to run the application and any of its build tools. This is a Spring Boot project so any profiles or environment property 
inheritance will be applied. There is plenty of documentation around Spring Boot and how it works.   

#### IDE (Intellij + others)
Right click on `ServerApplication` and run. 

#### Gradle
Run `../gradlew bootRun` from `toxiproxy-frontend/server`. This will build the frontend module as well.

### Testing

Testing is done using both unit and integration style testing. 100% coverage is enforced by our CI frameworks. 

#### IDE (Intellij + others)
Right click on the `test` package and run.  

#### Gradle
Run `../gradlew test` from `toxiproxy-frontend/server`.