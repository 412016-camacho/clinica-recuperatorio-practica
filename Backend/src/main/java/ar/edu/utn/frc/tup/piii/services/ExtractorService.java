package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.ExtractorDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExtractorService {

    List<ExtractorDTO> getAllExtractores();

    ExtractorDTO getExtractorById(Long id);
}
