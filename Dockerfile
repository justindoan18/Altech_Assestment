# =============================
# 1. Build Stage
# =============================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml và download dependency trước
COPY pom.xml .
COPY src ./src

# Build project (tạo JAR)
RUN mvn clean package -DskipTests

# =============================
# 2. Runtime Stage
# =============================
FROM eclipse-temurin:21-jdk
WORKDIR /app

# Copy JAR từ build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run app
ENTRYPOINT ["java", "-jar", "app.jar"]
