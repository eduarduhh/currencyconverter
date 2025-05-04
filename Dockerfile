# Estágio de construção otimizado para Kotlin/Java 21
FROM gradle:8.6-jdk21-alpine AS builder

WORKDIR /app

# Copia primeiro os arquivos de build para cache eficiente
COPY build.gradle.kts settings.gradle.kts gradle.properties /app/
COPY gradle /app/gradle
COPY src /app/src

# Build otimizado
RUN gradle build --no-daemon --stacktrace -x test

# Estágio de produção
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]