package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.service.ClienteService;
import com.desafioitau.api.transferencia.service.ContaService;
import com.desafioitau.api.transferencia.service.NotificacaoBacenService;
import com.desafioitau.api.transferencia.service.SaldoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TransferenciaServiceImplTest {
    
    private static final String ID_CLIENTE_ORIGEM = "ID_CLIENTE_ORIGEM";
    private static final String RESPONSE_BODY = "RESPONSE_BODY";
    
    @Mock
    private ClienteService clienteService;

    @Mock
    private ContaService contaService;

    @Mock
    private SaldoService saldoService;

    @Mock
    private NotificacaoBacenService notificacaoBacenService;

    @InjectMocks
    private TransferenciaServiceImpl transferenciaService;


    @Test
    @DisplayName("Deve sucesso caso a tranferência seja elegível.")
    void validarTransferenciaElegivel_shouldReturnOkIfTheTransferIsEligibleIsSuccessful() {
        //Arrange
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();
        transferenciaRequestDTO.setIdCliente(ID_CLIENTE_ORIGEM);
        ContaResponseDTO contaResponseDTO = new ContaResponseDTO();
        SaldoRequestDTO saldoRequestDTO = new SaldoRequestDTO();
        saldoRequestDTO.setValor(new BigDecimal("150.0"));

        when(clienteService.validarSeClienteExiste(ID_CLIENTE_ORIGEM)).thenReturn(new ResponseEntity<>(RESPONSE_BODY, HttpStatus.OK));
        
        when(contaService.validarContaElegivelTransferencia(transferenciaRequestDTO)).thenReturn(contaResponseDTO);
        
        when(saldoService.atualizarSaldoContaOrigem(contaResponseDTO, transferenciaRequestDTO)).thenReturn(saldoRequestDTO);

        when(notificacaoBacenService.enviarNotificaoBacen(saldoRequestDTO)).thenReturn(true);

        //Act
        boolean expectedResponse = transferenciaService.validarTransferenciaElegivel(transferenciaRequestDTO);

        //Assert
        assertTrue(expectedResponse);
        verify(clienteService).validarSeClienteExiste(ID_CLIENTE_ORIGEM);
        verify(contaService).validarContaElegivelTransferencia(transferenciaRequestDTO);
        verify(saldoService).atualizarSaldoContaOrigem(contaResponseDTO, transferenciaRequestDTO);
        verify(notificacaoBacenService).enviarNotificaoBacen(saldoRequestDTO);
    }
}
