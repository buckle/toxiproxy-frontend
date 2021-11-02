[![CircleCI](https://circleci.com/gh/buckle/toxiproxy-frontend/tree/master.svg?style=shield)](https://circleci.com/gh/buckle/toxiproxy-frontend/tree/master)
# Toxiproxy Frontend
**Toxiproxy Frontend** is an application that provides a GUI to [Shopify's Toxiproxy](https://github.com/Shopify/toxiproxy) as well as other functionality
such as backups.

## Screenshots
[Proxy Listing](screenshots/proxies.png)

[Proxy Details](screenshots/proxy-detail.png) 

## Quick Setup
Want to see how things work locally and quickly with Docker. The following commands will get you going. 

Replace `YOUR-HOST-IP` below, and **Toxiproxy Frontend** should be able to talk to the running Toxiproxy service. 

```
docker run -it -p 8474:8474 shopify/toxiproxy
docker run -it -p 8080:8080 -e "TOXIPROXY_URL=http://<YOUR-HOST-IP>:8474" buckle/toxiproxy-frontend
```

Navigate to `localhost:8080` and you should see the GUI frontend with a version in the corner of the Toxiproxy service being used.  
 
## Running 
### Docker
#### Tags
Any merge to master will create a new latest image. Any time a tag is created in GitHub a new image will be tagged with that version.

#### Running Image
##### Arguments
These are docker standard arguments, just going over why they are needed for the **Toxiproxy Frontend** application.

* `-p`: Maps the external listening port to the internal port. `8080` is always the internal port.
* `--env-file`: The environment variables used in the docker image. It's what configures the running application. 
  * Example file: [example.list](docker/example.list)
  * Full list of server properties are listed in the [server readme](server/README.md). They'll just need formatted like the the examples.
* `--mount`: If you want to use HTTPS and have it terminate at the service you need to mount a cert file to be used. *Not required.
  * The `destination` part of the mount will need to be configured correctly in the environment variables used in the `env-file` argument. 

##### Commands
Fully Configured Docker Command
```
docker run -tid -p 8080:8080 --env-file environment.list --mount type=bind,source=/cert/path/file.pkcs12,destination=/docker/cert/path/file.pkcs12,readonly=true buckle/toxiproxy-frontend
```

### Manual Build
* Download and install Java 17 OpenJDK.
* Run `./gradlew clean build -x test -x jacocoTestCoverageVerification`
* Grab jar from `server/build/libs/server.jar`, it'll have the frontend content included. 
* Run the jar using `java -jar server.jar`

## Development
There's two modules in this project. The frontend module and the server module. The frontend module is an Angular application while the backend
is a Java Spring Boot application. Each module has it's own respective README to explain the particulars of each. 

* [Frontend](frontend/README.md)
* [Server](server/README.md)