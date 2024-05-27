package com.desafioitau.api.transferencia.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class TransferenciaResponseDTO {

    private UUID idTransferencia;
}
