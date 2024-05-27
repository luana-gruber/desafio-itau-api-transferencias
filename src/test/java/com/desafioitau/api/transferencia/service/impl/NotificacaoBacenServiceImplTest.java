package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import com.desafioitau.api.transferencia.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.exception.NotificacaoBacenErrorException;
import com.desafioitau.api.transferencia.exception.RequestException;
import com.desafioitau.api.transferencia.service.NotificacaoBacenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class NotificacaoBacenServiceImplTest {

    private static final String RESPONSE_BODY = "RESPONSE_BODY";
    private static final String ID_CONTA_ORIGEM = "ID_CONTA_ORIGEM";

    @InjectMocks
    private NotificacaoBacenServiceImpl notificacaoBacenService;

    @Mock
    private RestClient restClient;

    private SaldoRequestDTO saldoRequestDTO;

    @Mock
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Deve retorna sucesso caso a notificação tenha sido enviada com sucesso.")
    void enviarNotificaoBacen_shouldReturnOkIfTheNotificationIsSuccessful() {
        // Arrange
        SaldoRequestDTO.Conta contaOrigem = SaldoRequestDTO.Conta.builder()
                .idOrigem(ID_CONTA_ORIGEM)
                .build();

        saldoRequestDTO = new SaldoRequestDTO();
        saldoRequestDTO.setValor(new BigDecimal("100.0"));
        saldoRequestDTO.setConta(contaOrigem);

        NotificacaoRequestDTO notificacaoRequestDTO = new NotificacaoRequestDTO();
        notificacaoRequestDTO.atualizarNotificacao(saldoRequestDTO);

        when(restClient.realizarRequisicaoPut(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        // Act
        boolean expectedResponse = notificacaoBacenService.enviarNotificaoBacen(saldoRequestDTO);

        // Assert
        assertTrue(expectedResponse);

    }

    @Test
    @DisplayName("Deve lançar NotificacaoBacenErrorException quando o Bacen retornar erro.")
    void enviarNotificaoBacen_shouldReturnOkIfBacenNotificationReturnError() throws JsonProcessingException {
        // Arrange
        SaldoRequestDTO.Conta contaOrigem = SaldoRequestDTO.Conta.builder()
                .idOrigem(ID_CONTA_ORIGEM)
                .build();

        saldoRequestDTO = new SaldoRequestDTO();
        saldoRequestDTO.setValor(new BigDecimal("100.0"));
        saldoRequestDTO.setConta(contaOrigem);
       NotificacaoRequestDTO notificacaoRequestDTO = new NotificacaoRequestDTO();
       notificacaoRequestDTO.atualizarNotificacao(saldoRequestDTO);



       String json = new ObjectMapper().writeValueAsString(notificacaoRequestDTO);

        // Act & Assert
        when(restClient.realizarRequisicaoPost(ApiEndpoints.POST_BACEN.getUrl(),json )).thenThrow(RequestException.class);

        //Assert
        assertThrows(NotificacaoBacenErrorException.class, () -> notificacaoBacenService.enviarNotificaoBacen(saldoRequestDTO));

    }

}