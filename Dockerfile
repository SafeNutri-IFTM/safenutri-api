# Estágio 1: Build
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Copia as imagens para uma pasta dentro da imagem (não montada como volume)
COPY src/main/resources/arquivos/receitas /app/uploads_default/receitas
COPY src/main/resources/arquivos/nutricionistas /app/uploads_default/nutricionistas

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]