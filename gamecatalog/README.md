# Game Catalog Service

The **Game Catalog Service** is responsible for managing and providing access to game data. It supports both public access for browsing and restricted access for managing games.

## Functionality

- **Public Access**: Unauthenticated users can browse and view games.
- **Admin Access**: Users with an admin role (determined via the JWT from the Keycloak service) can:
  - Add new games
  - Update existing games
  - Soft delete games (they are excluded from listings but remain in the database)

## Kafka Integration

- Communicates asynchronously with the `gameorder` service using **Kafka**.
- Listens for a request's details and processes said order.
- Sends a message to `gameorder` with the process' outcome (Success/Game doesn't exist/Stock too low).

## Game Listing and Pagination

- Users can list all available games.
- Optional query parameters:
  - `sortField`: Field to sort by (e.g., name, price)
  - `direction`: Ascending or descending
  - `page`: Page number
  - `size`: Page size
- Defaults are provided if parameters are not set.

## Database Seeding

- Includes a seeder that populates the database with predefined games if no games are found on startup.

## Docker Instructions

To build the Docker image manually:

```bash
cd gamecatalog
mvn install -DskipTests
docker build -t your-username/your-image:your-tag .
```

## API Documentation

Access the OpenAPI Swagger UI when the service is running at:

http://localhost:9094/swagger-ui.html

For a more detailed look of the API Documentation check out the `Endpoints.md` file.