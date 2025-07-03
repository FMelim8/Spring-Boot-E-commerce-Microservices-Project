# Spring Boot E-commerce Microservices Project

## Important Notice

This project is designed **to run inside Docker**, either via **Docker Compose** or a **Kubernetes cluster**.  
Running the services directly (uncontainerized) is **not supported or documented**, as containerization simplifies environment configuration, dependency management, and network communication through internal DNS.

---

## What’s Inside

This is a full-stack **e-commerce backend system** built with **Spring Boot** using a **microservices architecture**. It includes:

- **Keycloak Service** – Handles authentication and user-related logic (delegates authentication to Keycloak)
- **Game Catalog Service** – Manages the list of available games
- **Game Order Service** – Handles user orders and interacts with the catalog and user service
- **Eureka Server** – Provides service discovery
- **Shared Library Module** – Contains shared classes used across multiple services to avoid repetition (DRY principle)
- **PostgreSQL Database** – Each service has its own dedicated PostgreSQL database, but still using the same instance
- **Keycloak** – Used for authentication and authorization, pre-configured with a realm import for out-of-the-box use

---

## Tools & Technologies

- **Spring Boot** – Framework for building Java-based microservices
- **Spring Security** – Used for securing endpoints
- **Spring JPA** – Abstraction layer over JPA/Hibernate for ORM-based database access
- **Lombok** – Reduces boilerplate code using annotations
- **OpenFeign** – Declarative HTTP client for inter-service synchronous communication (used in Game Order Service)
- **Kafka** – Used for asynchronous communication (also in Game Order Service)
- **Swagger/OpenAPI** – API documentation with built-in JWT authentication testing
- **ELK Stack + Filebeat** – Log collection, processing and visualization
- **Grafana K6** – Load testing tools with ready-to-use testing scripts
- **Docker** – Deploy applications in lightweight containers
- **Minikube** – Local Kubernetes cluster

---

## API Documentation

Each service includes Swagger UI (via Springdoc OpenAPI) that provides:

- All endpoints and HTTP methods
- Input/output schemas
- Response codes
- JWT authentication integration
- Role-based access indicators

---

## Service Communication

- **Synchronous**: OpenFeign client (used mostly in Game Order Service)
- **Asynchronous**: Apache Kafka messaging

---

## Logging & Observability

Each main service produces structured logs that are processed through:

- **Filebeat** → **Logstash** → **Elasticsearch** → **Kibana**

This pipeline centralizes and structures log data for easy search, query, and visualization.

---

## Load Testing

This project includes **Grafana K6** scripts to simulate multiple users accessing the services and perform performance/load testing.

---

## How to Start

### 1. Clone the Repository

```bash
git clone https://github.com/my-username/my-repo.git
cd my-repo
```

### 2. Install Required Tools

You'll need the following tools installed:

For **Linux** or **Windows**:

