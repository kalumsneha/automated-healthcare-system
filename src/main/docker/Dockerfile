FROM eclipse-temurin:21
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} automated-healthcare-system-1.0.0.jar
EXPOSE 5050
ENTRYPOINT ["java", "-jar", "automated-healthcare-system-1.0.0.jar"]