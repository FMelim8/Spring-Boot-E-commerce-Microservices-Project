# Keycloak Service

This service connects to a Keycloak instance and acts as the authentication and authorization layer of the application. It handles user registration, login, and retrieval of user details.

## Keycloak Integration

- Uses the **Keycloak Admin Client** dependency to communicate with the Keycloak server.
- Performs actions like user registration via the **Client Credentials Grant**, allowing the service itself (not the user) to register users programmatically.
- Upon login, users receive a **JWT access token** issued by Keycloak.
- This JWT is then used for authentication and authorization across all other services in the system.

## Local User Table

Although authentication is managed by Keycloak, the service also maintains a local `users` table with the following fields:

- `id`: Internal service ID
- `subject`: Keycloak's subject ID
- `username`
- `email`

This data is stored locally to allow the `gameorder-service` and other services to reference user information easily.

## DTOs and Logging

- The service uses **DTOs (Data Transfer Objects)** to transfer data between layers.
- Fields in these DTOs that contain sensitive information are annotated with `@Sensitive` (from the shared library), so they are masked in logs automatically.

## Default users

The Keycloak instance that's used for authentication and authorization is pre-loaded with a realm import file for ease of use.

The realm that the services use is called `microservices-realm` and already comes with 2 users.

- The default admin user: **username:** user1, **password:** user1
- The default regular user: **username:** user2, **password:** user2

Aditional regular uses can be made through the Keycloak service, **however**, to create a new admin user, you have to access the Keycloak instance and do the following:

 1. Login into your Keycloak instance with your Keycloak credentials. (The default ones are: admin/admin)
 2. Access the realm `microservices-realm` and click on `Users`
 3. Select `Create user`
 4. Fill the user's `Username`, `Email`, then set `Email verified` to `yes` and hit `Create`
 5. Go to the `Credentials` tab, set the users password and disable the `Temporary` option
 6. Go to the `Role mapping` tab, click `Assign role` and assign the role `app_admin`

## Docker Instructions

If you prefer to build the Docker image yourself:

```bash
cd keycloak-service
mvn install -DskipTests
docker build -t your-username/your-image:your-tag .
```

## API Documentation

Access the OpenAPI Swagger UI when the service is running at:

http://localhost:8091/swagger-ui.html

For a more detailed look of the API Documentation check out the `Endpoints.md` file.