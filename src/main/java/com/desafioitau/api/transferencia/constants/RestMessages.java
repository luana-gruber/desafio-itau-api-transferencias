package com.desafioitau.api.transferencia.constants;

import lombok.Getter;

@Getter
public enum RestMessages {

    ERROR_4XX("Houve um erro 4xx ao realizar requisição: "),
    ERROR_IO("Houve um erro de I/O ao realizar requisição: "),
    ERROR_5XX("Houve um erro de 5xx ao realizar requisição: ");


    RestMessages(String message){
        this.message = message;
    }

    private final String message;

}
