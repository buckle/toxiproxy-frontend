FROM eclipse-temurin:21-jdk AS build

COPY . /toxiproxy/
WORKDIR /toxiproxy
RUN ./gradlew clean build -x test -x jacocoTestCoverageVerification

FROM eclipse-temurin:21-jdk
COPY --from=build /toxiproxy/server/build/libs/server.jar /toxiproxy/
WORKDIR /toxiproxy
EXPOSE 8080
ENTRYPOINT ["java",  "-jar",  "server.jar"]
CMD []