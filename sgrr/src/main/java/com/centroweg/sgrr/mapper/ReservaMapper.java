package com.centroweg.sgrr.mapper;

import com.centroweg.sgrr.dto.ReservaRequestDTO;
import com.centroweg.sgrr.dto.ReservaResponseDTO;
import com.centroweg.sgrr.model.Equipamento;
import com.centroweg.sgrr.model.Reserva;
import com.centroweg.sgrr.model.Sala;
import com.centroweg.sgrr.model.StatusReserva;
import com.centroweg.sgrr.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper {

    public Reserva toEntity(ReservaRequestDTO dto, Usuario usuario, Sala sala, Equipamento equipamento) {
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setSala(sala);
        reserva.setEquipamento(equipamento);
        reserva.setDataHoraInicio(dto.dataHoraInicio());
        reserva.setDataHoraFim(dto.dataHoraFim());
        reserva.setStatus(StatusReserva.CONFIRMADA);
        return reserva;
    }

    public ReservaResponseDTO toResponseDTO(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getUsuario() != null ? reserva.getUsuario().getId() : null,
                reserva.getUsuario() != null ? reserva.getUsuario().getNome() : null,
                reserva.getSala() != null ? reserva.getSala().getId() : null,
                reserva.getSala() != null ? reserva.getSala().getNome() : null,
                reserva.getEquipamento() != null ? reserva.getEquipamento().getId() : null,
                reserva.getEquipamento() != null ? reserva.getEquipamento().getNome() : null,
                reserva.getDataHoraInicio(),
                reserva.getDataHoraFim(),
                reserva.getStatus()
        );
    }

}
