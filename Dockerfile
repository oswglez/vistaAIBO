# Etapa 1: Construcción
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw # Buena práctica añadirlo por si acaso
RUN ./mvnw dependency:resolve # Esperemos que esto ahora funcione
COPY src ./src
RUN ./mvnw package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jre-jammy AS stage-1
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]