package com.desafioitau.api.transferencia.constants;

import lombok.Getter;

@Getter
public enum ApiEndpoints {

    GET_CLIENTE_ID("http://localhost:9090/clientes/{id}"),
    GET_CONTA_ID("http://localhost:9090/contas/{id}"),
    POST_BACEN("http://localhost:9090/notificacoes"),
    PUT_ATUALIZAR_SALDO("http://localhost:9090/contas/saldos");

    private final String url;

    ApiEndpoints(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return this.url;
    }

}
