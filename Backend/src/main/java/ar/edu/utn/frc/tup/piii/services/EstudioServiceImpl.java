package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.EstudioDTO;
import ar.edu.utn.frc.tup.piii.entities.EstudioEntity;
import ar.edu.utn.frc.tup.piii.repositories.EstudioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstudioServiceImpl implements EstudioService {

    private final EstudioRepository estudioRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<EstudioDTO> getAllEstudios() {
        List<EstudioEntity> estudioEntities = estudioRepository.findAll();
        return modelMapper.map(estudioEntities, new TypeToken<List<EstudioDTO>>() {}.getType());
    }

    @Override
    public EstudioDTO getEstudioById(Long id) {
        Optional<EstudioEntity> estudioEntityOptional = estudioRepository.findById(id);
        if (estudioEntityOptional.isPresent()) {
            return modelMapper.map(estudioEntityOptional.get(), EstudioDTO.class);
        } else {
            throw new EntityNotFoundException("Estudio not found with id: " + id);
        }
    }
}
