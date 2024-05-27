package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.dto.NotificacaoRequestDTO;
import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.exception.NotificacaoBacenErrorException;
import com.desafioitau.api.transferencia.exception.RequestException;
import com.desafioitau.api.transferencia.service.NotificacaoBacenService;
import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacaoBacenServiceImpl implements NotificacaoBacenService {

    private final RestClient restClient;

    ObjectMapper objectMapper = new ObjectMapper();

    public NotificacaoBacenServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public boolean enviarNotificaoBacen(SaldoRequestDTO requestSaldo) {
        NotificacaoRequestDTO notificacaoRequestDTO = prepararNotificacao(requestSaldo);
        notificarBacen(notificacaoRequestDTO);
        return true;
    }

    private NotificacaoRequestDTO prepararNotificacao(SaldoRequestDTO requestSaldo) {
        NotificacaoRequestDTO notificacaoRequestDTO = new NotificacaoRequestDTO();
        notificacaoRequestDTO.atualizarNotificacao(requestSaldo);
        return notificacaoRequestDTO;
    }

    @Retryable(retryFor= { RequestException.class },backoff = @Backoff(delay = 5000))
    private void notificarBacen(NotificacaoRequestDTO requestNotificacao) {
        log.info("Enviando notificação ao Bacen.");
        String url = ApiEndpoints.POST_BACEN.getUrl();
        try {
            String requestBody = objectMapper.writeValueAsString(requestNotificacao);
            restClient.realizarRequisicaoPost(url, requestBody);
        } catch (RequestException e) {
            throw new NotificacaoBacenErrorException("Não foi possível notificar o Bacen no momento.", e.getStatusCode());
        } catch (JsonProcessingException e) {
            throw new NotificacaoBacenErrorException("Houve um erro ao parsear o objeto de entrada para JSON.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
