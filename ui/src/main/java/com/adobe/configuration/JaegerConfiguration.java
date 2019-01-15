package com.adobe.configuration;

import io.opencensus.exporter.trace.jaeger.JaegerTraceExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JaegerConfiguration {

    public JaegerConfiguration(@Value("${opencensus.jaeger.thrift.url}") String jaegerThriftEndpoint,
                               @Value("${spring.application.name}") String appName) {
        JaegerTraceExporter.createAndRegister(jaegerThriftEndpoint, appName);
    }
}