#
# Build stage
#
FROM eclipse-temurin:17-jdk-focal AS build
ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN --mount=type=cache,target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:17-jre-focal
ARG JAR_FILE=/app/target/*-with-dependencies.jar
COPY --from=build $JAR_FILE /app/runner.jar
ENV FILENAME data.json
ENTRYPOINT java -jar /app/runner.jar
