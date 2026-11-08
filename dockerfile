# ============================================================
# ÉTAPE 1 : BUILD (Maven + JDK)
# ============================================================
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /build

# Copier pom.xml d'abord (cache des dépendances)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et builder
COPY src ./src
RUN mvn clean package -DskipTests

# ============================================================
# ÉTAPE 2 : RUNTIME (Tomcat + JRE)
# ============================================================
FROM tomcat:10.1-jdk21-temurin

# Supprimer les applications par défaut de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier le WAR dans le contexte attendu par le front /monapp
COPY --from=builder /build/target/monapp.war /usr/local/tomcat/webapps/ROOT.war

# Exposer le port
EXPOSE 8080

# Démarrer Tomcat
CMD ["catalina.sh", "run"]