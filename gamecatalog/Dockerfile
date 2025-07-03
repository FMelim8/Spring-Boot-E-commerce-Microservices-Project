# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container with its original name
COPY target/gamecatalog-1.2.0.jar game-catalog-service.jar

# Expose the service port
EXPOSE 9094

# Run the application
ENTRYPOINT ["java", "-jar", "game-catalog-service.jar"]