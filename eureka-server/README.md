# Eureka Service

The **Eureka Service** is used for service discovery purposes and is used to enable seamless communication between the different services.

## Functionality

Besides enabling communication between servers, this service has a dashboard that shows the application instances currently registered with Eureka, the quantity of Availability Zones and the aplications' status. It also offers some other general information.

## Docker Instructions

To build the Docker image manually:

```bash
cd eureka-server
mvn install -DskipTests
docker build -t your-username/your-image:your-tag .
```