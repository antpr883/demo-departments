FROM eclipse-temurin:17-jdk-alpine

# Install curl for health checks
RUN apk add --no-cache curl

ARG JAR_FILE=target/demoDepartments-0.0.1-SNAPSHOT.jar
WORKDIR /app

# Create logs directory
RUN mkdir -p /app/logs && chmod 777 /app/logs

COPY ${JAR_FILE} app.jar

# Set volume for logs
VOLUME /app/logs

# Expose both app and actuator ports
EXPOSE 8080

# Set Java options to enable logging and metrics
ENTRYPOINT ["java", "-Dlogging.file.path=/app/logs", "-Dmanagement.endpoints.web.exposure.include=health,info,prometheus", "-jar", "app.jar"]

# Add a health check
HEALTHCHECK --interval=10s --timeout=3s CMD curl --fail http://localhost:8080/actuator/health || exit 1