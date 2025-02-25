FROM openjdk:8-jdk-alpine

WORKDIR /usr/src/app

COPY . .

RUN chmod +x ./mvnw

EXPOSE 8080

RUN ./mvnw clean package

CMD ["java", "-jar", "target/InternetProvider-0.0.1-SNAPSHOT.jar"]