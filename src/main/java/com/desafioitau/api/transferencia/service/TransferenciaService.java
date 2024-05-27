package com.desafioitau.api.transferencia.service;

import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface TransferenciaService {
    boolean validarTransferenciaElegivel(TransferenciaRequestDTO transferenciaRequestDTO);
}
