# Grafana k6

**k6** is an open-source load testing tool designed to test the performance and reliability of your backend systems. It is built for developers and testers to script, execute, and analyze performance tests in JavaScript. It provides a CLI-based interface and integrates well with CI/CD pipelines. k6 is widely used to simulate user behavior under load and helps uncover bottlenecks or performance regressions in your services.


### Performance & Integration Testing

This folder contains K6 test scripts tailored for two different deployment environments:

- **Docker**: Services are exposed via ports (no ingress).
- **Minikube**: Services are exposed via an NGINX ingress controller (with path-based routing).

Each environment has its own subfolder:

---

## `Docker/`

Contains test scripts configured for direct access to services via ports.

### Scripts:

- `dockerOrderRequestScript.js`  
  Simulates real user behavior by creating game orders. It follows the actual order processing workflow used in the application.

- `dockerTestEndpointsScript.js`  
  Tests multiple endpoints accessible to a standard (non-admin) user. It checks:
  - Proper authentication via Keycloak.
  - Access to user-specific endpoints across multiple services.
  - Valid responses and authorization behavior.

### Usage Example:

```bash
k6 run Docker/dockerOrderRequestScript.js
```

```bash
k6 run Docker/dockerTestEndpointsScript.js
```

---

## `Minikube/`

Contains test scripts configured for environments using an NGINX ingress with path-based routing.

### Scripts:

- `kubeOrderRequestScript.js`  
  Simulates order creation through the ingress paths. Mimics user behavior in the order workflow.

- `kubeTestEndpointsScript.js`  
  Tests access and response validation for endpoints reachable through the NGINX ingress, with proper Keycloak authentication.

### Usage Example:

```bash
k6 run Minikube/kubeOrderRequestScript.js
```

```bash
k6 run Minikube/kubeTestEndpointsScript.js
```

---

## Notes

- Ensure Keycloak and all services are up and reachable before running tests.
- Update credentials or endpoints in the scripts if you've changed your Keycloak realm, client, or service paths.
