# ACT 6.1 - Kafka + REST API

Proyecto realizado con Spring Boot, Apache Kafka y PostgreSQL.

## Descripción

La aplicación está dividida en dos componentes:

- **str-producer**
  - Envía mensajes a un topic de Kafka mediante un endpoint REST.

- **str-consumer**
  - Escucha los mensajes de Kafka.
  - Guarda los mensajes en PostgreSQL.
  - Expone un endpoint REST para consultar los mensajes almacenados.

## Tecnologías utilizadas

- Java
- Spring Boot
- Apache Kafka
- PostgreSQL
- Docker Compose
- Maven

## Servicios Docker

El proyecto utiliza Docker Compose para levantar:

- Kafka
- Zookeeper
- PostgreSQL
- Kafdrop

## Ejecución

### 1. Levantar contenedores

```bash
docker compose up -d
2. Ejecutar aplicaciones

Ejecutar:

StrProducerApplication
StrConsumerApplication
Endpoints
Enviar mensaje
POST http://localhost:8000/producer

Ejemplo body:

"Hola Kafka"
Consultar mensajes
GET http://localhost:8200/messages
Resultado

Los mensajes enviados al producer se publican en Kafka, son consumidos automáticamente y almacenados en PostgreSQL para posteriormente poder consultarlos mediante la API REST.
