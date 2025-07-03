#!/bin/bash
set -e

ENV_FILE=".env"

#set or update a variable in the .env file
set_env_var() {
  local key="$1"
  local value="$2"
  if grep -q "^${key}=" "$ENV_FILE"; then
    sed -i "s/^${key}=.*/${key}=${value}/" "$ENV_FILE"
  else
    echo "${key}=${value}" >> "$ENV_FILE"
  fi
}

#load variables from .env or create the defaults ones
if [ -f "$ENV_FILE" ]; then
  source "$ENV_FILE"
else
  ELASTIC_VERSION="8.18.1"
  ELASTIC_PASSWORD="changeme"
  KIBANA_PASSWORD="kibanapassword123"
  LOGSTASH_PORT="9200"
  ELASTIC_PORT="9200"
  KIBANA_PORT="5601"
  MAIN_INDEX="microservices-reqres"
  SYSTEM_INDEX="microservices-system"

  echo "ELASTIC_VERSION=$ELASTIC_VERSION" > "$ENV_FILE"
  echo "ELASTIC_PASSWORD=$ELASTIC_PASSWORD" >> "$ENV_FILE"
  echo "KIBANA_PASSWORD=$KIBANA_PASSWORD" >> "$ENV_FILE"
  echo "ELASTIC_PORT=$ELASTIC_PORT" >> "$ENV_FILE"
  echo "LOGSTASH_PORT=$LOGSTASH_PORT" >> "$ENV_FILE"
  echo "KIBANA_PORT=$KIBANA_PORT" >> "$ENV_FILE"
  echo "MAIN_INDEX=$MAIN_INDEX" >> "$ENV_FILE"
  echo "SYSTEM_INDEX=$SYSTEM_INDEX" >> "$ENV_FILE"
fi

CERTS_DIR="./certs"
VOLUME_NAME="elastic_certs"

echo "Checking certs folder"
mkdir -p "$CERTS_DIR"

#create instances.yml if it doesn't exist
if [ ! -f "$CERTS_DIR/instances.yml" ]; then
  echo "creating the instances.yml file"
  cat > "$CERTS_DIR/instances.yml" <<EOL
instances:
  - name: elasticsearch
    dns:
      - elasticsearch
    ip:
      - 127.0.0.1
  - name: kibana
    dns:
      - kibana
    ip:
      - 127.0.0.1
  - name: logstash
    dns:
      - logstash
    ip:
      - 127.0.0.1
  - name: filebeat
    dns:
      - filebeat
    ip:
      - 127.0.0.1
EOL
  chmod -R 777 certs
fi

#generate the certificates if they're missing
if [ ! -f "$CERTS_DIR/ca/ca.crt" ]; then
  echo "creating the certs using a temporary container"

  MSYS_NO_PATHCONV=1 docker run --rm \
    -v "$(pwd)/certs:/usr/share/elasticsearch/config/certs" \
    docker.elastic.co/elasticsearch/elasticsearch:$ELASTIC_VERSION \
    bash -c "bin/elasticsearch-certutil ca --silent --pem --out config/certs/ca.zip && unzip config/certs/ca.zip -d config/certs && rm config/certs/ca.zip"

  MSYS_NO_PATHCONV=1 docker run --rm \
    -v "$(pwd)/certs:/usr/share/elasticsearch/config/certs" \
    docker.elastic.co/elasticsearch/elasticsearch:$ELASTIC_VERSION \
    bash -c "bin/elasticsearch-certutil cert --silent --pem --in config/certs/instances.yml --ca-cert config/certs/ca/ca.crt --ca-key config/certs/ca/ca.key --out config/certs/bundle.zip && unzip config/certs/bundle.zip -d config/certs && rm config/certs/bundle.zip"

  echo "certificates created in $CERTS_DIR"
else
  echo "certificates already exist, skipping creation."
fi

