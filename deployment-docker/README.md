# Docker Deployment

This folder contains all necessary files to deploy the entire project using Docker.

## Structure

- **`docker-compose.yml`** in the `deployment-docker` folder: Contains the definitions for all the Spring Boot application services and their dependencies (PostgreSQL, Keycloak, etc).
- **`docker-compose.yml`** in the `elk-setup` folder: Contains the ELK stack (Elasticsearch, Logstash, Kibana) and Filebeat. **You should not use this file directly**.

Instead, **a setup script is provided** to configure and launch the ELK stack properly.

- The configurations for the Spring boot services, for PostgreSQL and Keycloak are in the `docker-configs` folder.
- The configurations for the Elastic stack + Filebeat are present in the `elk-setup` folder. 

## ELK Setup Script

The `elk-setup/setup.sh` script performs the following actions:

1. Creates a dummy Elasticsearch container to generate self-signed certificates.
2. Uses those certificates for secure communication between ELK components.
3. Starts the real Elasticsearch container (with security enabled).
4. Sets up:
   - Indexes with `0` replicas.
   - System passwords (elastic, kibana-system).
   - A service account token for Kibana to connect securely.
5. Brings up the remaining ELK stack.

## .env Files

There are **two** sets of `.env` files:

### 1. `deployment-docker/.env`

Used for:
- PostgreSQL user and password.
- Rollbar token (optional).

**You are encouraged to change both values.**

### 2. `deployment-docker/elk-setup/.env`

Used for configuring:
- Ports for Elasticsearch, Logstash, Kibana, Filebeat.
- System user passwords (elastic, kibana-system).
- Index names.

**Most important: change the passwords to secure your setup.**

## How to Deploy

The deployment

The Docker deployment process consists of two steps:

### Step 1: Launch Services

Assuming you are at the root of the project and have **already built the Docker images**:

> If you built the service images yourself and used custom names/tags, you must edit the service's `docker-compose.yml` accordingly.

```bash
cd deployment-docker
docker compose up -d
```

This will bring up the application services and their dependencies.

### Step 2: Setup ELK Stack

```bash
cd elk-setup
bash setup.sh
```

### Windows Users

- If you are using **Windows Subsystem for Linux (WSL)**, open a WSL terminal and navigate to your project folder:

```bash
cd /mnt/full-path-to-project-folder/deployment-docker/elk-setup
bash setup.sh
```

- Alternatively, you can use **GitBash** to execute the script.

## Accessing Services

After setup:

- Each service will be available on `localhost` using the defined ports: `http://localhost:<service_port>`
- To access **Kibana**, open your browser and go to: `http://localhost:<kibana_port>` (default: 5601).
- Login with:
  - **Username**: `elastic`
  - **Password**: (from `.env` in `elk-setup` folder)

## Default Ports

- **Logstash:** 5044
- **Postgres:** 5432
- **Kibana:** 5601
- **Keycloak:** 8080
- **Keycloak-service:** 8091
- **Eureka-server:** 8761
- **Gamecatalog-service:** 9094
- **Gameorder-service:** 9095
- **Kafka:** 9092
- **Elasticsearch:** 9200

## Additional Commands

You may want to stop/start or even remove the containers.

- To stop the containers of a specific docker-compose do `docker compose stop`
- To start the containers of a specific docker-compose do `docker compose start`
- To remove the containers of a specific docker-compose do `docker compose down`
- To clean up everything, including volumes of a specific docker-compose do `docker compose down -v`

>If you remove the containers from the Elastic Stack deployment and want to start them up again, you'll need to run the setup script again.