# Build Stage
FROM maven:3.9.3-amazoncorretto-17 AS build

RUN mkdir -p /build
WORKDIR /build

ARG APP_SERVER_ENVIRONMENT

## Copy over the app files
COPY . /build/

RUN mvn -Denvironment=${APP_SERVER_ENVIRONMENT} -f pom.xml clean package

##################
# Deploy
##################
FROM ghcr.io/usepa/jdk-17:latest

COPY --from=build /build/target/physchem-1.0.0.jar /usr/local/lib/physchem-1.0.0.jar

RUN java --version

CMD ["java", "-jar", "-Dspring.profiles.active=${SERVER_ENVIRONMENT_PROFILE}", "/usr/local/lib/physchem-1.0.0.jar"]
