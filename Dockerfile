FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

CMD ["sh", "-c", "./gradlew bootRun --no-daemon"]
