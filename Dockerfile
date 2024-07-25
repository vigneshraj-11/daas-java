FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY ./pom.xml /app/pom.xml
COPY ./src ./app/src

RUN mvn -f /app/pom.xml clean package

COPY . /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:17-alphine
EXPOSE 9000
COPY --from=build /app/target/*.jar daas.jar
ENTRYPOINT ["sh", "-c", "java -jar /daas.jar"]
