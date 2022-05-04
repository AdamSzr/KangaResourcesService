FROM openjdk:18.0.1.1
COPY target/data_service_kotlin-0.0.1-SNAPSHOT.jar .
COPY src/main/resources/static/data static/data

ENV PWD="/"

RUN ls
CMD ./mvnw clean package
ENTRYPOINT  ["java", "-jar", "data_service_kotlin-0.0.1-SNAPSHOT.jar"]