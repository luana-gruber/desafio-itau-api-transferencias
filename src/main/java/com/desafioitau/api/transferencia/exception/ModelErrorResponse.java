package com.desafioitau.api.transferencia.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ModelErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String mensagem;
    private String path;
}


