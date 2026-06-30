# Estágio 1: Build da aplicação usando Maven e Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY sgrr/ .
RUN mvn clean package -DskipTests

# Estágio 2: Execução da aplicação usando JRE 21 leve
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]