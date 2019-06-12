[![CircleCI](https://circleci.com/gh/buckle/toxiproxy-frontend/tree/master.svg?style=shield)](https://circleci.com/gh/buckle/toxiproxy-frontend/tree/master)
# Toxiproxy Frontend
**Toxiproxy Frontend** is an application that provides a GUI to [Shopify's Toxiproxy](https://github.com/Shopify/toxiproxy) as well as other functionality
such as backing up proxies if you want them persisted.

## Screenshots
[Proxy Listing](screenshots/proxies.png)

[Proxy Details](screenshots/proxy-detail.png) 
 
## Docker
### Getting Image
```
docker pull buckle/toxiproxy-frontend
```

### Running
#### Arguments

These are docker standard arguments, just going over why they are needed for the Toxiproxy Frontend application.

* `-p`: Maps the external listening port to the internal port. `8080` is always the internal port.
* `--env-file`: The environment variables used in the docker image. It's what configures the running application. 
  * Example file: [example.list](docker/example.list)
* `--mount`: If you want to use HTTPS and have it terminate at the service you need to mount a cert file to be used. *Not required.
  * The `destination` part of the mount will need to be configured correctly in the environment variables used in the `env-file` argument. 

Fully Configured Docker Command
```
docker run -tid -p 8080:8080 --env-file environment.list --mount type=bind,source=/cert/path/file.pkcs12,destination=/docker/cert/path/file.pkcs12,readonly=true buckle/toxiproxy-frontend
```

## Building / Running 
* Download and install Java 11 OpenJDK.
* Run `./gradlew clean build -x test -x jacocoTestCoverageVerification`
* Grab jar from `server/build/libs/server.jar`, it'll have the frontend content included. 
* Run the jar using `java -jar server.jar`