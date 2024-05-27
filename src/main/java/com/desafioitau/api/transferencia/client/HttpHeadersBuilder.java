package com.desafioitau.api.transferencia.client;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Getter
@Component
public class HttpHeadersBuilder {

    private final HttpHeaders headers;

    public HttpHeadersBuilder() {
        this.headers = new HttpHeaders();
        this.headers.set("Content-Type", "application/json");
    }

    public HttpEntity<String> buildHttpEntity() {
        return new HttpEntity<>(headers);
    }

    public HttpEntity<String> buildHttpEntity(String body) {
        return new HttpEntity<>(body, headers);
    }
}
