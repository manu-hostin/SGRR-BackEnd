package com.centroweg.sgrr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SalaRequestDTO(

        @NotBlank(message = "O nome da sala e obrigatorio")
        String nome,

        @NotNull(message = "A capacidade e obrigatoria")
        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacidade,

        @NotBlank(message = "A localizacao e obrigatoria")
        String localizacao

) {
}
