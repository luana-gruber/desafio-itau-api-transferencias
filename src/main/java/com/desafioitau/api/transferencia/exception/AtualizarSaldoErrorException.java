package com.desafioitau.api.transferencia.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public class AtualizarSaldoErrorException extends RuntimeException {

    @Getter
    private final HttpStatusCode statusCode;


    public AtualizarSaldoErrorException(String mensagem, HttpStatusCode statusCode) {
        super(mensagem);
        this.statusCode = statusCode;
    }
}
