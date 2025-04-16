#!/bin/bash

# Docker management script for Demo Departments application
# Provides easy commands to build, start, stop, and monitor services

# Make sure script is run from the docker directory
cd "$(dirname "$0")" || { echo "Error: Failed to change to docker directory"; exit 1; }

# Check if Docker is installed and running
if ! command -v docker &> /dev/null; then
    echo "Error: Docker is not installed or not in PATH"
    exit 1
fi

if ! docker info &> /dev/null; then
    echo "Error: Docker daemon is not running"
    exit 1
fi

# Check for .env file
if [ ! -f .env ]; then
    echo "Warning: .env file not found. Creating sample file..."
    cat > .env << EOF
##########  Application #########
APP_NAME=demo_departments
SPRING_PROFILES_ACTIVE=dev
SERVER_URL=http://localhost:8080

########## DB REQUIRED #########
DB_HOST=localhost
DB_PORT=55432
DB_NAME=demo_departments
DB_USERNAME=demo_departments
DB_PASSWORD=123456
DB_PARAMS=ssl=false

# Logging settings
LOG_PATH=/logs
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_DEMO_DEPARTMENTS=DEBUG
EOF
    echo ".env file created. Please review and modify if needed."
fi

# Create required directories
mkdir -p data/grafana
mkdir -p config/loki
mkdir -p config/tempo
mkdir -p config/victoria-metrics
mkdir -p config/grafana/provisioning/{datasources,dashboards}

# Display service URLs
show_urls() {
    echo "Access points:"
    echo "- App: http://localhost:8080"
    echo "- Swagger UI: http://localhost:8080/swagger-ui/index.html"
    echo "- Grafana: http://localhost:3000 (default credentials: admin/admin)"
    echo "- Victoria Metrics: http://localhost:8428"
    echo "- Tempo: http://localhost:3200"
    echo "- Loki: http://localhost:3100"
}

# Process commands
case "$1" in
    start)
        echo "Starting services..."
        docker compose up -d
        echo "Services started."
        show_urls
        ;;
    stop)
        echo "Stopping services..."
        docker compose down
        echo "Services stopped"
        ;;
    restart)
        echo "Restarting services..."
        docker compose down && docker compose up -d
        echo "Services restarted."
        show_urls
        ;;
    logs)
        if [ -z "$2" ]; then
            echo "Showing logs for all services..."
            docker compose logs -f --tail=100
        else
            echo "Showing logs for $2..."
            docker compose logs -f --tail=100 "$2"
        fi
        ;;
    ps|status)
        echo "Service status:"
        docker compose ps
        ;;
    clean)
        echo "Cleaning up volumes..."
        docker compose down -v
        echo "Volumes removed"
        ;;
    build)
        echo "Building application..."
        (cd .. && ./mvnw clean package -DskipTests)
        echo "Building Docker images..."
        docker compose build
        echo "Build complete. Use './manage.sh start' to start the services."
        ;;
    open)
        # Check which browser to use
        if command -v xdg-open &> /dev/null; then
            BROWSER="xdg-open"
        elif command -v open &> /dev/null; then
            BROWSER="open"
        else
            echo "Error: No browser command found"
            exit 1
        fi
        
        # Open services in browser tabs
        $BROWSER http://localhost:8080/swagger-ui/index.html
        $BROWSER http://localhost:3000
        echo "Opened main services in browser"
        ;;
    info)
        # Show current configuration
        echo "Current Configuration:"
        echo "---------------------"
        grep -v "^#" .env | sort
        echo "---------------------"
        echo "Docker Compose Services:"
        docker compose config --services
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|logs [service]|status|clean|build|open|info}"
        echo ""
        echo "Commands:"
        echo "  start   - Start all services"
        echo "  stop    - Stop all services"
        echo "  restart - Restart all services"
        echo "  logs    - Show logs (all services or specific service)"
        echo "  status  - Show status of all services (alias: ps)"
        echo "  clean   - Remove all volumes (data will be lost)"
        echo "  build   - Build application and Docker images"
        echo "  open    - Open main services in browser"
        echo "  info    - Show environment configuration"
        exit 1
        ;;
esac

exit 0