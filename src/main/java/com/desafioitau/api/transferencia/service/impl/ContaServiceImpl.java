package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.exception.*;
import com.desafioitau.api.transferencia.service.ContaService;
import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class ContaServiceImpl implements ContaService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ContaServiceImpl(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public ContaResponseDTO validarContaElegivelTransferencia(TransferenciaRequestDTO transferenciaRequestDTO) {
        log.info("Iniciando validações para verificar se conta é elegível para transferência");
        ContaResponseDTO contaOrigem = buscarConta(transferenciaRequestDTO.getConta().getIdOrigem());
        validarSeContaOrigemEstaAtiva(contaOrigem);
        validarSeExisteSaldoDisponivelContaOrigem(contaOrigem, transferenciaRequestDTO.getValor());
        validarSeValorTransferenciaEstaNoLimiteDiarioDisponivel(contaOrigem, transferenciaRequestDTO.getValor());

        return contaOrigem;
    }

    private ContaResponseDTO buscarConta(String idConta) {
        log.info("Iniciando validação para verificar se conta origem existe");
        String url = ApiEndpoints.GET_CONTA_ID.getUrl();
        try {
            String responseBody = restClient.realizarRequisicaoGet(url, idConta).getBody();
            return objectMapper.readValue(responseBody, ContaResponseDTO.class);
        } catch (RequestException e) {
            throw new ContaNaoEncontradaException(e.getMessage(), e.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new RequestException("Houve um erro ao processar o JSON recebido.", HttpStatusCode.valueOf(400));
        }
    }


    private void validarSeContaOrigemEstaAtiva(ContaResponseDTO contaOrigem) {
        log.info("Iniciando validação para verificar se conta conta origem está ativa");
        if (!contaOrigem.isAtivo()) {
            throw new ContaDesativadaException("Conta corrente não está ativa.");
        }
    }


    private void validarSeExisteSaldoDisponivelContaOrigem(ContaResponseDTO contaOrigem, BigDecimal valorTransferencia) {
        log.info("Iniciando validação para verificar se há saldo disponível");
        if (contaOrigem.getSaldo().compareTo(valorTransferencia) <= 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem para realizar a transferência.");
        }
    }


    private void validarSeValorTransferenciaEstaNoLimiteDiarioDisponivel(ContaResponseDTO contaOrigem, BigDecimal valorTransferencia) {
        log.info("Iniciando validação para verificar se o limite diário foi atingido");

        if (contaOrigem.getLimiteDiario().compareTo(BigDecimal.ZERO) <= 0 || valorTransferencia.compareTo(contaOrigem.getLimiteDiario()) > 0) {
            throw new LimiteDiarioInsuficienteException("O Limite diário foi atingindo, não é possível realizar a transferência.");
        }
    }
}
