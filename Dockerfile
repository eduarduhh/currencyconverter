# Etapa única: build e execução com Amazon Corretto
#FROM amazoncorretto:21-alpine-jdk
FROM gradle:jdk21-corretto

WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Dá permissão ao Gradle wrapper (caso esteja sem execução)
RUN chmod +x ./gradlew

# Executa o build, ignorando testes para produção
#RUN ./gradlew clean build -x test -x check
RUN gradle clean build -x test -x check


# Copia o JAR construído para o diretório de execução
RUN cp /app/build/libs/*SNAPSHOT.jar app.jar

# Expõe a porta 8080 (usada pelo Spring Boot dentro do container)
EXPOSE 8080
# Executa o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]