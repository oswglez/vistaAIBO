# Etapa 1: Construcción de la aplicación con Maven
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Copiar el wrapper de Maven y el pom.xml primero para aprovechar el caché de Docker
# Asumiendo que tienes .mvn y mvnw en la raíz de C:\Users\Admin\IdeaProjects\vistaAIBO
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargar dependencias (esto se cacheará si pom.xml no cambia)
RUN ./mvnw dependency:resolve

# Copiar el código fuente
COPY src ./src

# Empaquetar la aplicación
RUN ./mvnw package -DskipTests

# Etapa 2: Creación de la imagen final ligera
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia el JAR construido desde la etapa 'builder'
# El nombre del JAR puede variar, *.jar lo tomará genéricamente
COPY --from=builder /app/target/*.jar app.jar

# Expón el puerto en el que tu aplicación Spring Boot escucha (usualmente 8080)
EXPOSE 8090

# Comando para ejecutar la aplicación
# Puedes añadir perfiles de Spring aquí si es necesario, ej: "-Dspring.profiles.active=docker"
ENTRYPOINT ["java", "-jar", "app.jar"]