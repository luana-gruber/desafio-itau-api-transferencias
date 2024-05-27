package com.desafioitau.api.transferencia.service.impl;

import com.desafioitau.api.transferencia.client.RestClient;
import com.desafioitau.api.transferencia.dto.ContaResponseDTO;
import com.desafioitau.api.transferencia.dto.SaldoRequestDTO;
import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.desafioitau.api.transferencia.exception.AtualizarSaldoErrorException;
import com.desafioitau.api.transferencia.exception.RequestException;
import com.desafioitau.api.transferencia.service.SaldoService;
import com.desafioitau.api.transferencia.constants.ApiEndpoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Slf4j
public class SaldoServiceImpl implements SaldoService {

    private final RestClient restClient;

    public SaldoServiceImpl(RestClient restClient) {
        this.restClient = restClient;

    }

    @Override
    public SaldoRequestDTO atualizarSaldoContaOrigem(ContaResponseDTO contaOrigem, TransferenciaRequestDTO requestTransferencia) {
        SaldoRequestDTO saldoAtual = deduzirSaldoContaOrigem(contaOrigem, requestTransferencia);
        enviarSaldoContaOrigem(saldoAtual);
        return saldoAtual;
    }

    private SaldoRequestDTO deduzirSaldoContaOrigem(ContaResponseDTO contaOrigem, TransferenciaRequestDTO requestTransferencia) {
        log.info("Iniciando dedução do saldo da conta.");

        SaldoRequestDTO saldoRequestDTO = new SaldoRequestDTO();
        saldoRequestDTO.preencherDadosConta(requestTransferencia.getConta());
        saldoRequestDTO.deduzirValor(contaOrigem.getSaldo(), requestTransferencia.getValor());
        return saldoRequestDTO;
    }


    private void enviarSaldoContaOrigem(SaldoRequestDTO saldoRequestDTO) {
        log.info("Iniciando envio de saldo atualizado.");
        String url = ApiEndpoints.PUT_ATUALIZAR_SALDO.getUrl();
        try {
            restClient.realizarRequisicaoPut(url, saldoRequestDTO.toString());
        } catch (RequestException e) {
            throw new AtualizarSaldoErrorException(e.getMessage(), e.getStatusCode());
        }
    }

}
