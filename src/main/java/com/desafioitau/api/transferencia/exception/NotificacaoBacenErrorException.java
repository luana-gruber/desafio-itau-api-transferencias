package com.desafioitau.api.transferencia.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

public class NotificacaoBacenErrorException extends RuntimeException{
    @Getter
    private final HttpStatusCode statusCode;


    public NotificacaoBacenErrorException(String mensagem, HttpStatusCode statusCode) {
        super(mensagem);
        this.statusCode = statusCode;
    }

}

