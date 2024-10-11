FROM openjdk:17

ENV TZ=Asia/Seoul
COPY build/libs/daepiro-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
