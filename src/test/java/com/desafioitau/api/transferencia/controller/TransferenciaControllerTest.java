package com.desafioitau.api.transferencia.controller;

import com.desafioitau.api.transferencia.dto.TransferenciaRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.math.BigDecimal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransferenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void shouldReturnOkIfTheIdTrasnferenceniaWasReturned() throws Exception {

        TransferenciaRequestDTO transferenciaRequestDTO = getTransferenciaRequestDTO();

        String requestBody = objectMapper.writeValueAsString(transferenciaRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idTransferencia", Matchers.notNullValue()));


    }

    @Test
    void shouldReturnOkIfTheIdClienteItWasNotFound() throws Exception {

        TransferenciaRequestDTO transferenciaRequestDTO = getTransferenciaRequestDTO();
        transferenciaRequestDTO.setIdCliente("2ceb26e9-7b5c-417e-bf75-ffaa66e3a76g");

        String requestBody = objectMapper.writeValueAsString(transferenciaRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem", Matchers.is("Cliente não encontrado.")));


    }

    @Test
    void shouldReturnOkIfTheIdContaItWasNotFound() throws Exception {
        TransferenciaRequestDTO transferenciaRequestDTO = getTransferenciaRequestDTO();
        transferenciaRequestDTO.getConta().setIdOrigem("d0d32142-74b7-4aca-9c68-838aeacef96f");

        String requestBody = objectMapper.writeValueAsString(transferenciaRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem", Matchers.is("Conta não encontrada.")));

    }

    @Test
    void shouldReturnOkIfTheLimideDiarioIsGreaterThanTheTransferAmount() throws Exception {
        TransferenciaRequestDTO transferenciaRequestDTO = getTransferenciaRequestDTO();
        transferenciaRequestDTO.setValor(new BigDecimal("1000.00"));

        String requestBody = objectMapper.writeValueAsString(transferenciaRequestDTO);

        ResultActions resultActions = mockMvc.perform(post("/transferencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem", Matchers.is("O Limite diário foi atingindo, não é possível realizar a transferência.")));

    }


    private static TransferenciaRequestDTO getTransferenciaRequestDTO() {
        TransferenciaRequestDTO transferenciaRequestDTO = new TransferenciaRequestDTO();
        transferenciaRequestDTO.setValor(new BigDecimal("500.00"));
        transferenciaRequestDTO.setIdCliente("2ceb26e9-7b5c-417e-bf75-ffaa66e3a76f");


        TransferenciaRequestDTO.Conta conta = new TransferenciaRequestDTO.Conta();
        conta.setIdOrigem("d0d32142-74b7-4aca-9c68-838aeacef96b");
        conta.setIdDestino("41313d7b-bd75-4c75-9dea-1f4be434007f");
        transferenciaRequestDTO.setConta(conta);
        return transferenciaRequestDTO;
    }


}
