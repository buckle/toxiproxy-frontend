FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.3_7 AS build

COPY . /toxiproxy/
WORKDIR /toxiproxy
RUN ./gradlew clean build -x test -x jacocoTestCoverageVerification

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.3_7
COPY --from=build /toxiproxy/server/build/libs/server.jar /toxiproxy/
WORKDIR /toxiproxy
EXPOSE 8080
ENTRYPOINT ["java",  "-jar",  "server.jar"]
CMD []
