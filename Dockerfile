# Usar uma imagem base do Maven para compilar o projeto
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app

# Copiar o código-fonte para o container
COPY src /app/src
COPY pom.xml /app

# Compilar o projeto
RUN mvn -f /app/pom.xml clean package -DskipTests

# Usar uma imagem base do Java para rodar a aplicação
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copiar o jar compilado do estágio anterior
COPY --from=build /app/target/api-transferencia.jar /app/app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]