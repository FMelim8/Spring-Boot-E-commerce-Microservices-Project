# Game Order Service

The **Game Order Service** handles game purchases and allows users to view the status of their own orders.

## Functionality

- **Place Order**: 
  - Only regular users (non-admins) can place orders.
  - Admins are restricted from using this endpoint.
- **View Own Orders**:
  - Users can list their own orders.
  - Supports sorting by the following fields:
    - `ORDERID`, `GAMEID`, `USERID`, `ORDERDATE`, `QUANTITY`, `TOTALAMOUNT`
  - Supports query parameters:
    - `sortField`, `direction` (asc/desc), `page`, `size`
- **Search Order by ID**:
  - Users can search for an order by ID.
  - Users can only access their own orders.
  - Admins can access all orders.
- **Admin: View All Orders**:
  - Admins can view all user orders.
  - Supports the same pagination and sorting functionality as above.

## Inter-Service Communication

- **Synchronous Communication (OpenFeign)**:
  - Used to fetch read-only data like:
    - Game details
    - User details
  - Uses Eureka for service discovery, enabling the service to locate others by name.
  
- **Asynchronous Communication (Kafka)**:
  - Used when placing an order.
  - Sends a message to the `gamecatalog` service.
  - `gamecatalog` determines the order result (e.g., if the game exists or has sufficient stock).
  - Responds with success or failure accordingly.

## Resilience with Resilience4j

This service uses **Resilience4j** to prevent system overload:

- **CircuitBreaker**:
  - Detects failures and short-circuits calls to avoid making requests to failing services.
  - Helps fail fast instead of waiting on timeouts.
  
- **Bulkhead**:
  - Limits the number of concurrent calls to external services.
  - Prevents system-wide slowdowns by isolating problems.

## Docker Instructions

To build the Docker image manually:

```bash
cd gameorder
mvn install -DskipTests
docker build -t your-username/your-image:your-tag .
```

## API Documentation

Access the OpenAPI Swagger UI when the service is running at:

http://localhost:9095/swagger-ui.html

For a more detailed look of the API Documentation check out the `Endpoints.md` file.