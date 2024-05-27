package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.exception.ClienteException;
import com.desafioitau.api.transferencia.exception.RequestException;
import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClienteServiceImplTest {
    private static final String ID_CLIENTE_ORIGEM = "ID_CLIENTE_ORIGEM";

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private RestClient restClient;

    @Test
    @DisplayName("Deve retornar OK se o cliente for validado com sucesso.")
    void validarSeClienteExiste_shouldReturnOkIfCustomerHaveBeenSuccessfullyValidated(){

        //Arrange
        String expectedJson = "  { \"id\": \"bcdd1048-a501-4608-bc82-66d7b4db3600\", \"nome\": \"João Silva\", \"telefone\": \"912348765\", \"tipoPessoa\": \"Fisica\" }";

        when(restClient.realizarRequisicaoGet(ApiEndpoints.GET_CLIENTE_ID.getUrl(), ID_CLIENTE_ORIGEM)).thenReturn(new ResponseEntity<>(expectedJson,HttpStatus.OK));

        //Act
        ResponseEntity<String> actualResponse = clienteService.validarSeClienteExiste(ID_CLIENTE_ORIGEM);

        //Assert
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(expectedJson, actualResponse.getBody());

    }


    @Test
    @DisplayName("Deve retornar ClienteException se a validação retornar erro porque o serviço API Cadastro retornou erro.")
    void validarSeClienteExiste_shouldReturnOkIfValidationThrowsException(){

        //Arrange
        String expectedExceptionMessage = "Houve um erro de 5xx ao realizar requisição:";

        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(502);

        when(restClient.realizarRequisicaoGet(ApiEndpoints.GET_CLIENTE_ID.getUrl(), ID_CLIENTE_ORIGEM))
                .thenThrow(new RequestException(expectedExceptionMessage, HttpStatus.BAD_GATEWAY));

        //Act & Assert
        ClienteException clienteException = assertThrows(ClienteException.class, () -> clienteService.validarSeClienteExiste(ID_CLIENTE_ORIGEM));

        //Assert
        assertEquals(expectedExceptionMessage, clienteException.getMessage());
        assertEquals(expectedStatusCode, clienteException.getStatusCode());

    }
}
