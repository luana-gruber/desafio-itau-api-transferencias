package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TransferenciaServiceImpl implements TransferenciaService {
    private final ClienteService clienteService;
    private final ContaService contaService;
    private final SaldoService saldoService;
    private final NotificacaoBacenService notificacaoBacenService;

    public TransferenciaServiceImpl(ClienteService clienteService, ContaService contaService, SaldoService saldoService, NotificacaoBacenService notificacaoBacenService) {
        this.clienteService = clienteService;
        this.contaService = contaService;
        this.saldoService = saldoService;
        this.notificacaoBacenService = notificacaoBacenService;
    }

    @Override
    public boolean validarTransferenciaElegivel(TransferenciaRequestDTO transferenciaRequestDTO) {
        clienteService.validarSeClienteExiste(transferenciaRequestDTO.getIdCliente());
        ContaResponseDTO contaOrigem = contaService.validarContaElegivelTransferencia(transferenciaRequestDTO);
        SaldoRequestDTO saldoRequestDTO = saldoService.atualizarSaldoContaOrigem(contaOrigem, transferenciaRequestDTO);
        notificacaoBacenService.enviarNotificaoBacen(saldoRequestDTO);
        return true;
    }
}



