package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.exception.*;
import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import static org.mockito.ArgumentMatchers.eq;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContaServiceImplTest {
    private static final String ID_CONTA_ORIGEM = "ID_CONTA_ORIGEM";
    private static final String RESPONSE_BODY = "RESPONSE_BODY";

    @InjectMocks
    private ContaServiceImpl contaService;

    @Mock
    private RestClient restClient;

    @Mock
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Deve retornar ContaResponseDTO se a conta for elegível para transferência")
    void validarContaElegivelTransferencia_shouldOkIfTheAccountIsEligibleForTransfer() throws Exception {
        // Arrange
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();
        transferenciaRequestDTO.setValor(new BigDecimal("500.00"));

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);
        transferenciaRequestDTO.setConta(contaOrigem);

        ContaResponseDTO contaResponseDTO = new ContaResponseDTO();
        contaResponseDTO.setAtivo(true);
        contaResponseDTO.setSaldo(new BigDecimal("1000.00"));
        contaResponseDTO.setLimiteDiario(new BigDecimal("1000.00"));

        when(restClient.realizarRequisicaoGet(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(ContaResponseDTO.class))).thenReturn(contaResponseDTO);

        // Act
        ContaResponseDTO expectedResponse = contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);

        // Assert
        assertEquals(contaResponseDTO, expectedResponse);
    }

    @Test
    @DisplayName("Deve lançar ContaNaoEncontradaException se conta de origem não for encontrada")
    void validarContaElegivelTransferencia_shouldOkIfContaOrigemNotFound() {
        // Arrange
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);
        transferenciaRequestDTO.setConta(contaOrigem);

        String expectedExceptionMessage = "Houve um erro de 5xx ao realizar requisição:";
        HttpStatusCode expectedStatusCode = HttpStatusCode.valueOf(502);

        when(restClient.realizarRequisicaoGet(ApiEndpoints.GET_CONTA_ID.getUrl(), ID_CONTA_ORIGEM))
                .thenThrow(new RequestException(expectedExceptionMessage, HttpStatus.BAD_GATEWAY));

        // Act & Assert
        ContaNaoEncontradaException contaNaoEncontradaException = assertThrows(ContaNaoEncontradaException.class, () -> {
            contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);
        });

        // Assert
        assertEquals(expectedExceptionMessage, contaNaoEncontradaException .getMessage());
        assertEquals(expectedStatusCode, contaNaoEncontradaException .getStatusCode());
    }

    @Test
    @DisplayName("Deve lançar RequestException se houve um erro ao converter JSON")
    void validarContaElegivelTransferencia_shouldOkIfJsonDidntConvertCorrectly() throws Exception {
        // Arrange
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);
        transferenciaRequestDTO.setConta(contaOrigem);

        String invalidJson = "JSON invalido";
        String expectedExceptionMessage = "Houve um erro ao processar o JSON recebido.";

        ResponseEntity<String> mockResponseEntity = mock(ResponseEntity.class);

        when(mockResponseEntity.getBody()).thenReturn(invalidJson);
        when(restClient.realizarRequisicaoGet(ApiEndpoints.GET_CONTA_ID.getUrl(),transferenciaRequestDTO.getConta().getIdOrigem())).thenReturn(mockResponseEntity);

        // Act & Assert
        when(objectMapper.readValue(invalidJson, ContaResponseDTO.class)).thenThrow(JsonProcessingException.class);
        RequestException requestException = assertThrows(RequestException.class, () -> {
            contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);
        });

        // Assert
        assertEquals(expectedExceptionMessage, requestException.getMessage());
    }


    @Test
    @DisplayName("Deve lançar ContaDesativadaException se conta não estiver ativa")
    void validarContaElegivelTransferencia_shouldOkIfContaDisable() throws Exception {
        // Arrange
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);
        transferenciaRequestDTO.setConta(contaOrigem);

        ContaResponseDTO contaResponseDTO = new ContaResponseDTO();
        contaResponseDTO.setAtivo(false);

        String expectedExceptionMessage = "Conta corrente não está ativa.";

        when(restClient.realizarRequisicaoGet(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(ContaResponseDTO.class))).thenReturn(contaResponseDTO);

        // Act & Assert
        ContaDesativadaException contaDesativadaException = assertThrows(ContaDesativadaException.class, () -> {
            contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);
        });

        // Assert
        assertEquals(expectedExceptionMessage, contaDesativadaException.getMessage());
    }

    @Test
    @DisplayName("Deve lançar SaldoInsuficienteException se a conta tiver o saldo menor que o valor a ser transferido")
    void validarContaElegivelTransferencia_shouldOkIfBalanceIsInsufficient() throws Exception {
        // Arrange
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();
        transferenciaRequestDTO.setValor(new BigDecimal("5000.00"));

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);
        transferenciaRequestDTO.setConta(contaOrigem);

        ContaResponseDTO contaResponseDTO = new ContaResponseDTO();
        contaResponseDTO.setAtivo(true);
        contaResponseDTO.setSaldo(new BigDecimal("1000.00"));

        String expectedExceptionMessage = "Saldo insuficiente na conta de origem para realizar a transferência.";

        when(restClient.realizarRequisicaoGet(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(ContaResponseDTO.class))).thenReturn(contaResponseDTO);

        // Act & Assert
        SaldoInsuficienteException saldoInsuficienteException = assertThrows(SaldoInsuficienteException.class, () -> {
            contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);
        });

        // Assert
        assertEquals(expectedExceptionMessage, saldoInsuficienteException.getMessage());
    }

    @Test
    @DisplayName("Deve lançar LimiteDiarioInsuficienteException se o valor transferido execer o limite diário")
    void validarContaElegivelTransferencia_shouldOkIfValueIsGreaterThanDailyLimit() throws Exception {
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();
        transferenciaRequestDTO.setValor(new BigDecimal("900.00"));

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);
        transferenciaRequestDTO.setConta(contaOrigem);

        ContaResponseDTO contaResponseDTO = new ContaResponseDTO();
        contaResponseDTO.setAtivo(true);
        contaResponseDTO.setSaldo(new BigDecimal("1000.00"));
        contaResponseDTO.setLimiteDiario(new BigDecimal("500.00"));

        String expectedExceptionMessage = "O Limite diário foi atingindo, não é possível realizar a transferência.";

        when(restClient.realizarRequisicaoGet(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(ContaResponseDTO.class))).thenReturn(contaResponseDTO);

        // Act & Assert
        LimiteDiarioInsuficienteException saldoInsuficienteException = assertThrows(LimiteDiarioInsuficienteException.class, () -> {
            contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);
        });

        // Assert
        assertEquals(expectedExceptionMessage, saldoInsuficienteException.getMessage());
    }

    @Test
    @DisplayName("Deve lançar LimiteDiarioInsuficienteException se limite diário for 0")
    void validarContaElegivelTransferencia_shouldOkIfDailyLimitIsZero() throws Exception {
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();
        transferenciaRequestDTO.setValor(new BigDecimal("900.00"));

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);
        transferenciaRequestDTO.setConta(contaOrigem);

        ContaResponseDTO contaResponseDTO = new ContaResponseDTO();
        contaResponseDTO.setAtivo(true);
        contaResponseDTO.setSaldo(new BigDecimal("1000.00"));
        contaResponseDTO.setLimiteDiario(new BigDecimal("0.0"));

        String expectedExceptionMessage = "O Limite diário foi atingindo, não é possível realizar a transferência.";

        when(restClient.realizarRequisicaoGet(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));
        when(objectMapper.readValue(anyString(), eq(ContaResponseDTO.class))).thenReturn(contaResponseDTO);

        // Act & Assert
        LimiteDiarioInsuficienteException saldoInsuficienteException = assertThrows(LimiteDiarioInsuficienteException.class, () -> {
            contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);
        });

        // Assert
        assertEquals(expectedExceptionMessage, saldoInsuficienteException.getMessage());
    }

}

