FROM openjdk:25-jdk-slim

WORKDIR /app

COPY backend/med-manager-backend/target/med-manager-backend-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
