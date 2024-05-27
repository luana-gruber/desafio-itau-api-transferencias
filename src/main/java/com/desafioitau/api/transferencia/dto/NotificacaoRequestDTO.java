package com.desafioitau.api.transferencia.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NotificacaoRequestDTO {

    private BigDecimal valor;
    private Conta conta;

    public void atualizarNotificacao(SaldoRequestDTO saldoRequest) {
        this.valor = saldoRequest.getValor();
        this.conta = NotificacaoRequestDTO.Conta.builder()
                .idOrigem(saldoRequest.getConta().getIdOrigem())
                .idDestino(saldoRequest.getConta().getIdDestino())
                .build();
    }

    @Builder
    @Setter
    public static class Conta {
        @JsonProperty("idOrigem")
        private String idOrigem;

        @JsonProperty("idDestino")
        private String idDestino;
    }
}
