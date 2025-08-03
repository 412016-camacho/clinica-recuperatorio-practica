package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.ExtractorDTO;
import ar.edu.utn.frc.tup.piii.dtos.TurnoDTO;
import ar.edu.utn.frc.tup.piii.services.ExtractorService;
import ar.edu.utn.frc.tup.piii.services.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExtractorController {

    private final ExtractorService extractorService;

    private final TurnoService turnoService;

    @GetMapping("/extractores")
    public ResponseEntity<List<ExtractorDTO>> getExtractores() {
        return ResponseEntity.ok(extractorService.getAllExtractores());
    }

    @GetMapping("/extractores/{id}/turnos")
    public ResponseEntity<List<TurnoDTO>> getTurnosExtractor(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.getAllTurnosByExtractor(id));
    }
}
