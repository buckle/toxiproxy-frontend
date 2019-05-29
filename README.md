# Toxiproxy

## Building Both Modules
* Run `./gradlew build -x test -x jacocoTestCoverageVerification`
* Grab jar from `server/build/libs/server*.jar`, it'll have the frontend content included. 
* Run the jar using `java -jar jarname.jar` 