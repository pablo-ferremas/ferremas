#################################################################
# Optimized multi-stage Dockerfile
# - cachea dependencias copiando pom primero
# - usa Maven en la etapa de build y una JRE ligera en runtime
# - copia sólo el jar final desde la etapa de build
# - ejecuta la app como usuario no-root
#################################################################

### Build stage: compilar el jar
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom para cachear dependencias
COPY pom.xml ./

# Pre-descargar dependencias para aprovechar cache de Docker
RUN mvn -B -e -DskipTests dependency:go-offline

# Copiar todo el código fuente
COPY . .

# Compilar el artefacto
RUN mvn -B clean package -DskipTests

### Runtime stage: imagen ligera sólo con JRE
FROM eclipse-temurin:17-jre-jammy

# Crear usuario no-root para mayor seguridad
RUN useradd --create-home --shell /bin/bash appuser || true

WORKDIR /app

# ARG para permitir especificar el jar exacto si lo necesitas
ARG JAR_FILE=target/*.jar

# Copiar el artefacto compilado desde la etapa de build
COPY --from=build /app/${JAR_FILE} /app/app.jar

# Puerto en el que corre la app (coincide con server.port en application.properties)
EXPOSE 8080

# Opciones Java por defecto (puedes sobrescribir con -e JAVA_OPTS)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Ejecutar como usuario no-root
USER appuser

# ENTRYPOINT que respeta JAVA_OPTS y permite pasar argumentos (p.ej. --spring.profiles.active=prod)
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar \"$@\""]
CMD ["--spring.profiles.active=prod"]
