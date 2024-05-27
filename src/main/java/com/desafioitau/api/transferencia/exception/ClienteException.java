package com.desafioitau.api.transferencia.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public class ClienteException extends RuntimeException {

    @Getter
    private final HttpStatusCode statusCode;

    public ClienteException(String mensagem, HttpStatusCode statusCode) {
        super(mensagem);
        this.statusCode = statusCode;
    }

}
