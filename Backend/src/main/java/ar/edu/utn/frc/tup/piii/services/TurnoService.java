package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.NewTurnoDTO;
import ar.edu.utn.frc.tup.piii.dtos.TurnoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TurnoService {

    TurnoDTO createTurno(NewTurnoDTO newTurnoDTO);
    List<TurnoDTO> getAllTurnosByExtractor(Long extractorId);
    List<TurnoDTO> getAllTurnosByPaciente(Long pacienteId);
    List<TurnoDTO> getAllTurnosByEstudio(Long estudioId);
    List<TurnoDTO> getAllTurnosByFecha(LocalDate fecha);
    List<TurnoDTO> getAllTurnos();
    List<TurnoDTO> programarTurnos(LocalDate fecha);
}
