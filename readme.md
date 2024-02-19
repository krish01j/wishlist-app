# Project Setup and Configuration Guide

This guide provides instructions on how to set up and configure the project locally, including running it with an H2 database for development purposes.

## Prerequisites

- Java JDK 17 or higher
- Maven 3.6.3 or higher

## Setup Instructions

### Clone the Repository

First, clone the project repository to your local machine using the following command:

```bash
git clone <repository-url>
```

## Configure Application for H2 Database
The application is configured to use an H2 in-memory database for simplicity in development and testing:

Navigate to src/main/resources/.
Open or create application.properties.
Configure the application to use H2 by adding or updating the following properties:

```bash
spring.h2.console.enabled=true
spring.h2.console.path=/h2-ui
 
spring.datasource.url=jdbc:h2:file:./testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
 
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

wishlist.app.jwtSecret= ======================wishlist===========================
wishlist.app.jwtExpirationMs=86400000
```

## Build the Project
Build the project and install dependencies using Maven:
```agsl
mvn clean install
```

## Run the Application
Start the application with:
```agsl
mvn spring-boot:run
```

The application will be accessible at http://localhost:8080. 
The H2 console can be accessed at http://localhost:8080/h2-ui 
using the JDBC URL, username, and password configured in application.properties.


## Testing Instructions
To run unit and integration tests, use the following Maven command:
```agsl
mvn test
```
These tests will interact with the H2 database, following the configuration specified in application.properties.

## Document Assumptions
##### Database Persistence:
Data is temporary and stored in memory. Data will be lost upon application restart.
##### Environment: 
The H2 database is intended for development and testing. Consider a persistent database solution for production.

##### Postman Collection for APIs:
https://www.postman.com/joint-operations-cosmologist-9188039/workspace/movieradar/collection/30177890-7e841c91-9e1d-4fd9-afd6-cdfa39c726ec?action=share&creator=30177890

Order in which the APIs need to called/used.

1. POST  /auth/api/signup
2. POST  /auth/api/signin
3. POST  /api/wishlists
4. GET   /api/wishlists
5. DELETE /api/wishlists/{id}


On calling sign in API, the response would contain a token, which is the authentication/authorization token.
This token needs to be used in the Authorization header of all wishlists APIs.

```bash
--header 'Authorization: Bearer ${AUTH_TOKEN}'
```

Sample CURL for GET wishlist requests.

```bash
curl --location 'http://localhost:8080/api/wishlists' \
--header 'Authorization: Bearer {{AUTH_TOKEN}}' \
```