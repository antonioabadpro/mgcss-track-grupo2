# Etapa 1: Construcción (Maven)
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java","-jar","app.jar"]