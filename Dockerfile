# Use a base image with Java
FROM amazoncorretto:17 AS build

# Set working directory
WORKDIR /app

# Copy only necessary files first to leverage Docker caching
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Grant execution permission to Gradlew
RUN chmod +x gradlew

# Download dependencies to cache layer
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the application files
COPY . .

# Build the application
RUN ./gradlew build -x test

# Use a minimal JDK runtime
FROM amazoncorretto:17 AS runtime
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
