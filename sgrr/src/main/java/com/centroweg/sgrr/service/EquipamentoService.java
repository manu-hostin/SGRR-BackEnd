package com.centroweg.sgrr.service;

import com.centroweg.sgrr.dto.EquipamentoRequestDTO;
import com.centroweg.sgrr.dto.EquipamentoResponseDTO;
import com.centroweg.sgrr.mapper.EquipamentoMapper;
import com.centroweg.sgrr.model.Equipamento;
import com.centroweg.sgrr.repository.EquipamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final EquipamentoMapper equipamentoMapper;

    public EquipamentoResponseDTO salvar(EquipamentoRequestDTO dto) {
        Equipamento equipamento = equipamentoMapper.toEntity(dto);
        Equipamento equipamentoSalvo = equipamentoRepository.save(equipamento);
        return equipamentoMapper.toResponseDTO(equipamentoSalvo);
    }

    public List<EquipamentoResponseDTO> listarTodos() {
        return equipamentoRepository.findAll()
                .stream()
                .map(equipamentoMapper::toResponseDTO)
                .toList();
    }

    public EquipamentoResponseDTO buscarPorId(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento nao encontrado com o id: " + id));
        return equipamentoMapper.toResponseDTO(equipamento);
    }

    public EquipamentoResponseDTO atualizar(Long id, EquipamentoRequestDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento nao encontrado com o id: " + id));

        equipamento.setNome(dto.nome());
        equipamento.setTipo(dto.tipo());
        equipamento.setPatrimonio(dto.patrimonio());

        Equipamento equipamentoAtualizado = equipamentoRepository.save(equipamento);
        return equipamentoMapper.toResponseDTO(equipamentoAtualizado);
    }

    public void deletar(Long id) {
        if (!equipamentoRepository.existsById(id)) {
            throw new RuntimeException("Equipamento nao encontrado com o id: " + id);
        }
        equipamentoRepository.deleteById(id);
    }

}
