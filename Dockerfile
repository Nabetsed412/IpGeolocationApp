FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN wget https://repo1.maven.org/maven2/com/h2database/h2/2.1.214/h2-2.1.214.jar -O /app/h2.jar
COPY target/IpGeolocationApp-1.0-SNAPSHOT.jar /app/IpGeolocationApp.jar
ENTRYPOINT ["java", "-jar", "IpGeolocationApp.jar"]
