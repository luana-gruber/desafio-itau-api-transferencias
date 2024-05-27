package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface ContaService {
    ContaResponseDTO validarContaElegivelTransferencia(TransferenciaRequestDTO transferenciaRequestDTO);
}
