package com.desafioitau.api.transferencia.controller;

import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaResponseDTO;
import com.desafioitau.api.transferencia.service.TransferenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping("/transferencias")
    public ResponseEntity<TransferenciaResponseDTO> efetuarTransferencia(@RequestBody TransferenciaRequestDTO transferenciaRequestDTO) {
            transferenciaService.validarTransferenciaElegivel(transferenciaRequestDTO);

            UUID uuid = UUID.randomUUID();

            TransferenciaResponseDTO transferenciaResponseDTO = TransferenciaResponseDTO.builder()
                    .idTransferencia(uuid)
                    .build();
            return ResponseEntity.ok().body(transferenciaResponseDTO);
    }
}
