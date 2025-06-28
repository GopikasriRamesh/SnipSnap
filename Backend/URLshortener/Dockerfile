# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml first, to cache dependencies
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# Run Maven to download dependencies (this gets cached)
RUN ./mvnw dependency:go-offline

# Now copy the rest of the source code
COPY src ./src

# Package the Spring Boot app
RUN ./mvnw package -DskipTests

# Expose port 8080
EXPOSE 8080

COPY . .
RUN ./mvnw clean install -DskipTests


# Run the Spring Boot app
CMD ["java", "-jar", "target/URLshortener-0.0.1-SNAPSHOT.jar"]


