# Use an official OpenJDK 21 runtime as a parent image (since you are using Java 21)
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml first (for better caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (this step will be cached)
RUN ./mvnw dependency:go-offline

# Copy the project source
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Run the jar file
# Render passes the PORT environment variable to the container
ENTRYPOINT ["java","-jar","target/banking-1.0.0.jar"]
