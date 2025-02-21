# Use a base image with Java
FROM amazoncorretto:17 AS build

# Set working directory
WORKDIR /app

# Copy and build the application
COPY . .
RUN ./mvnw package -DskipTests

# Use a minimal JDK runtime
FROM amazoncorretto:17 AS runtime
WORKDIR /app

# Copy the built JAR file
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]
