# Demo Departments Application

A Spring Boot application for managing departments, employees, and related data.

## Features

- Person management with various information levels
- Address management with support for multiple address types
- Contact information management
- Role-based access control with permissions

## Technology Stack

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- PostgreSQL database
- OpenAPI/Swagger for API documentation
- MapStruct for object mapping

## Getting Started

### Prerequisites

- Java JDK 17 or newer
- Maven 3.6+
- PostgreSQL database

### Environment Variables

Set the following environment variables:

```
SPRING_PROFILES_ACTIVE=development
APP_NAME=DemoDepartments
DB_HOST=localhost
DB_PORT=5432
DB_NAME=demoDepartments
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_PARAMS=
SERVER_URL=http://localhost:8080
```

### Running the Application

```bash
# Clone the repository
git clone https://github.com/yourusername/demoDepartments.git
cd demoDepartments

# Build the application
mvn clean install

# Run the application
mvn spring-boot:run
```

## API Documentation

The API documentation is available through Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

You can also access the raw OpenAPI specification at:

```
http://localhost:8080/v3/api-docs
```

## Data Model

The application is built around these core entities:

- **Person**: Basic user information
- **Address**: Physical addresses (home, work, etc.)
- **Contact**: Contact information like emails and phone numbers
- **Role**: User roles for access control
- **Permissions**: Specific permissions assigned to roles

## Mapping Levels

The API supports multiple mapping levels to control the detail of returned data:

- **MINIMAL**: Only essential data
- **BASIC**: Core fields plus basic relationships
- **SUMMARY**: Comprehensive view with related entities
- **COMPLETE**: Full data including all related entities