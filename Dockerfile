FROM maven:3.8.4-openjdk-11-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim

WORKDIR /app

COPY - from=build /app/target/system-1.0.0.jar .

CMD ["java", "-jar", "system-1.0.0.jar"]
