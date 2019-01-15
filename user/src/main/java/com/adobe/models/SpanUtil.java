package com.adobe.models;

import io.opencensus.trace.*;
import io.opencensus.trace.propagation.SpanContextParseException;
import io.opencensus.trace.propagation.TextFormat;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class SpanUtil {
    private Span span;
    private final Tracer tracer = Tracing.getTracer();
    private final TextFormat textFormat = Tracing.getPropagationComponent().getB3Format();
    private final TextFormat.Setter setter = new TextFormat.Setter<HttpHeaders>() {
        public void put(HttpHeaders carrier, String key, String value) {
            carrier.add(key, value);
        }
    };
    private final TextFormat.Getter<HttpServletRequest> getter = new TextFormat.Getter<HttpServletRequest>() {
        public String get(HttpServletRequest httpRequest, String s) {
            return httpRequest.getHeader(s);
        }
    };

    public Span create(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest) req;
        SpanContext spanContext;
        SpanBuilder spanBuilder;
        String spanName = request.getMethod() + " " + request.getRequestURI();
        try {
            spanContext = textFormat.extract(request, getter);
            spanBuilder = tracer.spanBuilderWithRemoteParent(spanName, spanContext);
        } catch (SpanContextParseException e) {
            spanBuilder = tracer.spanBuilder(spanName);
        }
        span = spanBuilder.setRecordEvents(true)
                .setSampler(Samplers.alwaysSample())
                .startSpan();
        return span;

    }

    public HttpHeaders getHeadersWithSpanContext() {
        HttpHeaders headers = new HttpHeaders();
        textFormat.inject(span.getContext(), headers, setter);
        return headers;
    }
}
