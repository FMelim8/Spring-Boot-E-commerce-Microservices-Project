# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container with its original name
COPY target/gameorder-1.2.0.jar game-order-service.jar

# Expose the service port
EXPOSE 9095

# Run the application
ENTRYPOINT ["java", "-jar", "game-order-service.jar"]