package com.desafioitau.api.transferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class SaldoRequestDTO {

    private BigDecimal valor;

    private  Conta conta;

    public void deduzirValor(BigDecimal saldoContaOrigem,  BigDecimal valorTransferido) {
        this.valor = saldoContaOrigem.subtract(valorTransferido);
    }

    public void preencherDadosConta(TransferenciaRequestDTO.Conta requestDadosConta) {

        this.conta = Conta.builder()
                .idOrigem(requestDadosConta.getIdOrigem())
                .idDestino(requestDadosConta.getIdDestino())
                .build();
    }


    @Setter
    @Getter
    @Builder
    public static class Conta {
        @JsonProperty("idOrigem")
        private String idOrigem;

        @JsonProperty("idDestino")
        private String idDestino;
    }
}
