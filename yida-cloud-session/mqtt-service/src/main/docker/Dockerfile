FROM java:8
VOLUME /tmp
ADD /target/mqtt-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xmx150m","-jar","/app.jar"]
