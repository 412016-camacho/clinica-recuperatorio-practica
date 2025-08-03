package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.ExtractorDTO;
import ar.edu.utn.frc.tup.piii.entities.ExtractorEntity;
import ar.edu.utn.frc.tup.piii.repositories.ExtractorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExtractorServiceImpl implements ExtractorService {

    private final ExtractorRepository extractorRepository;

    private final ModelMapper modelMapper;

    @Override
    public List<ExtractorDTO> getAllExtractores() {
        List<ExtractorEntity> extractorEntities = extractorRepository.findAll();
        return modelMapper.map(extractorEntities, new TypeToken<List<ExtractorDTO>>() {}.getType());
    }

    @Override
    public ExtractorDTO getExtractorById(Long id) {
        Optional<ExtractorEntity> extractorEntity = extractorRepository.findById(id);
        if (extractorEntity.isPresent()) {
            return modelMapper.map(extractorEntity.get(), ExtractorDTO.class);
        } else {
            throw new EntityNotFoundException("Extractor not found with id: " + id);
        }
    }
}
