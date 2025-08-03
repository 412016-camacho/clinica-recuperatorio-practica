package ar.edu.utn.frc.tup.piii.controllers;


import ar.edu.utn.frc.tup.piii.dtos.EstudioDTO;
import ar.edu.utn.frc.tup.piii.services.EstudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstudiosController {

    private final EstudioService estudioService;

    @GetMapping("/estudios")
    public ResponseEntity<List<EstudioDTO>> getEstudios() {
        return ResponseEntity.ok(estudioService.getAllEstudios());
    }
}
