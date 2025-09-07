FROM public.ecr.aws/docker/library/eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

CMD ["sh", "-c", "./gradlew bootRun --no-daemon"]
