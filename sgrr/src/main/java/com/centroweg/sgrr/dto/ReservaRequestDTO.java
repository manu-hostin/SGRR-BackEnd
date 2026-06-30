package com.centroweg.sgrr.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservaRequestDTO(

        @NotNull(message = "O usuario e obrigatorio")
        Long usuarioId,

        Long salaId,

        Long equipamentoId,

        @NotNull(message = "A data e hora de inicio sao obrigatorias")
        LocalDateTime dataHoraInicio,

        @NotNull(message = "A data e hora de fim sao obrigatorias")
        LocalDateTime dataHoraFim

) {
}
