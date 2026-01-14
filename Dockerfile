# --- ETAPA 1: BUILD (Construcción) ---
# Usamos una imagen de Maven con Java 17 para compilar
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos todo el código fuente al contenedor
COPY . .

# Ejecutamos el comando de Maven para crear el JAR (saltando los tests para ir más rápido)
RUN mvn clean package -Dmaven.test.skip=true

# --- ETAPA 2: RUN (Ejecución) ---
# Usamos una imagen ligera de Java 17 solo para correr la app
FROM eclipse-temurin:17-jdk-jammy

# Directorio de trabajo
WORKDIR /app

# Copiamos el JAR generado en la etapa anterior (build) a esta nueva etapa
# El *.jar busca cualquier archivo jar generado
COPY --from=build /app/target/*.jar app.jar

# Informamos que la app usa el puerto 8080
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]