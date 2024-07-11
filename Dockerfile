FROM openjdk:18

WORKDIR /app

COPY target/taskflow-0.0.1-SNAPSHOT.jar /app/app.jar

ENV SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/task_flow?useSSL=false
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=12345678

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]