package com.centroweg.sgrr.service;

import com.centroweg.sgrr.dto.ReservaRequestDTO;
import com.centroweg.sgrr.dto.ReservaResponseDTO;
import com.centroweg.sgrr.mapper.ReservaMapper;
import com.centroweg.sgrr.model.Equipamento;
import com.centroweg.sgrr.model.Reserva;
import com.centroweg.sgrr.model.Sala;
import com.centroweg.sgrr.model.StatusReserva;
import com.centroweg.sgrr.model.Usuario;
import com.centroweg.sgrr.repository.EquipamentoRepository;
import com.centroweg.sgrr.repository.ReservaRepository;
import com.centroweg.sgrr.repository.SalaRepository;
import com.centroweg.sgrr.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ReservaMapper reservaMapper;

    public ReservaResponseDTO salvar(ReservaRequestDTO dto) {
        if (!dto.dataHoraFim().isAfter(dto.dataHoraInicio())) {
            throw new RuntimeException("A data/hora de fim deve ser posterior a data/hora de inicio");
        }

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado com o id: " + dto.usuarioId()));

        Sala sala = null;
        if (dto.salaId() != null) {
            sala = salaRepository.findById(dto.salaId())
                    .orElseThrow(() -> new RuntimeException("Sala nao encontrada com o id: " + dto.salaId()));
        }

        Equipamento equipamento = null;
        if (dto.equipamentoId() != null) {
            equipamento = equipamentoRepository.findById(dto.equipamentoId())
                    .orElseThrow(() -> new RuntimeException("Equipamento nao encontrado com o id: " + dto.equipamentoId()));
        }

        List<Reserva> conflitos = reservaRepository.buscarConflitos(
                dto.salaId(), dto.equipamentoId(), dto.dataHoraInicio(), dto.dataHoraFim());

        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Ja existe uma reserva para esse recurso no horario solicitado");
        }

        Reserva reserva = reservaMapper.toEntity(dto, usuario, sala, equipamento);
        Reserva reservaSalva = reservaRepository.save(reserva);
        return reservaMapper.toResponseDTO(reservaSalva);
    }

    public List<ReservaResponseDTO> listarTodos() {
        return reservaRepository.findAll()
                .stream()
                .map(reservaMapper::toResponseDTO)
                .toList();
    }

    public ReservaResponseDTO buscarPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva nao encontrada com o id: " + id));
        return reservaMapper.toResponseDTO(reserva);
    }

    public ReservaResponseDTO cancelar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva nao encontrada com o id: " + id));

        reserva.setStatus(StatusReserva.CANCELADA);
        Reserva reservaCancelada = reservaRepository.save(reserva);
        return reservaMapper.toResponseDTO(reservaCancelada);
    }

    public void deletar(Long id) {
        if (!reservaRepository.existsById(id)) {
            throw new RuntimeException("Reserva nao encontrada com o id: " + id);
        }
        reservaRepository.deleteById(id);
    }

}
