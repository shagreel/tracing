package com.adobe.models;

import io.opencensus.common.Scope;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import java.io.IOException;

public class TracingFilter implements Filter {
    private final Tracer tracer = Tracing.getTracer();

    @Autowired
    private SpanUtil spanUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Span span = spanUtil.create(request);

        try (Scope s = tracer.withSpan(span)) {
            filterChain.doFilter(request, response);
        }

        span.end();
    }
}