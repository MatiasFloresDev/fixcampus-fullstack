FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 10000
ENTRYPOINT ["sh", "-c", "if [ -n \"${DATABASE_URL:-}\" ]; then case \"$DATABASE_URL\" in jdbc:*) export DB_URL=\"$DATABASE_URL\" ;; *) export DB_URL=\"jdbc:$DATABASE_URL\" ;; esac; fi; exec java -jar /app/app.jar"]
