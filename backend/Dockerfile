FROM openjdk:11-jre-slim
EXPOSE 8080
ARG JAR_FILE=target/yourdrive-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} backend.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom ","-jar","/backend.jar"]