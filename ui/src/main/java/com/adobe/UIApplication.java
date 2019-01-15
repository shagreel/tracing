/*
Copyright 2019 Adobe. All rights reserved.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.adobe;

import com.adobe.models.SpanHttpRequestInterceptor;
import com.adobe.models.SpanUtil;
import com.adobe.models.TracingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
public class UIApplication {

    private @Autowired
    AutowireCapableBeanFactory beanFactory;

    public static void main(String[] args) {
        SpringApplication.run(UIApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        SpanHttpRequestInterceptor interceptor = new SpanHttpRequestInterceptor();
        beanFactory.autowireBean(interceptor);
        template.setInterceptors(Collections.singletonList(interceptor));
        return template;
    }

    @Bean
    public FilterRegistrationBean<TracingFilter> tracingFilter() {
        FilterRegistrationBean<TracingFilter> registrationBean = new FilterRegistrationBean<>();
        TracingFilter tracingFilter = new TracingFilter();
        beanFactory.autowireBean(tracingFilter);
        registrationBean.setFilter(tracingFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SpanUtil spanUtil() {
        return new SpanUtil();
    }
}