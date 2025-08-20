# E-Commerce Platform

A modular, microservices-based e-commerce platform built with **Spring MVC**, **Jakarta EE**, **Lombok**, and **Java 21**.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Modules](#modules)
- [API Overview](#api-overview)
- [Technologies Used](#technologies-used)

---

## Project Overview

**E-Commerce Platform** is designed to support scalable e-commerce operations with customizable microservices. The platform supports user-based operations including and not limited to registering, updating, deleting profile etc. It also supports different user types like Seller or Customer. It also exposes CRUD APIs for product management.

---

## Features

- User management
- Security and Authentication using Jwt tokens
- Product and Inventory management (CRUD operations)
- Modular microservice architecture
- RESTful APIs
- Pagination for listings
- Java 21 and Jakarta EE support

---

## Modules

- **API Gateway**: Secured microservices with single entry point api gateway which also authenticates user before sending it to downstream servers.
- **User Microservice**: Handles auth and CRUD operations for different users.
- **Authentication Microservice**: Handles sign up, login, logout for users and other internal API endpoints.
- **Product Microservice**: Handles product data, inventory, and related API endpoints.
- *(Can be extended with more microservices as needed: Orders, Payments, etc.)*

---

## API Overview

### User APIs

| Method | Endpoint                                                      | Description         |
|--------|---------------------------------------------------------------|---------------------|
| POST   | `api/v1/authentication/users/login?roleType={<role type>}`    | Login user          |
| POST   | `api/v1/user/auth/logout`                                     | Logout user         |
| DELETE | `api/v1/user/profile?roleType={<role type>}`                  | Delete user profile |
| PATCH  | `api/v1/user/profile?roleType={<role type>}`                  | Update user profile |
| POST   | `api/v1/authentication/users/register?roleType={<role type>}` | Create user profile |


### Product APIs

| Method        | Endpoint                  | Description             |
|---------------|--------------------------|-------------------------|
| POST          | `/api/products`          | Create a new product    |
| GET           | `/api/products`          | List all products       |
| GET           | `/api/products/{id}`     | Get product by ID       |
| POST          | `/api/products/inventory`| Add inventory           |

---

## Technologies Used

- **Java 21**
- **Spring Boot**
- **Spring MVC**
- **Spring Security**
- **Spring Cloud Api Gateway**
- **OpenFeign**
- **Jakarta EE**
- **Lombok**
- **JUnit and Mockito** (for testing)
- **Maven** for build automation
- **Docker** for containerization
