FROM maven:3.6-jdk-8-alpine as build
ADD . /build
WORKDIR /build
RUN mvn clean package

FROM openjdk:8-jre-alpine
WORKDIR /root
COPY --from=build /build/target/tracing-ads-0.1.0.jar .
CMD ["java", "-jar", "tracing-ads-0.1.0.jar"]