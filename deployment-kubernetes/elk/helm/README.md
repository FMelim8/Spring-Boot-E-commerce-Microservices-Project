# elk-chart

This Helm chart deploys an Elastic Stack (ELK) environment into a Kubernetes cluster. It includes services like Elasticsearch, Logstash, Kibana and Filebeat. It is highly customizable and supports advanced deployment techniques using Helm hooks, jobs, role-based access control, and templated configuration.

---

## What is a Helm Hook?

A Helm hook is a way to attach operations to specific events in the Helm lifecycle. For example, you can execute a Kubernetes Job **before** or **after** a chart is installed, upgraded, or deleted. Hooks are useful for tasks like initializing data, setting up configuration, generating secrets, or cleaning up resources.

Hooks can have weights (to determine execution order) and deletion policies (to clean up after execution).

---

## Templates

This chart includes the following template files:

- **`accounts.yaml`**
- **`configMapsFile.yaml`**
- **`configMapsStandard.yaml`**
- **`deployments.yaml`**
- **`ingress.yaml`**
- **`jobs.yaml`**
- **`pvcs.yaml`**
- **`roles.yaml`**
- **`secrets.yaml`**
- **`services.yaml`**

### Why two different configMap templates?

There are two types of ConfigMaps:

- **Standard ConfigMaps (`configMapsStandard.yaml`)**: These are used for simple key-value pairs, often injected as environment variables into a deployment.
- **File-based ConfigMaps (`configMapsFile.yaml`)**: These contain configuration files that will be mounted into the containers.

This separation helps keep configuration clean and purpose-specific.

---

## `values.yaml` Structure

The `values.yaml` file is organized to define all parts of the stack and related Kubernetes resources.

### `stack`

Each service (e.g., `elasticsearch`, `kibana`, etc.) is configured under the `stack` key:

- `image`: Container image
- `tag`: Image version
- `port`: Port the container listens on
- `nodePort` *(optional)*: Static NodePort for external access
- `replicas`: Number of replicas to deploy
- `args` *(optional)*: Arguments passed to the container entrypoint
- `initContainers` *(optional)*: List of init containers with the following properties:
  - `image`: Image to run
  - `volumeMounts` *(optional)*: List of volumes, with `name` and `mountPath`
  - `command` *(optional)*: Command to execute
- `resources` *(optional)*: Resource requirements
- `env` *(optional)*: Additional environment variables
- `configMaps` *(optional)*: Names of required ConfigMaps
- `secrets` *(optional)*: Names of required Secrets
- `volumeMounts` *(optional)*: Mount paths and optional `subPath`
- `volumes` *(optional)*: Required volume definitions

### `versions`

A shared `versions.stack` section ensures all components (e.g., Elasticsearch, Kibana, etc) run the same version.

### `roles`

Defines Kubernetes RBAC roles:

```yaml
roles:
  elastic-role:
    rules:
      - apiGroups: [""]
        resources: ["pods"]
        verbs: ["get", "watch", "list"]
    apiGroup: rbac.authorization.k8s.io
```

- `elastic-role`: Name of the role
- `apiGroups`: "" indicates the core API group
- `resources`: Resources to grant access to
- `verbs`: Permissions to grant your role 

### `accounts`

Defines service accounts and their associated roles:

```yaml
accounts:
  elk-service-account:
    roles: 
      - "elastic-role"
```

- `elk-service-account`: Name of the account
- `roles`: List of the roles for that account

### `volumes`

Persistent volume claims configuration:

```yaml
volumes:
  elastic-certs:
    keep: true
    hook:
      type: pre-install
      weight: -6
    size: 500Mi
```

- `elastic-certs`: Name of the volume
- `keep`: Whether this volume is kept after a chart uninstall
- `type`: Hook type (e.g., `pre-install`, `post-install`)
- `weight`: Determines the execution order
- `size`: size of the volume

### `configMap`

Two types of config maps can be defined:

#### File-based ConfigMaps

```yaml
configMap:
  file:
    logstash-pipeline:
      pipeline.conf: |
        input { beats { port => 5044 } }
        output { elasticsearch { hosts => ["http://elasticsearch:9200"] }
```

- `logstash-pipeline`: Name of the config map (Will be changed to `logstash-pipeline-config` after templating)
- `pipeline.conf`: File name and type
- Everything after the Pipe `|`: Contents of the file

#### Standard ConfigMaps

```yaml
configMap:
  standard:
    index:
      MAIN_INDEX: "main-logs"
      SYSTEM_INDEX: "system-logs"
```

- `index`: Name of the config map (Will be changed to `index-config` after templating)
- Everything after the **Config Name**: Key-value pairs

### `secrets`

Same structure as standard config maps, but values are automatically base64-encoded by the template:

```yaml
secret:
  elastic-credentials:
    ELASTIC_PASSWORD: elastic
```
- `elastic-credentials`: Name of the secret (Will be changed to `elastic-credentials-secret` after templating)
- Everything after the **Secret Name**: Key-value pairs

### `jobs`

Defines one-time jobs to run during chart lifecycle. Useful for setup tasks like token generation, index initialization, etc.

```yaml
jobs:
  generate-certs:
    image: docker.elastic.co/elasticsearch/elasticsearch
    tag: 8.18.1
    hook:
      type: pre-install
      weight: -5
      delete: hook-succeeded
    volumeMounts:
      certs: /certs
    volumes:
      certs: elastic-certs
    command: 
      - bash
      - -c
      - |
        echo "This is a command"
```

---

## Summary

This chart enables a robust and modular setup for deploying the ELK stack using Helm. It separates configuration into clear logical groups, supports Helm hooks for advanced workflows, and encourages reusable and composable resource definitions through its templated system.
