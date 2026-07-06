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

# Instala todas as dependências que o Playwright (Chromium) exige
RUN apt-get update && apt-get install -y \
    libglib2.0-0 \
    libnss3 \
    libnspr4 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libatspi2.0-0 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libxcb1 \
    libxkbcommon0 \
    libpango-1.0-0 \
    libcairo2 \
    libasound2 \
    && rm -rf /var/lib/apt/lists/*

# Cria pastas padrão para imagens (receitas e nutricionistas)
RUN mkdir -p /app/uploads_default/receitas /app/uploads_default/nutricionistas
COPY src/main/resources/arquivos/receitas /app/uploads_default/receitas
COPY src/main/resources/arquivos/nutricionistas /app/uploads_default/nutricionistas

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]