# Stage 1: Build
FROM openjdk:17-jdk-slim AS build

WORKDIR /Gudnuz

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Rendre mvnw exécutable
RUN chmod +x ./mvnw

# Copier le code source
COPY src src

# Construire l'application
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:17-jre-slim

WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Lancer l'application
CMD ["java", "-jar", "app.jar"]