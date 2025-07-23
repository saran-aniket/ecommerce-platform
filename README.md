# ecommerce-platform
Simple e-commerce Api services

# Fake Store Microservice

**Fake Store Microservice** is a scalable microservice built with Spring Boot, designed to handle store-related operations such as product management. The service offers both RESTful APIs and gRPC-based communication for efficient internal and external interactions. It can run locally or within a containerized environment using Docker for easy deployment.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Usage](#usage)

---

## Overview

This microservice acts as a backend service for managing fake store products and operations. It provides a REST API for client applications and a gRPC interface for seamless service-to-service communication. Leveraging modern Java and Spring Boot, it ensures high performance, scalability, and maintainability.

---

## Features

- **RESTful API**: Exposes endpoints for managing products and store operations.
- **gRPC Integration**: Offers high-performance communication for internal microservices.
- **Docker Support**: Fully containerized for easy deployment.
- **Protocol Buffers**: Used for efficient data serialization across services.
- **Spring Boot Development**: Built on top of Spring Boot for rapid development and production readiness.

---

## Architecture

The Fake Store Microservice is designed with a modular architecture to separate key responsibilities:

### REST API Service
- Handles incoming client requests over HTTP.
- Endpoints are accessible at `http://localhost:8080/api`.
- Example Endpoint: `GET /api/v1/products/{productId}` retrieves product details.

### gRPC Service
- Provides high-performance RPC communication using gRPC.
- Runs on the default gRPC server port (`6565`).
- Designed to interact with related microservices in the ecosystem.

---

## Tech Stack

- **Languages**: Java 21
- **Frameworks**:
    - Spring Boot (Web, gRPC, DevTools)
- **Communication**:
    - REST API (via Spring Boot Starter Web) and gRPC.
- **Data Serialization**:
    - Protocol Buffers (Protobuf).
- **Build Tool**: Maven
- **Containerization**: Docker.

## Usage

### REST API
1. Use tools like Postman, `curl`, or a frontend application to interact with the API.
2. Example Endpoint:
   ```http
   GET http://localhost:8080/api/v1/products/{productId}
   ```

### gRPC Service
1. Generate client stubs using the Protobuf file definitions.
2. Configure gRPC clients to call methods on the gRPC server running on `localhost:6565`.

