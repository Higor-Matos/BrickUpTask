# Usar uma imagem base Java
FROM openjdk:21-jdk-slim

# Definir o diretório de trabalho dentro do container
WORKDIR /app

# Copiar o arquivo pom.xml, o Maven Wrapper e o diretório .mvn
COPY pom.xml mvnw /app/
COPY .mvn /app/.mvn/

# Tornar o script Maven Wrapper executável
RUN chmod +x /app/mvnw

# Baixar as dependências do Maven para aproveitar o cache de camadas do Docker
RUN ./mvnw dependency:go-offline -B

# Copiar os arquivos do projeto para o diretório de trabalho
COPY src /app/src

# Construir o aplicativo Spring Boot
RUN ./mvnw package -DskipTests

# Executar o aplicativo Spring Boot
ENTRYPOINT ["java", "-jar", "/app/target/BrickUpTask-0.0.1-SNAPSHOT.jar"]
