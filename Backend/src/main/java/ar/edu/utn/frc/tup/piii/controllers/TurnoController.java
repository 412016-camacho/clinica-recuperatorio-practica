package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.NewTurnoDTO;
import ar.edu.utn.frc.tup.piii.dtos.TurnoDTO;
import ar.edu.utn.frc.tup.piii.services.TurnoService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping("/turnos")
    public ResponseEntity<List<TurnoDTO>> getTurnos(
            @RequestParam(name = "estudio_id") Optional<Long> estudioId,
            @RequestParam(name = "extractor_id") Optional<Long> extractorId,
            @RequestParam(name = "paciente_id") Optional<Long> pacienteId,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") @RequestParam Optional<LocalDate> fecha
    ) {
        // TODO: Validar parametros recibidos y retornar la respuesta adecuada
        //  Orden de prioridad en la validación: extractorId, pacienteId, fecha, estudioId
        //  Por default (ningun parametro), retornar todos los turnos.
        if(extractorId.isPresent()){
            return ResponseEntity.ok(turnoService.getAllTurnosByExtractor(extractorId.get()));
        }
        if (pacienteId.isPresent()) {
            return ResponseEntity.ok(turnoService.getAllTurnosByPaciente(pacienteId.get()));
        }
        if (fecha.isPresent()) {
            return ResponseEntity.ok(turnoService.getAllTurnosByFecha(fecha.get()));
        }
        if(estudioId.isPresent()){
            return ResponseEntity.ok(turnoService.getAllTurnosByEstudio(estudioId.get()));
        }
        return ResponseEntity.ok(turnoService.getAllTurnos());
    }

    @PutMapping("/turnos")
    public ResponseEntity<TurnoDTO> createNewTurno(@RequestBody @Valid NewTurnoDTO newTurnoDTO) {
        return ResponseEntity.ok(turnoService.createTurno(newTurnoDTO));
    }

    @PostMapping("/turnos/programar")
    public ResponseEntity<List<TurnoDTO>> programarTurnos(
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") @RequestParam LocalDate fecha) {
        return ResponseEntity.ok(turnoService.programarTurnos(fecha));
    }
}
