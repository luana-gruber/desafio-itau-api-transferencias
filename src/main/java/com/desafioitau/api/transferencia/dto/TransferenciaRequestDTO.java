package com.desafioitau.api.transferencia.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferenciaRequestDTO {

    private String idCliente;
    private BigDecimal valor;
    private Conta conta;

    @Getter
    @Setter
    public static class Conta {
        private String idOrigem;
        private String idDestino;
    }
}
