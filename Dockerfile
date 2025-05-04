# Etapa 1: build da aplicação com Gradle wrapper
 FROM amazoncorretto:21-alpine-jdk AS builder

 WORKDIR /app

 # Copia os arquivos do projeto
 COPY . .

 # Dá permissão ao Gradle wrapper (caso esteja sem execução)
 RUN chmod +x ./gradlew

 # Executa o build, ignorando testes para produção
 RUN ./gradlew clean build -x test -x check

 # Etapa 2: imagem final, menor possível
 FROM eclipse-temurin:21-jdk-alpine

 WORKDIR /app

 # Copia o JAR construído na etapa anterior
 COPY --from=builder /app/build/libs/*.jar app.jar

 # Expõe a porta padrão do Spring Boot
 EXPOSE 8080

 # Executa o JAR
 ENTRYPOINT ["java", "-jar", "app.jar"]
