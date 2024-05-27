package com.desafioitau.api.transferencia.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class Handler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(ClienteException.class)
    public ResponseEntity<ModelErrorResponse> tratamentoClienteException(ClienteException exception, WebRequest request) {

        String mensagem = exception.getMessage();

        if(exception.getStatusCode().value() == HttpStatus.NOT_FOUND.value()){
            mensagem = "Cliente não encontrado.";
        }

        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.valueOf(exception.getStatusCode().value()),
                mensagem,
                request);
        log.error("Houve um problema ao buscar o cliente na API de clientes {}", exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(tratamentoResposta);
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<ModelErrorResponse> tratamentoContaNaoEncontradaException(ContaNaoEncontradaException exception, WebRequest request) {

        String mensagem = exception.getMessage();

        if(exception.getStatusCode().value() == HttpStatus.NOT_FOUND.value()){
            mensagem = "Conta não encontrada.";
        }

        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.valueOf(exception.getStatusCode().value()),
                mensagem,
                request);
        log.error("Houve um problema ao buscar a conta na API de contas {}", exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(tratamentoResposta);
    }

    @ExceptionHandler(ContaDesativadaException.class)
    public ResponseEntity<ModelErrorResponse> tratamentoContaDesativadaException(ContaDesativadaException exception, WebRequest request) {
        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request);
        log.error("Conta do cliente não está com status de ativa.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tratamentoResposta);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ModelErrorResponse> tratamentoSaldoInsuficienteException(SaldoInsuficienteException exception, WebRequest request) {
        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request);
        log.error("Cliente não possui saldo suficiente para realizar a transação");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tratamentoResposta);
    }

    @ExceptionHandler(LimiteDiarioInsuficienteException.class)
    public ResponseEntity<ModelErrorResponse> tratamentoLimiteDiarioInsuficienteException(LimiteDiarioInsuficienteException exception, WebRequest request) {
        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request);
        log.error("Limite diário do cliente foi atingido, transferência não realizada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(tratamentoResposta);
    }

    @ExceptionHandler(AtualizarSaldoErrorException.class)
    public ResponseEntity<ModelErrorResponse> tratamentoAtualizarSaldoErrorException(AtualizarSaldoErrorException exception, WebRequest request) {
        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.valueOf(exception.getStatusCode().value()),
                exception.getMessage(),
                request);
        log.error("API de Contas não conseguiu atualizar o saldo");
        return ResponseEntity.status(exception.getStatusCode().value()).body(tratamentoResposta);
    }

    @ExceptionHandler(NotificacaoBacenErrorException.class)
    public ResponseEntity<ModelErrorResponse> tratamentoNotificacaoBacenErrorException(NotificacaoBacenErrorException exception, WebRequest request) {
        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.TOO_MANY_REQUESTS,
                exception.getMessage(),
                request);
        log.error("API do Bacen atingiu Rate Limit {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(tratamentoResposta);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ModelErrorResponse> requestException(RequestException exception, WebRequest request) {
        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.valueOf(exception.getStatusCode().value()),
                exception.getMessage(),
                request);
        log.error("Erro ao chamar API{}", exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode().value()).body(tratamentoResposta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ModelErrorResponse> handleGenericException(Exception exception, WebRequest request) {
        ModelErrorResponse tratamentoResposta = BuilderModelErrorResponse.constuirRespostaErro(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor.",
                request);
        log.error("Erro interno da aplicação oriundo da classe Exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(tratamentoResposta);
    }
}
