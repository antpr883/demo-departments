# Demo Departments Application

A Spring Boot application for managing departments, employees, and related data with comprehensive monitoring and observability.

## Features

- Person management with various information levels
- Address management with support for multiple address types
- Contact information management
- Role-based access control with permissions
- Comprehensive monitoring with Grafana, Prometheus, and Loki
- Structured JSON logging with Loki integration
- Real-time metrics visualization

## Technology Stack

- Java 17
- Spring Boot 3.4.4
- Spring Data JPA
- PostgreSQL database
- OpenAPI/Swagger for API documentation
- MapStruct for object mapping
- Prometheus/Victoria Metrics for metrics
- Loki for log aggregation
- Tempo for distributed tracing
- Grafana for visualization

## Running the Application

### Using Docker (Recommended)

The easiest way to run the application is using Docker:

```bash
# Navigate to the docker directory
cd docker

# Build the application and Docker images
./manage.sh build

# Start the application and all supporting services
./manage.sh start

# Check the status of the services
./manage.sh status

# View logs for all services
./manage.sh logs

# View logs for a specific service
./manage.sh logs app

# Open main services in browser
./manage.sh open

# Show environment configuration
./manage.sh info

# Stop the application
./manage.sh stop

# Restart all services
./manage.sh restart

# Clean up volumes (if needed)
./manage.sh clean
```

### Manual Startup

If you prefer to run the application outside of Docker:

```bash
# Build the application
./mvnw clean package

# Run the application
java -jar target/demoDepartments-0.0.1-SNAPSHOT.jar
```

## Accessing the Application

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Grafana**: http://localhost:3000 (default credentials: admin/admin)
- **Victoria Metrics** (Prometheus compatible): http://localhost:8428
- **Tempo** (Distributed tracing): http://localhost:3200
- **Loki** (Log aggregation): http://localhost:3100

## Monitoring and Observability

The application includes a comprehensive monitoring stack:

- **Metrics**: Prometheus/Victoria Metrics for storing metrics
- **Logs**: Loki for log aggregation with structured JSON logging
- **Traces**: Tempo for distributed tracing
- **Visualization**: Grafana for dashboards and data visualization

### Grafana Dashboards

Pre-configured dashboards:
- **Master Dashboard** - All-in-one view of metrics, logs, and system health
- **Logs Dashboard** - Dedicated view for application logs analysis
- **App Metrics Dashboard** - Detailed application metrics

For detailed information on monitoring capabilities, see [Monitoring Guide](docker/MONITORING-GUIDE.md)

## Database Management

### PostgreSQL Database

The application uses PostgreSQL for data storage:

- **Host**: localhost
- **Port**: 55432 (mapped from 5432 inside container)
- **Database**: dep_user
- **Username**: demo_departments
- **Password**: 123456

### Data Persistence

Data persistence is managed through JPA with Hibernate:

- The database schema is updated automatically with `hibernate.ddl-auto=update`
- Liquibase is configured for database migrations
- PostgreSQL data is persisted in a Docker volume

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

## Troubleshooting

If you encounter any issues:

1. **Database Connectivity**: Ensure PostgreSQL container is running with `./manage.sh ps`
2. **Missing Metrics**: Check if the application has the correct Prometheus endpoint exposed at `/actuator/prometheus`
3. **Volume Issues**: Use `./manage.sh clean` to remove volumes and start fresh
4. **Log Access**: Logs can be viewed through Grafana (Loki datasource) or directly with `./manage.sh logs`

## Development Guidelines

See the following guides for development best practices:

- [Architecture Guide](ARCHITECTURE-GUIDE.md)
- [REST API Guide](REST-API-GUIDE.md)
- [Liquibase Guide](LIQUIBASE-GUIDE.md)