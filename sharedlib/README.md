# Shared Library Module

This module contains shared functionality used across all microservices in the system. It is designed to promote consistency, reduce duplication, and centralize key concerns such as logging, exception handling and JWT management.

## Features

### Logging Annotations & Aspect

- **Annotations**:
  - `@LoggableController`: Marks REST endpoints whose requests and responses should be logged.
  - `@LoggableException`: Marks exception handler methods that should be logged.
  - `@Sensitive`: Marks sensitive fields (e.g., passwords, tokens) to be censored in logs.

- **Aspect**:
  - Intercepts and logs methods annotated with `@LoggableController` and `@LoggableException`.
  - For endpoints, logs key information:
    - HTTP code and path
    - Request body (with sensitive fields censored)
    - Response body
    - Execution time

### DTOs for Kafka Messaging

- Shared Data Transfer Objects used in Kafka communication between services.
- Ensures compatibility and type safety across service boundaries.

### Custom Exception Handling

- Contains a `ServiceCustomException` and a centralized exception handling mechanism.
- Handles and formats various exception types to provide consistent error responses across all services.

### JWT Converter

- Utility to parse and convert JWT tokens.
- Extracts user details and roles to be used internally in services.

### Rollbar Integration

- Centralized configuration for [Rollbar](https://rollbar.com/) exception tracking.
- Can be enabled by services to report runtime exceptions for observability and debugging.

## Usage

1. Add the shared module as a dependency in your service's `pom.xml`.
2. Annotate your endpoints and exception handlers using:
   - `@LoggableController`
   - `@LoggableException`
   - `@Sensitive` (on DTO fields)
3. Add your Rollbar token to each service's `application.yaml` or `application.properties`.
