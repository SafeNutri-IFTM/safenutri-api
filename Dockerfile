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

# Instala Node.js e Playwright
RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

RUN npm install -g playwright

# Instala dependências do sistema (playwright install-deps)
RUN playwright install-deps

# Instala pacotes de desenvolvimento (para garantir que as bibliotecas estejam presentes)
RUN apt-get update && apt-get install -y \
    libicu-dev \
    libxml2-dev \
    libvpx-dev \
    && rm -rf /var/lib/apt/lists/*

# 🔥 Script que cria links simbólicos automaticamente
RUN for lib in libicudata libicui18n libicuuc; do \
        found=$(find /usr/lib -name "${lib}.so*" 2>/dev/null | head -1); \
        if [ -n "$found" ]; then \
            ln -sf "$found" "/usr/lib/x86_64-linux-gnu/${lib}.so.70"; \
        fi; \
    done && \
    for lib in libxml2 libvpx; do \
        found=$(find /usr/lib -name "${lib}.so*" 2>/dev/null | head -1); \
        if [ -n "$found" ]; then \
            ln -sf "$found" "/usr/lib/x86_64-linux-gnu/${lib}.so.2"; \
        fi; \
    done && \
    # Ajuste específico para libvpx (espera .so.7)
    found_vpx=$(find /usr/lib -name "libvpx.so*" 2>/dev/null | head -1); \
    if [ -n "$found_vpx" ]; then \
        ln -sf "$found_vpx" "/usr/lib/x86_64-linux-gnu/libvpx.so.7"; \
    fi

# Cria pastas padrão para imagens
RUN mkdir -p /app/uploads_default/receitas /app/uploads_default/nutricionistas
COPY src/main/resources/arquivos/receitas /app/uploads_default/receitas
COPY src/main/resources/arquivos/nutricionistas /app/uploads_default/nutricionistas

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]