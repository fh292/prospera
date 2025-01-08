FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install

FROM eclipse-temurin:17
WORKDIR app
COPY --from=build /app/target/*.jar prospera.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","prospera.jar"]
