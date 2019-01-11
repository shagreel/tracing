FROM maven:3.6-jdk-8-alpine as build
RUN apk add unzip && \
    curl -O "http://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip" && \
    unzip newrelic-java.zip -d /build
ADD . /build
WORKDIR /build
RUN mvn clean package

FROM openjdk:8-jre-alpine
WORKDIR /root
COPY --from=build /build/newrelic/newrelic.* ./
COPY --from=build /build/target/tracing-ui-0.1.0.jar .
CMD ["java", "-javaagent:newrelic.jar", "-jar", "tracing-ui-0.1.0.jar"]