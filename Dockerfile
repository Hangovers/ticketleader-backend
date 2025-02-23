# -----------------------------
# Stage 1: Build the application
# -----------------------------
FROM gradle:8.11.1-jdk21 AS build
WORKDIR /home/gradle/project
# Copy the project files into the container (adjust .dockerignore as needed)
COPY --chown=gradle:gradle . .
# Build the application without the Gradle daemon for a cleaner build
RUN gradle build --no-daemon

# -----------------------------
# Stage 2: Package and run the application
# -----------------------------
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copy the built JAR from the previous stage into the app folder
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar
# Expose the default Micronaut port (adjust if you use a different one)
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"] 