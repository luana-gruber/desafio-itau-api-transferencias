package com.desafioitau.api.transferencia.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public class ContaNaoEncontradaException extends RuntimeException {

    @Getter
    private final HttpStatusCode statusCode;


    public ContaNaoEncontradaException(String mensagem, HttpStatusCode statusCode) {
        super(mensagem);
        this.statusCode = statusCode;
    }

}