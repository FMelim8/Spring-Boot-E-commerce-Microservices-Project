# services-chart

This Helm chart is responsible for deploying various application services into a Kubernetes cluster. It is designed to be flexible and configurable, allowing the definition of environment variables, configuration files, secrets, volume mounts, and more through `values.yaml`.

## Templates

This chart includes the following templates:

- **`configMapsFile.yaml`**
- **`configMapsStandard.yaml`**
- **`configMapsTpl.yaml`**
- **`deployments.yaml`**
- **`ingress.yaml`**
- **`pvcs.yaml`**
- **`secrets.yaml`**
- **`services.yaml`**

### Why three different configMap templates?

Not all ConfigMaps are used in the same way, so they are separated into three different templates:

- **Standard ConfigMaps (`configMapsStandard.yaml`)**: These are used for simple key-value pairs, often injected as environment variables into a deployment.
- **File-based ConfigMaps (`configMapsFile.yaml`)**: These contain configuration files that will be mounted into the containers.
- **Templated ConfigMaps (`configMapsTpl.yaml`)**: These are intended for large or complex configuration files (e.g., Keycloak realm import files). Including such data directly in `values.yaml` would significantly hurt readability and bloat the file. Instead, they pull the content from a `.tpl` file — in this case, `_helpers.tpl`.

> Files starting with an underscore (e.g., `_helpers.tpl`) are assumed to not contain any Kubernetes manifests. They will not be rendered into Kubernetes objects but can be used as templates and helpers.

---

## `values.yaml` Structure

The `values.yaml` is structured to allow high flexibility in configuring services and their associated resources.

### `services`

Each service is defined inside the `services` block and includes the following fields:

- `image`: The container image to use
- `tag`: The image tag to pull
- `port`: The container port to expose
- `nodePort` *(optional)*: Static NodePort if the service needs to be externally accessible
- `resources` *(optional)*: resources if they require a different ammount of resources than the default
- `replicas`: Number of replicas for the deployment
- `initContainers` *(optional)*: configurations for an init container for various purposes *(not used in this chart)*
- `env` *(optional)*: Additional environment variables (as **key-value pairs**)
- `config` *(optional)*: Names of config maps required by the service
- `secrets`: Names of secrets required by the service
- `volumeMounts` *(optional)*: List of volume mount paths with optional `subPath`
- `volumes` *(optional)*: Volume definitions required for the service

### `volumes`

Persistent volume claims configuration:

```yaml
volumes:
  logs:
    keep: true
    hook:
      type: pre-install
      weight: -6
    size: 500Mi
```

- `logss`: Name of the volume
- `keep`: Whether this volume is kept after a chart uninstall
- `type`: Hook type (e.g., `pre-install`, `post-install`)
- `weight`: Determines the execution order
- `size`: size of the volume

### `configMap`

Three types of ConfigMaps can be defined:

#### `tpl`

Used for large configurations pulled from a template file.

```yaml
configMap:
  tpl:
    keycloak-import:
      type: json
      template: keycloakImportFile
```

In this example:
- `keycloak-import`: Name of the config map (Will be changed to `keycloak-import-config` after templating)
- `template`: Name of the template defined in `_helpers.tpl` (Example: "services.keycloakImportFile")
- `type`: Optional file extension (e.g., `json`, `yaml`, `conf`)

#### `file`

Used for defining inline configuration files.

```yaml
configMap:
  file:
    postgres:
      init.sql: |
        CREATE DATABASE games;
        CREATE DATABASE orders;
        CREATE DATABASE users;
        CREATE DATABASE keycloak;
```

- `postgres`: Name of the config map (Will be changed to `postgres-config` after templating)
- `init.sql`: File name and type
- Everything after the Pipe `|`: Contents of the file

#### `standard`

Used for simple key-value config maps.

```yaml
configMap:
  standard:
    service:
      DB_HOST: localhost
      DB_PORT: 5432
```

- `service`: Name of the config map (Will be changed to `service-config` after templating)
- Everything after the **Config Name**: Key-value pairs

### `secret`

Secrets follow the same structure as `standard` config maps, but their values do **not need to be base64-encoded** — the template will handle encoding automatically.

```yaml
secret:
  database:
    USERNAME: admin
    PASSWORD: s3cr3t
```

- `database`: Name of the secret (Will be changed to `database-secret` after templating)
- Everything after the **Secret Name**: Key-value pairs

---

## Notes

- Services can be configured individually within the `values.yaml`.
- Different types of ConfigMaps allow flexibility in managing configurations of varying complexity.
- Secrets are safely encoded inside the template, simplifying their declaration.
- `_helpers.tpl` is a powerful place to define large, reusable content like JSON imports for applications like Keycloak.

This structure allows you to scale your application services cleanly while keeping configurations modular and readable.