
package com.desafioitau.api.transferencia.client;

import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import com.desafioitau.api.transferencia.exception.RequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class RestClientTest {
    private static final String RESPONSE_BODY = "RESPONSE_BODY";
    private static final String URL_CONTA = ApiEndpoints.GET_CONTA_ID.getUrl();
    private static final String URL_ATUALIZAR_SALDO = ApiEndpoints.PUT_ATUALIZAR_SALDO.getUrl();
    private static final String URL_NOTIFICACAO_BACEN = ApiEndpoints.POST_BACEN.getUrl();
    private static final String ID = "ID";

    @InjectMocks
    private RestClient restClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpHeadersBuilder httpHeadersBuilder;

    @Test
    @DisplayName("Deve retornar Http Status OK se requisição GET retornar com sucesso.")
    void realizarRequisicaoGet_shouldReturnOkIfGetRequestReturnsSuccess() {
        // Arrange
        ResponseEntity<String> responseEntity = new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> headers = new HttpEntity<>(null, httpHeaders);

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(headers);
        when(restTemplate.exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID)).thenReturn(responseEntity);

        // Act
        ResponseEntity<String> expectedResponse = restClient.realizarRequisicaoGet(URL_CONTA, ID);

        //Assert
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());
        assertEquals(RESPONSE_BODY, expectedResponse.getBody());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID);
    }

    @Test
    @DisplayName("Deve retornar Http status 400 se requisição GET retornar erro 4xx.")
    void realizarRequisicaoGet_shouldReturnOkIfGetRequestReturnsHttpStatus400() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro 4xx ao realizar requisição: 400 BAD_REQUEST";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(400);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> headers = new HttpEntity<>(null, httpHeaders);

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(headers);
        when(restTemplate.exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoGet(URL_CONTA, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID);
    }

    @Test
    @DisplayName("Deve retornar Http status 503 se requisição GET retornar erro I/O.")
    void realizarRequisicaoGet_shouldReturnOkIfGetRequestReturnsHttpStatusIO() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro de I/O ao realizar requisição: 503 SERVICE_UNAVAILABLE";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(503);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> headers = new HttpEntity<>(null, httpHeaders);

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(headers);
        when(restTemplate.exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID)).thenThrow(new ResourceAccessException("503 SERVICE_UNAVAILABLE"));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoGet(URL_CONTA, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID);
    }

    @Test
    @DisplayName("Deve retornar Http status 500 se requisição GET retornar erro 5xx.")
    void realizarRequisicaoGet_shouldReturnOkIfGetRequestReturnsHttpStatus500() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro de 5xx ao realizar requisição: 500 INTERNAL_SERVER_ERROR";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(500);
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<String> headers = new HttpEntity<>(null, httpHeaders);

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(headers);
        when(restTemplate.exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoGet(URL_CONTA, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_CONTA, HttpMethod.GET, headers, String.class, ID);
    }

    @Test
    @DisplayName("Deve retornar Http Status OK se requisição PUT retornar com sucesso.")
    void realizarRequisicaoGet_shouldReturnOkIfPutRequestReturnsHttpStatus200() {
        // Arrange
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(200);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK);

        when(restTemplate.exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, entityBody, String.class))
                .thenReturn(responseEntity);

        //Act
        ResponseEntity<String> expectedResponse = restClient.realizarRequisicaoPut(URL_ATUALIZAR_SALDO, RESPONSE_BODY);

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(RESPONSE_BODY, expectedResponse.getBody());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, null, String.class);
    }

    @Test
    @DisplayName("Deve retornar Http status 400 se requisição PUT retornar erro 4xx.")
    void realizarRequisicaoGet_shouldReturnOkIfPutRequestReturnsHttpStatus400() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro 4xx ao realizar requisição: 400 BAD_REQUEST";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(400);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        when(restTemplate.exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, entityBody, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoPut(URL_ATUALIZAR_SALDO, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, null, String.class);
    }

    @Test
    @DisplayName("Deve retornar Http status 500 se requisição PUT retornar erro 5xx.")
    void realizarRequisicaoGet_shouldReturnOkIfPutRequestReturnsHttpStatus500() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro de 5xx ao realizar requisição: 500 INTERNAL_SERVER_ERROR";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(500);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        when(restTemplate.exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, entityBody, String.class))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoPut(URL_ATUALIZAR_SALDO, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, null, String.class);
    }

    @Test
    @DisplayName("Deve retornar Http status 503 se requisição PUT retornar erro I/O.")
    void realizarRequisicaoGet_shouldReturnOkIfPutRequestReturnsHttpStatus503() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro de I/O ao realizar requisição: 503 SERVICE_UNAVAILABLE";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(503);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        when(restTemplate.exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, entityBody, String.class))
                .thenThrow(new ResourceAccessException("503 SERVICE_UNAVAILABLE"));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoPut(URL_ATUALIZAR_SALDO, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).exchange(URL_ATUALIZAR_SALDO, HttpMethod.PUT, null, String.class);
    }

    @Test
    @DisplayName("Deve retornar Http Status OK se requisição POST retornar com sucesso.")
    void realizarRequisicaoGet_shouldReturnOkIfPostRequestReturnsHttpStatus200() {
        // Arrange
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(200);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK);

        when(restTemplate.postForEntity(URL_NOTIFICACAO_BACEN, entityBody, String.class))
                .thenReturn(responseEntity);

        //Act
        ResponseEntity<String> expectedResponse = restClient.realizarRequisicaoPost(URL_NOTIFICACAO_BACEN, RESPONSE_BODY);

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(RESPONSE_BODY, expectedResponse.getBody());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).postForEntity(URL_NOTIFICACAO_BACEN, null, String.class);
    }

    @Test
    @DisplayName("Deve retornar Http status 400 se requisição POST retornar erro 4xx.")
    void realizarRequisicaoGet_shouldReturnOkIfPostRequestReturnsHttpStatus400() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro 4xx ao realizar requisição: 400 BAD_REQUEST";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(400);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        when(restTemplate.postForEntity(URL_NOTIFICACAO_BACEN, entityBody, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoPost(URL_NOTIFICACAO_BACEN, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).postForEntity(URL_NOTIFICACAO_BACEN, null, String.class);
    }

    @Test
    @DisplayName("Deve retornar Http status 500 se requisição POST retornar erro 5xx.")
    void realizarRequisicaoGet_shouldReturnOkIfPostRequestReturnsHttpStatus500() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro de 5xx ao realizar requisição: 500 INTERNAL_SERVER_ERROR";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(500);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        when(restTemplate.postForEntity(URL_NOTIFICACAO_BACEN, entityBody, String.class))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoPost(URL_NOTIFICACAO_BACEN, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).postForEntity(URL_NOTIFICACAO_BACEN, null, String.class);
    }

    @Test
    @DisplayName("Deve retornar Http status 503 se requisição PUT retornar erro I/O.")
    void realizarRequisicaoGet_shouldReturnOkIfPostRequestReturnsHttpStatus503() {
        // Arrange
        String expectedExceptionMessage = "Houve um erro de I/O ao realizar requisição: 503 SERVICE_UNAVAILABLE";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(503);

        HttpEntity<String> entityBody = httpHeadersBuilder.buildHttpEntity();

        when(httpHeadersBuilder.buildHttpEntity()).thenReturn(entityBody);

        when(restTemplate.postForEntity(URL_NOTIFICACAO_BACEN, entityBody, String.class))
                .thenThrow(new ResourceAccessException("503 SERVICE_UNAVAILABLE"));

        //Act & Assert
        RequestException expectedResponse = assertThrows(RequestException.class, () -> restClient.realizarRequisicaoPost(URL_NOTIFICACAO_BACEN, ID));

        //Assert
        assertEquals(expectedStatusCode, expectedResponse.getStatusCode());
        assertEquals(expectedExceptionMessage, expectedResponse.getMessage());

        verify(httpHeadersBuilder).buildHttpEntity();
        verify(restTemplate).postForEntity(URL_NOTIFICACAO_BACEN, null, String.class);
    }
}
