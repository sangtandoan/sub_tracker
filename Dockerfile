# Multi-stage Dockerfile for Spring Boot application with Gradle

# Stage 1: Build stage
FROM gradle:8.10.2-jdk21-alpine AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and configuration files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies (this layer will be cached if dependencies don't change)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ ./src/

# Build the application
RUN ./gradlew build --no-daemon -x test

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/sub_tracker-0.0.1-SNAPSHOT.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Set JVM options for containerized environment and improved startup
# ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+UseStringDeduplication"

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

