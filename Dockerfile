FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8080

ADD build/libs/filestorage-0.0.1-SNAPSHOT.jar filestorage.jar

ENTRYPOINT ["java", "-jar", "/filestorage.jar"]