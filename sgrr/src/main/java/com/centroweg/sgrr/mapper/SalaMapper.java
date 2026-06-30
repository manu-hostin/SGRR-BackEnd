package com.centroweg.sgrr.mapper;

import com.centroweg.sgrr.dto.SalaRequestDTO;
import com.centroweg.sgrr.dto.SalaResponseDTO;
import com.centroweg.sgrr.model.Sala;
import org.springframework.stereotype.Component;

@Component
public class SalaMapper {

    public Sala toEntity(SalaRequestDTO dto) {
        Sala sala = new Sala();
        sala.setNome(dto.nome());
        sala.setCapacidade(dto.capacidade());
        sala.setLocalizacao(dto.localizacao());
        return sala;
    }

    public SalaResponseDTO toResponseDTO(Sala sala) {
        return new SalaResponseDTO(
                sala.getId(),
                sala.getNome(),
                sala.getCapacidade(),
                sala.getLocalizacao()
        );
    }

}
