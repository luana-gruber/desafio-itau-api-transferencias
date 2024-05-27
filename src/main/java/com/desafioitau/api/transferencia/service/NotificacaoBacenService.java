package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface NotificacaoBacenService {
    boolean enviarNotificaoBacen(SaldoRequestDTO requestSaldo);
}
