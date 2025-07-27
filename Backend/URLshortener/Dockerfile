# ✅ Use OpenJDK 21 slim as base
FROM openjdk:21-jdk-slim

# ✅ Set working directory inside the container
WORKDIR /app

# ✅ Copy Maven wrapper and POM file first to cache dependencies
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

# ✅ Download dependencies (this step is cached for faster builds)
RUN ./mvnw dependency:go-offline

# ✅ Copy the application source code
COPY src ./src

# ✅ Package the Spring Boot app (without running tests)
RUN ./mvnw clean package -DskipTests

# ✅ Expose the port your Spring Boot app runs on
EXPOSE 8080

# ✅ Run the Spring Boot app using the final jar
CMD ["java", "-jar", "target/URLshortener-0.0.1-SNAPSHOT.jar"]
