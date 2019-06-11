FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.3_7 AS build

COPY . /root/toxiproxy
WORKDIR /root/toxiproxy
RUN ./gradlew clean build -x test -x jacocoTestCoverageVerification

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.3_7
COPY --from=build /root/toxiproxy/server/build/libs/server.jar /root
WORKDIR /root
EXPOSE 8080
ENTRYPOINT java -jar server.jar