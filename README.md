# Java Assessment API

## Overview

This Java API provides functionalities related to tasks and users. It allows for operations such as scheduling tasks, listing users, and retrieving tasks. The API is built with Spring Boot and integrates with an in-memory H2 database. For ease of deployment and scaling, it can be containerized using Docker.

## Prerequisites

- Java 17 or later
- Maven
- Docker (for containerization)

## Building and Running the Project

### Building with Maven

1. Clone the repository:
git clone <repository-url>

2. Navigate to the project directory:
cd <project-directory>


3. Build the project using Maven:
mvn clean install

4. Run the application:
mvn spring-boot:run


### Containerizing with Docker

1. Build a Docker image:
docker build -t java-assessment-api .

2. Run the application using Docker:
docker run -p 8080:8080 java-assessment-api


## Using the API

The API is self-documented using Swagger UI. Once the application is running, open your browser and navigate to:
http://localhost:8080/swagger-ui/index.html


From here, you can see all the available endpoints, their descriptions, and even test them directly from the browser. You can schedule tasks, list users, retrieve tasks, and more.

Endpoints such as `/users` and `/tasks` support pagination and sorting. By default, they return 50 records per page, but this can be adjusted by passing the `page`, `size`, and `sort` parameters.

## Running Tests

To ensure the integrity and functionality of the API, it's important to run tests before deployment or after making changes.

1. Navigate to the project directory (if you're not already there):
cd <project-directory>


2. Run the tests using Maven:
**mvn test**
This will execute all unit and integration tests. Upon completion, Maven will provide a summary of passed and failed tests. It's recommended to address any failed tests before moving on to other operations or deployments.


## Troubleshooting

If you encounter any issues while setting up or running the project, check the application logs for detailed error messages. Make sure that all prerequisites are correctly installed and that no other applications are running on the same port (default is 8080).


