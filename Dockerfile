FROM gradle:8.14.4-jdk21 as build
COPY --chown=gradle:gradle . /app
WORKDIR /app

RUN gradle bootJar --no-daemon

ENTRYPOINT ["top", "-b"]

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/build/*.jar tauru_api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod","-jar","tauru_api.jar"]