#copy the certs into a docker volume
echo "copying certs into docker volume"
docker volume inspect $VOLUME_NAME >/dev/null 2>&1 || docker volume create $VOLUME_NAME
MSYS_NO_PATHCONV=1 docker run --rm \
  -v "$(pwd)/certs":/tmp/certs:ro \
  -v ${VOLUME_NAME}:/usr/share/elasticsearch/config/certs \
  alpine sh -c "cp -r /tmp/certs/* /usr/share/elasticsearch/config/certs/"

#start only the Elasticsearch instance from the docker-compose
echo "starting Elasticsearch container for setup"
docker-compose up -d --quiet-pull elasticsearch

echo "waiting for Elasticsearch to be ready"
timeout=160
until docker exec elasticsearch curl -k -s -u elastic:$ELASTIC_PASSWORD https://localhost:9200 | grep -q '"cluster_name"'; do
  timeout=$((timeout - 1))
  if [ $timeout -le 0 ]; then
    echo "Elasticsearch didn't start in time, try again later."
    docker-compose down -v
    exit 1
  fi
  sleep 1
done

echo "elasticsearch is up"

#set passwords
echo "setting user passwords"
docker exec elasticsearch curl -k -s -o /dev/null -X POST "https://localhost:9200/_security/user/elastic/_password" \
  -H "Content-Type: application/json" \
  -u elastic:elastic \
  -d "{\"password\":\"$ELASTIC_PASSWORD\"}"

docker exec elasticsearch curl -k -s -o /dev/null -X POST "https://localhost:9200/_security/user/kibana_system/_password" \
  -H "Content-Type: application/json" \
  -u elastic:$ELASTIC_PASSWORD \
  -d "{\"password\":\"$KIBANA_PASSWORD\"}"

#create the service account token
echo "creating the Kibana service account token"
TOKEN_OUTPUT=$(MSYS_NO_PATHCONV=1 docker exec elasticsearch /usr/share/elasticsearch/bin/elasticsearch-service-tokens create elastic/kibana kibana-token) 
SERVICE_TOKEN=$(echo "$TOKEN_OUTPUT" | grep -oP '(?<=kibana-token = )\S+')
if [ -z "$SERVICE_TOKEN" ]; then
  echo "failed to extract the Kibana service token, the output was:"
  echo "$TOKEN_OUTPUT"
  docker-compose down -v
  exit 1
fi

#generate keys
KEY1=$(od  -vN 32 -An -tx1 /dev/urandom | tr -d " \n")
KEY2=$(od  -vN 32 -An -tx1 /dev/urandom | tr -d " \n")
KEY3=$(od  -vN 32 -An -tx1 /dev/urandom | tr -d " \n")

#save token and keys
echo "saving the service token to $ENV_FILE"
set_env_var "ELASTICSEARCH_SERVICE_TOKEN" "$SERVICE_TOKEN"

if ! grep -q "KIBANA_SECURITY_ENCRYPTION_KEY" "$ENV_FILE"; then
  set_env_var "KIBANA_SECURITY_ENCRYPTION_KEY" "$KEY1"
  set_env_var "KIBANA_REPORTING_ENCRYPTION_KEY" "$KEY2"
  set_env_var "KIBANA_SAVEDOBJECTS_ENCRYPTION_KEY" "$KEY3"
fi

#create the indices with 0 replicas
echo "creating the indices"
curl -s -o /dev/null -X PUT https://localhost:9200/$MAIN_INDEX \
  --insecure -u elastic:$ELASTIC_PASSWORD \
  -H 'Content-Type: application/json' \
  -d '{
    "settings": {
      "number_of_replicas": 0
    }
  }'

curl -s -o /dev/null -X PUT https://localhost:9200/$SYSTEM_INDEX \
  --insecure -u elastic:$ELASTIC_PASSWORD \
  -H 'Content-Type: application/json' \
  -d '{
    "settings": {
      "number_of_replicas": 0
    }
  }'

#reload env
source "$ENV_FILE"

echo "starting the full Elastic Stack"
chmod -R 777 ../logs
docker-compose up -d --quiet-pull

echo "setup complete"
