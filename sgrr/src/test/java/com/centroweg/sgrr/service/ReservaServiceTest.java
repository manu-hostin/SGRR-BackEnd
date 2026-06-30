package com.centroweg.sgrr.service;

import com.centroweg.sgrr.dto.ReservaRequestDTO;
import com.centroweg.sgrr.mapper.ReservaMapper;
import com.centroweg.sgrr.model.Reserva;
import com.centroweg.sgrr.model.Sala;
import com.centroweg.sgrr.model.StatusReserva;
import com.centroweg.sgrr.model.Usuario;
import com.centroweg.sgrr.repository.EquipamentoRepository;
import com.centroweg.sgrr.repository.ReservaRepository;
import com.centroweg.sgrr.repository.SalaRepository;
import com.centroweg.sgrr.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Cobre os dois pontos do checklist de testes que envolvem regra de
 * negocio na Reserva: conflito de horario e data/hora invalida.
 */
@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SalaRepository salaRepository;
    @Mock
    private EquipamentoRepository equipamentoRepository;

    private ReservaMapper reservaMapper;
    private ReservaService reservaService;

    private Usuario usuario;
    private Sala sala;

    @BeforeEach
    void setUp() {
        reservaMapper = new ReservaMapper();
        reservaService = new ReservaService(
                reservaRepository, usuarioRepository, salaRepository, equipamentoRepository, reservaMapper);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Usuario Teste");

        sala = new Sala();
        sala.setId(1L);
        sala.setNome("Sala 101");
    }

    @Test
    void deveRejeitarReservaComDataFimAnteriorOuIgualAoInicio() {
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 30, 14, 0);
        ReservaRequestDTO dto = new ReservaRequestDTO(1L, 1L, null, inicio, inicio);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> reservaService.salvar(dto));
        assertTrue(ex.getMessage().contains("posterior"));
    }

    @Test
    void deveRejeitarReservaComConflitoDeHorario() {
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 30, 14, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 6, 30, 15, 0);
        ReservaRequestDTO dto = new ReservaRequestDTO(1L, 1L, null, inicio, fim);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));

        Reserva reservaExistente = new Reserva();
        reservaExistente.setStatus(StatusReserva.CONFIRMADA);
        when(reservaRepository.buscarConflitos(1L, null, inicio, fim))
                .thenReturn(List.of(reservaExistente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> reservaService.salvar(dto));
        assertTrue(ex.getMessage().toLowerCase().contains("ja existe"));
    }

    @Test
    void devePermitirReservaSemConflito() {
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 30, 14, 0);
        LocalDateTime fim = LocalDateTime.of(2026, 6, 30, 15, 0);
        ReservaRequestDTO dto = new ReservaRequestDTO(1L, 1L, null, inicio, fim);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(salaRepository.findById(1L)).thenReturn(Optional.of(sala));
        when(reservaRepository.buscarConflitos(1L, null, inicio, fim))
                .thenReturn(Collections.emptyList());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> {
            Reserva r = invocation.getArgument(0);
            r.setId(10L);
            return r;
        });

        var response = reservaService.salvar(dto);

        assertEquals(10L, response.id());
        assertEquals(StatusReserva.CONFIRMADA, response.status());
    }

}
