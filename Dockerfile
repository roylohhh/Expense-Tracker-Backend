# Use Gradle base image with Java 23
FROM gradle:8.12.1-jdk23 AS build

WORKDIR /app

# Copy necessary Gradle files
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Grant execution permission to Gradlew
RUN chmod +x gradlew

# Set the Gradle home environment variable (optional)
ENV GRADLE_USER_HOME=/app/.gradle

# Download dependencies (optional caching step)
RUN ./gradlew dependencies --no-daemon

# Copy the full project
COPY . .

# Build the application
RUN ./gradlew build -x test

# Use a smaller Java 23 runtime image
FROM eclipse-temurin:23 AS runtime
WORKDIR /app

# Copy the JAR from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar

# Copy the .env file (optional, useful for local development)
COPY .env .env

# Expose the application port
EXPOSE 8080

# Load environment variables & run the application
CMD export $(grep -v '^#' .env | xargs) && java -jar app.jar
