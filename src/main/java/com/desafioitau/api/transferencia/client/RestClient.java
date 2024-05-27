package com.desafioitau.api.transferencia.client;

import com.desafioitau.api.transferencia.constants.RestMessages;
import com.desafioitau.api.transferencia.exception.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

@Component
@Slf4j
public class RestClient {

    private final RestTemplate restTemplate;

    private final HttpHeadersBuilder httpHeadersBuilder;

    @Autowired
    public RestClient(RestTemplate restTemplate, HttpHeadersBuilder httpHeadersBuilder) {
        this.restTemplate = restTemplate;
        this.httpHeadersBuilder = httpHeadersBuilder;
    }

    public ResponseEntity<String> realizarRequisicaoGet(String url, String id){
        HttpEntity<String> headers = httpHeadersBuilder.buildHttpEntity();

        try {
            return restTemplate.exchange(url, HttpMethod.GET, headers, String.class, id);
        } catch (HttpClientErrorException e) {
            throw new RequestException(RestMessages.ERROR_4XX.getMessage()+e.getMessage(), e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new RequestException(RestMessages.ERROR_IO.getMessage()+e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }catch (HttpServerErrorException e) {
            throw new RequestException(RestMessages.ERROR_5XX.getMessage()+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> realizarRequisicaoPut(String url, String bodyRequest){
        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity(bodyRequest);

        try {
            return restTemplate.exchange(url, HttpMethod.PUT, entityBody, String.class);
        } catch (HttpClientErrorException e) {
            throw new RequestException(RestMessages.ERROR_4XX.getMessage()+e.getMessage(), e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new RequestException(RestMessages.ERROR_IO.getMessage()+e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }catch (HttpServerErrorException e) {
            throw new RequestException(RestMessages.ERROR_5XX.getMessage()+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> realizarRequisicaoPost(String url, String bodyRequest){
        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity(bodyRequest);

        try {
           return restTemplate.postForEntity(url, entityBody, String.class);
        } catch (HttpClientErrorException e) {
            throw new RequestException(RestMessages.ERROR_4XX.getMessage()+e.getMessage(), e.getStatusCode());
        } catch (ResourceAccessException e) {
            throw new RequestException(RestMessages.ERROR_IO.getMessage()+e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }catch (HttpServerErrorException e) {
            throw new RequestException(RestMessages.ERROR_5XX.getMessage()+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
