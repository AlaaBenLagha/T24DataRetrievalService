# Use an OpenJDK 11 base image
FROM adoptopenjdk:11-jdk-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the compiled JAR file into the container
COPY target/T24DataRetrievalService-0.0.1-SNAPSHOT.jar app.jar

# Copy the application.properties file into the container
COPY src/main/resources/application.properties application.properties

# Expose the port your Spring Boot application listens on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
