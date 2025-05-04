# Estágio de build com Corretto JDK
FROM amazoncorretto:21-alpine-jdk AS builder

WORKDIR /app

# Cache de dependências
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle /app/gradle
RUN ./gradlew dependencies --no-daemon

# Build da aplicação
COPY src /app/src
RUN ./gradlew build --no-daemon -x test

# Estágio de produção com Corretto JRE
FROM amazoncorretto:21-alpine-jre

WORKDIR /app

# Copia o JAR construído
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Configurações recomendadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]