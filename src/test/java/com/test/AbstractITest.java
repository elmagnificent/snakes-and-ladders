package com.test;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Michael on 12.04.2021.
 */
public abstract class AbstractITest {
    private final String baseUrl;

    protected HttpHeaders headers;

    @Autowired
    protected TestRestTemplate template;

    protected AbstractITest(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /*@BeforeEach
    public void setHeaders() {
        setHeaders("admin@sarus.ro", "1234");
    }*/

    protected <T> ResponseEntity<T> get(String urlSuffix, Class<T> responseType, Object... urlVariables) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.GET, new HttpEntity<>(null, null), responseType, urlVariables);
    }

    protected <T> ResponseEntity<T> get(String urlSuffix, ParameterizedTypeReference<T> responseType, Object... urlVariables) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.GET, new HttpEntity<>(headers), responseType, urlVariables);
    }

    protected <T> ResponseEntity<T> post(String urlSuffix, Object payload, Class<T> responseType, Object... urlVariables) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.POST, new HttpEntity<>(payload, headers), responseType, urlVariables);
    }

    protected <T> ResponseEntity<T> post(String urlSuffix, Object payload, Class<T> responseType) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.POST, new HttpEntity<>(payload, headers), responseType);
    }

    protected <T> ResponseEntity<T> post(String urlSuffix, Object payload, ParameterizedTypeReference<T> responseType, Object... urlVariables) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.POST, new HttpEntity<>(payload, headers), responseType, urlVariables);
    }

    protected <T> ResponseEntity<T> post(String urlSuffix, ParameterizedTypeReference<T> responseType, Object... urlVariables) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.POST, new HttpEntity<>(headers), responseType, urlVariables);
    }

    protected <T> ResponseEntity<T> put(String urlSuffix, Object payload, Class<T> responseType, Object... urlVariables) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.PUT, new HttpEntity<>(payload, headers), responseType, urlVariables);
    }

    protected <T> ResponseEntity<T> put(String urlSuffix, Object payload, ParameterizedTypeReference<T> responseType) {
        return template.exchange(baseUrl + urlSuffix, HttpMethod.PUT, new HttpEntity<>(payload, headers), responseType);
    }
}
