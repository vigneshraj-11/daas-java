# First stage: Build the application
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

# Second stage: Create the runtime image
FROM openjdk:17-alpine

WORKDIR /app

COPY --from=build /app/target/system-1.0.0.jar .

CMD ["java", "-jar", "system-1.0.0.jar"]
