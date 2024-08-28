# BUILD jar
FROM maven:3.6-alpine
WORKDIR /workspace/app
# Root stuff
COPY mvnw .
COPY .mvn .mvn
COPY ./pom.xml .
# Modules
COPY code-coverage-aggregate/pom.xml code-coverage-aggregate/pom.xml

COPY dao/src dao/src
COPY dao/pom.xml dao/pom.xml

COPY rest-client/src rest-client/src
COPY rest-client/pom.xml rest-client/pom.xml

COPY rest-server/src rest-server/src
COPY rest-server/pom.xml rest-server/pom.xml

RUN ["mvn", "clean", "install", "-DskipTests", "spring-boot:repackage"]
EXPOSE 8080
CMD ["mvn", "spring-boot:run"]
#"spring-boot:repackage"

