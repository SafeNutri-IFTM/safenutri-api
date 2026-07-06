# Estágio 1: Build com cache de dependências
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copiar apenas o pom.xml para baixar dependências (cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar o código fonte e buildar
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]