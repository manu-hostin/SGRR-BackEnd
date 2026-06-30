package com.centroweg.sgrr.mapper;

import com.centroweg.sgrr.dto.EquipamentoRequestDTO;
import com.centroweg.sgrr.dto.EquipamentoResponseDTO;
import com.centroweg.sgrr.model.Equipamento;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {

    public Equipamento toEntity(EquipamentoRequestDTO dto) {
        Equipamento equipamento = new Equipamento();
        equipamento.setNome(dto.nome());
        equipamento.setTipo(dto.tipo());
        equipamento.setPatrimonio(dto.patrimonio());
        return equipamento;
    }

    public EquipamentoResponseDTO toResponseDTO(Equipamento equipamento) {
        return new EquipamentoResponseDTO(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getTipo(),
                equipamento.getPatrimonio()
        );
    }

}
