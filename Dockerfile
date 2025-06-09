# Etapa 1: Construcción de la aplicación con Maven
# Usamos eclipse-temurin con JDK 21, que coincide con tu Java 21.
# 'AS builder' nombra esta etapa para poder referenciarla luego.
FROM eclipse-temurin:21-jdk-jammy AS builder

# Establece el directorio de trabajo dentro de la imagen.
WORKDIR /app

# Copia primero el wrapper de Maven (.mvn) y los archivos mvnw y pom.xml.
# Esto aprovecha el caché de capas de Docker: si estos archivos no cambian,
# Docker no necesita volver a descargar las dependencias en el siguiente paso.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Asegura que el script mvnw sea ejecutable dentro del contenedor.
# Esto es crucial, especialmente si el archivo viene de Windows.
RUN chmod +x mvnw

# Descarga las dependencias del proyecto.
# Si 'dependency:resolve' sigue dando problemas, puedes intentar con 'dependency:go-offline'
# o incluso omitir este paso si 'package' las descarga igual (aunque es buena práctica).
# RUN ./mvnw dependency:resolve
RUN ./mvnw dependency:go-offline

# Copia el código fuente de tu aplicación.
COPY src ./src

# Empaqueta tu aplicación en un JAR, omitiendo los tests para acelerar
# la construcción de la imagen.
RUN ./mvnw package -DskipTests

# Etapa 2: Creación de la imagen final ligera para ejecución
# Usamos una imagen JRE (Java Runtime Environment) ya que solo necesitamos ejecutar el JAR.
# 'AS stage-1' nombra esta etapa. Puedes usar otro nombre si prefieres.
FROM eclipse-temurin:21-jre-jammy AS stage-1

# Establece el directorio de trabajo dentro de la imagen.
WORKDIR /app

# Copia el JAR construido desde la etapa 'builder' a la imagen actual.
# El comodín '*.jar' tomará el JAR generado sin importar su nombre exacto.
COPY --from=builder /app/target/*.jar app.jar

# Informa a Docker que el contenedor escuchará en el puerto 8090.
# Esto no publica el puerto al host, solo lo documenta.
EXPOSE 8090

# Comando para ejecutar la aplicación cuando el contenedor inicie.
# Puedes añadir argumentos de JVM aquí si es necesario.
# Por ejemplo: ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
ENTRYPOINT ["java", "-jar", "app.jar"]