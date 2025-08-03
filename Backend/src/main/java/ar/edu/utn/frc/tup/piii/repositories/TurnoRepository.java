package ar.edu.utn.frc.tup.piii.repositories;

import ar.edu.utn.frc.tup.piii.entities.TurnoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@Repository
public interface TurnoRepository extends JpaRepository<TurnoEntity, Long> {

    List<TurnoEntity> findTurnoEntitiesByExtractorId(Long medicoId);
    List<TurnoEntity> findTurnoEntitiesByPacienteId(Long pacienteId);
    List<TurnoEntity> findTurnoEntitiesByEstudioId(Long estudioId);
    List<TurnoEntity> findTurnoEntitiesByFechaHora(LocalDateTime fechaHora);
    List<TurnoEntity> findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime fechaHora, Long extractorId);
    @Query("SELECT t FROM TurnoEntity t WHERE CAST(t.fechaHora AS DATE) = :date")
    List<TurnoEntity> findTurnoEntitiesByFecha(@Param(value = "date") LocalDate date);

    Object existsByExtractor_IdAndFechaHora(long eq, LocalDate eq1, LocalTime eq2);
}
