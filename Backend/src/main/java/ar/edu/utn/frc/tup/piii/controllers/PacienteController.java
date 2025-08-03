package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.PacienteDTO;
import ar.edu.utn.frc.tup.piii.dtos.TurnoDTO;
import ar.edu.utn.frc.tup.piii.services.PacienteService;
import ar.edu.utn.frc.tup.piii.services.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class PacienteController {

    private final PacienteService pacienteService;

    private final TurnoService turnoService;

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteDTO>> getPacientes() {
        return ResponseEntity.ok(pacienteService.getAllPacientes());
    }

    @GetMapping("/pacientes/{id}/turnos")
    public ResponseEntity<List<TurnoDTO>> getTurnosPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(turnoService.getAllTurnosByPaciente(id));
    }
}
