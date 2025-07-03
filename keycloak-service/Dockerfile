# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container with its original name
COPY target/keycloak-service-1.2.0.jar keycloak-service.jar

# Expose the service port
EXPOSE 8091

# Run the application
ENTRYPOINT ["java", "-jar", "keycloak-service.jar"]