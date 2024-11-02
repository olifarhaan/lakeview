# First Stage (build)
FROM openjdk:21-slim AS build

WORKDIR /app

# Copy all necessary files
COPY . .

# Set executable permission for the Gradle wrapper and run the build
RUN chmod +x gradlew && ./gradlew clean build --no-daemon

# Second Stage (run)
FROM openjdk:21-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*-boot.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]