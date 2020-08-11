FROM openjdk:11-jre-slim
COPY ./husky/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
