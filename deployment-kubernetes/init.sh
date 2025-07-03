#!/bin/bash

images=(
  postgres:15.11-alpine
  quay.io/keycloak/keycloak:23.0.6
  bitnami/kafka:4.0.0
  docker.io/kubernetesui/dashboard:v2.7.0
  docker.io/kubernetesui/metrics-scraper:v1.0.8
  docker.elastic.co/elasticsearch/elasticsearch:8.18.1
  docker.elastic.co/kibana/kibana:8.18.1
  docker.elastic.co/logstash/logstash:8.18.1
  docker.elastic.co/beats/filebeat:8.18.1
  alpine/k8s:1.33.1
  registry.k8s.io/ingress-nginx/controller:v1.10.1
  registry.k8s.io/ingress-nginx/kube-webhook-certgen:v1.4.1
)

selfimages=(
  filipem/gamecatalog-service:1.2.0
  filipem/eureka-service:1.1.0
  filipem/keycloak-service:1.2.0
  filipem/gameorder-service:1.2.0
)

for img in "${images[@]}"; do
  echo "pulling $img"
  docker pull "$img"

  echo "loading $img into Minikube"
  minikube image load "$img"
done

for img in "${selfimages[@]}"; do
  echo "loading $img into Minikube"
  minikube image load "$img"
done

echo "enabling ingress"
minikube addons enable ingress

echo "done"
