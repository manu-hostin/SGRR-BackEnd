package com.centroweg.sgrr.repository;

import com.centroweg.sgrr.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("SELECT r FROM Reserva r WHERE r.status = 'CONFIRMADA' " +
            "AND ((:salaId IS NOT NULL AND r.sala.id = :salaId) " +
            "OR (:equipamentoId IS NOT NULL AND r.equipamento.id = :equipamentoId)) " +
            "AND r.dataHoraInicio < :dataHoraFim AND r.dataHoraFim > :dataHoraInicio")
    List<Reserva> buscarConflitos(
            @Param("salaId") Long salaId,
            @Param("equipamentoId") Long equipamentoId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim
    );

}
