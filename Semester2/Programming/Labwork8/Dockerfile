FROM gradle:7.4-jdk17-alpine AS build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN gradle build --no-daemon

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
COPY --from=build /app/server/build/libs/server-all.jar /app/server.jar
COPY server/.env.dev /app/.env

ENTRYPOINT ["java","-jar","/app/server.jar"]