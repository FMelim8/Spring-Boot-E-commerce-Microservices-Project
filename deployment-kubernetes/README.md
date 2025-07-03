# Kubernetes Deployment Guide

## What is Kubernetes?

Kubernetes is an open-source container orchestration platform designed to automate the deployment, scaling, and management of containerized applications. It provides a framework to run distributed systems resiliently and allows developers to focus on building applications without worrying about the underlying infrastructure.

### Advantages:
- Automated container scheduling, scaling, and management
- Self-healing capabilities (e.g., restarts failed containers)
- Service discovery and load balancing
- Rolling updates and rollbacks
- Declarative configuration using YAML

### Disadvantages:
- Steep learning curve
- Requires configuration and setup overhead
- Can be overkill for small/simple applications

## What is Minikube?

Minikube is a lightweight tool that runs a single-node Kubernetes cluster locally, ideal for development and testing. It provides a local environment where you can simulate a real Kubernetes cluster on your machine.

## What is Helm?

Helm is a package manager for Kubernetes. It simplifies the deployment and management of applications through reusable and configurable templates known as charts.

### How Helm Works:
- A Helm *chart* is a collection of YAML templates and a `values.yaml` file.
- You install a chart using the `helm install` command.
- Helm fills in templates using the values from `values.yaml`.
- You can override values during installation or define your own configurations.

### Advantages:
- Simplified Deployments
- Reusable Templates
- Centralized Chart Management

### Disadvantages:
- Steep learning curve
- Complexity with Customization
- Limitations with Dynamic Deployments

> **Note:** This guide assumes you have Minikube, `kubectl`, and Helm already installed.

---

## Project Structure

The Kubernetes deployment is divided into two parts:
1. **Services and Dependencies**
2. **ELK Stack + Filebeat**

Each part can be deployed:
- **Manually** via YAML files
- **Automatically** using **Helm charts** (recommended)

### `init.sh`

A helper script `init.sh` is provided to solve a common issue where Minikube’s internal Docker daemon cannot access your local Docker images. The script:
- Pulls the necessary images into your local Docker
- Loads them into the Minikube cluster

---

## Getting Started

From the root of the project, run:

```
cd deployment-kubernetes
minikube start --driver=docker --cpus=2 --memory=8192 --ports=127.0.0.1:443:443 --ports=127.0.0.1:80:80
```

> **Linux users with root privileges:**  
Use `minikube start --driver=docker --cpus=2 --memory=8192 --force --ports=127.0.0.1:443:443 --ports=127.0.0.1:80:80` instead.

Then run:

```
bash init.sh
```

> **On Windows:**  
Use WSL or GitBash and navigate to the folder with:
```
cd /mnt/full-path-to-project-folder/deployment-kubernetes
bash init.sh
```

If you have custom image names or tags, update them inside `init.sh`.

> The script pulls and loads all the necessary images, so it usually takes a while.

### Optional: Launch the Dashboard

The Minikube dashboard is useful for visualization and debugging:

```
minikube dashboard
```

This opens a browser where you can monitor pod status, logs, and errors more easily.

---

## Deployment Options

>For **both** options, it is **necessary** that you deploy the services first, and only then the Elastic Stack.

There are two ways to deploy:

### 1. **Manual Deployment (YAML files) (Not Recommended)**

#### Step 1: Services and Dependencies

```
cd services/manual
```

Apply the files of these folders in this order:

- `configMaps/`
- `persistentVolumeClaims/`
- `secrets/`
- `ingress/`
- Finally, `deployments/`

In each folder:

```
cd folder-name

# For each file in that folder 
kubectl apply -f file.yaml 

cd ..
```

Return to the base folder with:

```
cd ../..
```

#### Step 2: ELK + Filebeat

```
cd elk/manual
```

Apply in this order:

- `configMaps/`
- `persistentVolumeClaims/`
- `secrets/`
- `serviceAccounts/`
- `ingress/`

Then:

