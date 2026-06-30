package com.centroweg.sgrr.dto;

import jakarta.validation.constraints.NotBlank;

public record EquipamentoRequestDTO(

        @NotBlank(message = "O nome do equipamento e obrigatorio")
        String nome,

        @NotBlank(message = "O tipo do equipamento e obrigatorio")
        String tipo,

        @NotBlank(message = "O numero de patrimonio e obrigatorio")
        String patrimonio

) {
}
