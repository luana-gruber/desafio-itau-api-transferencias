package com.desafioitau.api.transferencia.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ClienteService {
    ResponseEntity<String> validarSeClienteExiste(String idCliente);
}
