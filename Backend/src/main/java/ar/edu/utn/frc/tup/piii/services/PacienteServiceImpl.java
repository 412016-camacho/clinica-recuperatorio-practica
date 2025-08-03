package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.PacienteDTO;
import ar.edu.utn.frc.tup.piii.entities.PacienteEntity;
import ar.edu.utn.frc.tup.piii.repositories.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<PacienteDTO> getAllPacientes() {
        List<PacienteEntity> pacienteEntities = pacienteRepository.findAll();
        return modelMapper.map(pacienteEntities, new TypeToken<List<PacienteDTO>>() {}.getType());
    }

    @Override
    public PacienteDTO getPacienteById(Long id) {
        Optional<PacienteEntity> pacienteEntity = pacienteRepository.findById(id);
        if (pacienteEntity.isPresent()) {
            return modelMapper.map(pacienteEntity.get(), PacienteDTO.class);
        } else {
            throw new EntityNotFoundException("Paciente not found with id: " + id);
        }
    }
}
