package com.centroweg.sgrr.service;

import com.centroweg.sgrr.dto.SalaRequestDTO;
import com.centroweg.sgrr.dto.SalaResponseDTO;
import com.centroweg.sgrr.mapper.SalaMapper;
import com.centroweg.sgrr.model.Sala;
import com.centroweg.sgrr.repository.SalaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SalaService {

    private final SalaRepository salaRepository;
    private final SalaMapper salaMapper;

    public SalaResponseDTO salvar(SalaRequestDTO dto) {
        Sala sala = salaMapper.toEntity(dto);
        Sala salaSalva = salaRepository.save(sala);
        return salaMapper.toResponseDTO(salaSalva);
    }

    public List<SalaResponseDTO> listarTodos() {
        return salaRepository.findAll()
                .stream()
                .map(salaMapper::toResponseDTO)
                .toList();
    }

    public SalaResponseDTO buscarPorId(Long id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala nao encontrada com o id: " + id));
        return salaMapper.toResponseDTO(sala);
    }

    public SalaResponseDTO atualizar(Long id, SalaRequestDTO dto) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala nao encontrada com o id: " + id));

        sala.setNome(dto.nome());
        sala.setCapacidade(dto.capacidade());
        sala.setLocalizacao(dto.localizacao());

        Sala salaAtualizada = salaRepository.save(sala);
        return salaMapper.toResponseDTO(salaAtualizada);
    }

    public void deletar(Long id) {
        if (!salaRepository.existsById(id)) {
            throw new RuntimeException("Sala nao encontrada com o id: " + id);
        }
        salaRepository.deleteById(id);
    }

}
