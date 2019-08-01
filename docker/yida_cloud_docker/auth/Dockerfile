FROM java:8
VOLUME /tmp
ADD auth-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Xmx50m","/app.jar"]
