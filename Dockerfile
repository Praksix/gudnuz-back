# Stage 1: Build stage
FROM maven:3.9.6-openjdk-21-slim AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Télécharger les dépendances (cache layer)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire l'application
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage
FROM openjdk:21-jdk-slim

# Installer les outils nécessaires
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Créer un utilisateur non-root pour la sécurité
RUN groupadd -r gudnuz && useradd -r -g gudnuz gudnuz

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/Gudnuz-0.0.1-SNAPSHOT.jar app.jar

# Changer la propriété du fichier
RUN chown gudnuz:gudnuz app.jar

# Passer à l'utilisateur non-root
USER gudnuz

# Exposer le port 8080
EXPOSE 8080

# Variables d'environnement par défaut
ENV PORT=8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/health || exit 1

# Commande pour démarrer l'application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]