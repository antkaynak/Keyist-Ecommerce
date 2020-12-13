FROM maven:3.6-adoptopenjdk-11 AS build

WORKDIR /build

COPY pom.xml .
COPY src src

RUN mvn package -DskipTests


FROM adoptopenjdk:11-jre-hotspot
WORKDIR /app

COPY --from=build /build/target/*.jar authorization_server.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "authorization_server.jar"]