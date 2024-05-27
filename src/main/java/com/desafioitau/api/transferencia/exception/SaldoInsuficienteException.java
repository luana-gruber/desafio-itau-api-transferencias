package com.desafioitau.api.transferencia.exception;

public class SaldoInsuficienteException extends RuntimeException{
    public SaldoInsuficienteException(String mensagem) { super(mensagem);}
}