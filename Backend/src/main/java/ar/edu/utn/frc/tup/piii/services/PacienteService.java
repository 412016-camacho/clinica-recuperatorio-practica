package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.PacienteDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PacienteService {

    List<PacienteDTO> getAllPacientes();

    PacienteDTO getPacienteById(Long id);
}
