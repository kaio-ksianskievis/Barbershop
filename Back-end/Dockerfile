# --- ETAPA 1: BUILD ---
# ATENÇÃO: Mudamos para uma imagem com Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Adicionei o parametro de encoding para garantir que não dê erro de acentos
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# --- ETAPA 2: RUN ---
# ATENÇÃO: Mudamos para Java 21 aqui também
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]