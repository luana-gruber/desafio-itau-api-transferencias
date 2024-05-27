package com.desafioitau.api.transferencia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

public class BuilderModelErrorResponse {
    private BuilderModelErrorResponse() {}

    public static ModelErrorResponse constuirRespostaErro(HttpStatus status, String mensagem, WebRequest request) {
        return ModelErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .mensagem(mensagem)
                .path(request.getDescription(false))
                .build();
    }
}
