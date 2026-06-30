package com.centroweg.sgrr.dto;

import com.centroweg.sgrr.model.StatusReserva;

import java.time.LocalDateTime;

public record ReservaResponseDTO(

        Long id,
        Long usuarioId,
        String usuarioNome,
        Long salaId,
        String salaNome,
        Long equipamentoId,
        String equipamentoNome,
        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        StatusReserva status

) {
}
