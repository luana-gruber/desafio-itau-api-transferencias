package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.exception.ClienteException;
import com.desafioitau.api.transferencia.exception.RequestException;
import com.desafioitau.api.transferencia.service.ClienteService;
import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final RestClient restClient;

    public ClienteServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public ResponseEntity<String> validarSeClienteExiste(String idCliente) {
        log.info("Iniciando validação para verificar se cliente existe");

        String url = ApiEndpoints.GET_CLIENTE_ID.getUrl();

        try{
            return restClient.realizarRequisicaoGet(url,idCliente);
        }catch(RequestException e){
            throw new ClienteException(e.getMessage(), e.getStatusCode());
        }

    }
}
