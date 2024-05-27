package com.desafioitau.api.transferencia.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    private static final Long CONNECT_TIMEOUT = 5000L;
    private static final Long CONNECTION_REQUEST_TIMEOUT = 5000L;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){

        return  restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT))
           .setReadTimeout(Duration.ofMillis(CONNECTION_REQUEST_TIMEOUT))
           .build();
    }
}
