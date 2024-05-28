package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.exception.AtualizarSaldoErrorException;
import com.desafioitau.api.transferencia.exception.RequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SaldoServiceImplTest {

    private static final BigDecimal SALDO_INICIAL = new BigDecimal("200.0");
    private static final BigDecimal VALOR_TRANSFERENCIA = new BigDecimal("50.0");
    private static final BigDecimal SALDO_ESPERADO = new BigDecimal("150.0");
    private static final String ID_CONTA_ORIGEM = "ID_CONTA_ORIGEM";
    private static final String RESPONSE_BODY = "RESPONSE_BODY";

    @InjectMocks
    private SaldoServiceImpl saldoService;

    @Mock
    private RestClient restClient;


    private ContaResponseDTO conta;
    private TransferenciaRequestDTO requestTransferencia;
    private SaldoRequestDTO saldoAtualEsperado;

    @Test
    @DisplayName("Deve retornar Saldo se o saldo foi atualizado com sucesso.")
    void atualizarSaldoContaOrigem_shouldReturnOkIfBalanceUpdateWasSuccessful() {
        // Arrange
        conta = new ContaResponseDTO();
        conta.setSaldo(SALDO_INICIAL);

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);

        requestTransferencia = new TransferenciaRequestDTO();
        requestTransferencia.setConta(contaOrigem);
        requestTransferencia.setValor(VALOR_TRANSFERENCIA);

        saldoAtualEsperado = new SaldoRequestDTO();
        saldoAtualEsperado.setValor(SALDO_ESPERADO);

        when(restClient.realizarRequisicaoPut(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));

        // Act
        SaldoRequestDTO expectedBalance = saldoService.atualizarSaldoContaOrigem(conta, requestTransferencia);

        // Assert
        assertEquals(SALDO_ESPERADO, expectedBalance.getValor());
        assertEquals(ID_CONTA_ORIGEM,
                expectedBalance.getConta().getIdOrigem());
    }

    @Test
    @DisplayName("Deve lançar AtualizarSaldoErrorException se o saldo não foi atualizado com sucesso.")
    void atualizarSaldoContaOrigem_shouldReturnOkIfTheBalanceHasNotBeenUpdated () {
        // Arrange
        conta = new ContaResponseDTO();
        conta.setSaldo(SALDO_INICIAL);

        TransferenciaRequestDTO.Conta contaOrigem = new TransferenciaRequestDTO.Conta();
        contaOrigem.setIdOrigem(ID_CONTA_ORIGEM);

        requestTransferencia = new TransferenciaRequestDTO();
        requestTransferencia.setConta(contaOrigem);
        requestTransferencia.setValor(VALOR_TRANSFERENCIA);

        saldoAtualEsperado = new SaldoRequestDTO();
        saldoAtualEsperado.setValor(SALDO_ESPERADO);

        // Act
        doThrow(new RequestException("Erro na requisição", HttpStatus.BAD_GATEWAY))
                .when(restClient).realizarRequisicaoPut(anyString(), anyString());

        //Assert
        assertThrows(AtualizarSaldoErrorException.class,
                () -> saldoService.atualizarSaldoContaOrigem(conta, requestTransferencia));

    }
}
