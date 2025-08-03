package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.EstudioDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EstudioService {

    List<EstudioDTO> getAllEstudios();
    EstudioDTO getEstudioById(Long id);
}
