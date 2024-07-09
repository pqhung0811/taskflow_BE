FROM openjdk:18-jre-slim

WORKDIR /app

COPY target/taskflow-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "app.jar"]