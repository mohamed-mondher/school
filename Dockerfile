FROM openjdk:17-jdk-slim
WORKDIR /app

COPY target/school-0.0.1-SNAPSHOT.jar /app/school-0.0.1-SNAPSHOT.jar

## Copier le fichier init.sql dans le répertoire /docker-entrypoint-initdb.d du conteneur
#COPY src/main/resources/init.sql /docker-entrypoint-initdb.d/

# Étape 4 : Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Étape 5 : Démarrer l'application avec la commande Java
ENTRYPOINT ["java", "-jar", "school-0.0.1-SNAPSHOT.jar"]
