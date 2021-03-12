FROM openjdk:11-jdk-slim
EXPOSE 8080
ARG JAR_FILE=build/libs/*-all.jar

ADD ${JAR_FILE} app.jar

ENV APP_NAME keymanager-rest

ENTRYPOINT ["java", "-jar", "/app.jar"]