```
cd jobs
kubectl apply -f generate-certs-job.yaml
cd ../deployments
kubectl apply -f elasticsearch-deployment.yaml
cd ../jobs
kubectl apply -f generate-token-job.yaml
cd ../deployments
```
And finally, apply the rest of the files in the `deployments` folder:
```
# For the rest of the files

kubectl apply -f file.yaml
```

> **Note:** Secrets must be base64-encoded. If you change them, encode the values manually.

#### Cleanup Tip:
Manual cleanup can be messy. A full reset is often easier:

```
minikube delete
minikube start --driver=docker --ports=127.0.0.1:443:443 --ports=127.0.0.1:80:80
```

As you can see, manual deployment is verbose and **error-prone**. Helm is a much better alternative for most use cases.

---

### 2. **Helm Deployment (Recommended)**

#### Important Notes:
- The main config file is `values.yaml` in each Helm chart.
- To change PostgreSQL/Keycloak credentials in the **services** chart:
  - `secret.database.DATABASE_USERNAME`
  - `secret.database.DATABASE_PASSWORD`
  - `configMap.standard.keycloak.KEYCLOAK_ADMIN`
  - `configMap.standard.keycloak.KEYCLOAK_ADMIN_PASSWORD`
- To change the `elastic` user password in the **ELK** chart:
  - `secret.elasticsearch.ELASTIC_PASSWORD`

Additional info is available in a `README.md` file besides each chart’s folder.

#### Deploy Services:

```
cd services/helm
helm install services-chart services-chart/
cd ../..
```

#### Deploy ELK:

```
cd elk/helm
helm install elk-chart elkChart/
```

Wait for the jobs to complete. Everything should be running correctly.

## Accessing your applications

Now that your applications are deployed on minikube, accessing them won't be as easy as to just open up a localhost with their port. To solve this, each deployment has it's own Ingress that uses `Nginx` as a reverse proxy.

For security purposes, not all applications are exposed this way, only the main ones are accessible through an **`Ingress`**. Only the `Game Catalog Service`, `Game Order Service`, `Keycloak Service` `(HTTP)` and `Kibana` `(HTTPS)` have been configured in the Ingress. To access the additional resources of the deployment take a look at the `port-forwarding` option.

> Note: for the services to work, there was an additional setting added to their configs to define their `Base Path`

If you don't know each deployment's service's name and port, you can list them with the command:

```
kubectl get service
```

After getting the service's name and port, do:

```
kubectl port-forward service/your-service-name services-port:target-port
```

After that, you'll be able to access your aplication with localhost and the target-port you chose, for example `http://localhost:9094`.

>Keep in mind that to port-forward, you will need that terminal to stay open. If you want to run any other commands, open another one or stop the port-forward

### Examples:

  - `Game Catalog Service`:
    - Port-Forward: http://localhost:9094/swagger-ui/index.html
    - Ingress: http://localhost/gamecatalog/swagger-ui/index.html
  - `Game Order Service`
    - Port-Forward: http://localhost:9095/swagger-ui/index.html
    - Ingress: http://localhost/gameorder/swagger-ui/index.html
  - `Keycloak Service`
    - Port-Forward: http://localhost:8091/swagger-ui/index.html
    - Ingress: http://localhost/keycloakservice/swagger-ui/index.html
  - `Kibana`
    - Port-Forward: https://localhost:5601
    - Ingress: https://localhost/kibana

> Note: In case you get stuck in a loop where you can't get passed the login page in Kibana, clear your localhost cookies. Open `Dev-tools` in your browser, go to the `Application` section. In the `storage` sub-section, go to `cookies`, then `localhost` and click `clear all cookies`.

## Default values

 - **DATABASE_USERNAME** (PostgreSQL user's username): admin
 - **DATABASE_PASSWORD** (PostgreSQL user's password): changeme
 - **KEYCLOAK_ADMIN** (Keycloak admin's username): admin
 - **KEYCLOAK_ADMIN_PASSWORD** (Keycloak admin's password): admin
 - **ELASTIC_PASSWORD** (Kibana's user `elastic`'s password): elastic

### Cleanup:

```
helm uninstall service-chart
helm uninstall elk-chart
```