- [Maven](https://maven.apache.org/)
- [Grafana K6](https://k6.io/open-source/)
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start)
- [kubectl](https://kubernetes.io/docs/tasks/tools/)
- [Helm](https://helm.sh/docs/intro/install/)

For **Windows**:

- [Ubuntu-WSL](https://ubuntu.com/desktop/wsl) (Optional. For running shell scripts)

---

## Building Docker Images

### Option 1: Run the `build-images.sh` script

Simply run the command

```bash
bash build-images.sh
```

### Option 2: Build from Source

If you want to make changes and build the images yourself:

1. Go into each service directory:
2. Run Maven build:

```bash
mvn clean install -DskipTests
```

3. Build Docker image:

```bash
docker build -t your-username/your-image:your-tag .
```
- Keep in mind that if you choose this option, you will need to change the images and tags for both deployments

## How to Get a Rollbar Token

To enable error reporting to Rollbar, follow these steps to obtain  your token:

1. **Create an Account on Rollbar**  
   Go to [https://rollbar.com](https://rollbar.com) and create a free account.

2. **Get Started with Java Spring**  
   After signing up, click on **"Get started"**, choose **"Java Spring"** as the platform, and click **"View setup instructions"**.

3. **Find Your Access Token**  
   Scroll down to **"Step 2: Configure Rollbar"**. Copy the token found inside the `getRollbarConfigs()` method.

4. **Configure Your Deployment**

   - **For Docker Deployment**:  
     Open the `.env` file located at the root of the Docker deployment directory. Paste your token into the `ROLLBAR_TOKEN=` key:
     
     ```
     ROLLBAR_TOKEN=your_token_here
     ```

   - **For Kubernetes Deployment**:

     - **Manual Deployment**:  
       Add your token to the `ROLLBAR_TOKEN` key inside the `service-configmap.yaml` file:
       
       ```
       ROLLBAR_TOKEN: "your_token_here"
       ```

     - **Helm Deployment**:  
       Open the Helm `values.yaml` file and scroll to the `configMap.standard.service` section. Add your token to the `ROLLBAR_TOKEN` key:
       
       ```
       configMap:
         standard:
           service:
             ROLLBAR_TOKEN: "your_token_here"
       ```

5. **Test It**  
   Try accessing a secured endpoint (e.g., one that requires JWT) without providing a valid token. This should trigger an exception, which will be sent to your Rollbar dashboard.


## Running the Stack

You can run the full system:

- **With Docker Compose**:
  - Brings up all services, Keycloak, Kafka, PostgreSQL, and ELK stack

- **With Kubernetes**:
  - Using raw YAML manifests
  - Or using Helm Charts (separated into 2 charts: one for services and one for ELK + Filebeat)

---

## Project Structure

```
/
├── deployment-docker/             # Docker Compose deployment
│   ├── docker-configs/            # Docker configurations
│   │   ├── Kafka-ui/
│   │   ├── keycloak/
│   │   ├── postgres/
│   │   └── service-props/         # Service Configurations
│   │       ├── eureka/
│   │       ├── gamecatalog/
│   │       ├── gameorder/
│   │       └── keycloak/
│   ├── elk-setup/                 # Elastic Stack setup
│   │   ├── elasticsearch/
│   │   ├── filebeat/
│   │   ├── kibana/
│   │   ├── logstash/
│   │   ├── .env
│   │   ├── docker-compose.yaml    # Docker compose for the ELK stack + Filebeat
│   │   └── setup.sh               # Setup script
│   ├── .env
│   └── docker-compose.yaml        # Docker compose for the services and dependencies
├── deployment-kubernetes/         # Kubernetes' minikube deployment
│   ├── elk/
│   │   ├── helm/                  # Helm chart for the Elastic Stack + Filebeat
│   │   │   └── elkChart/
│   │   └── manual/                # Manual deployment with Yaml files
│   │       ├── configmaps/
│   │       ├── deployments/
│   │       ├── jobs/
│   │       ├── persistentVolumeClaims/
│   │       ├── secrets/
│   │       └── serviceAccounts/
│   ├── services/
│   │   ├── helm/                  # Helm chart for the services and their dependencies
│   │   │   └── services-chart/
│   │   └── manual/                # Manual deployment with Yaml files
│   │       ├── configmaps/
│   │       ├── deployments/
│   │       ├── persistentVolumeClaims/
│   │       └── secrets/
│   └── init_all.sh                # Script for loading of Docker images to Minikube
├── eureka-server/                 # Eureka Server Service
├── gamecatalog/                   # Game Catalog Service
├── gameorder/                     # Game Order Service
├── images/                        # Docker Images for ease of use
├── K6/                            # Grafana K6 performance testing scripts
├── keycloak-service/              # Keycloak Service
├── sharedlib/                     # Shared Library Module
└── README.md                      # This file
```

---

## Contact

Created and maintained by **Filipe Melim**  
Feel free to contribute or report issues!

