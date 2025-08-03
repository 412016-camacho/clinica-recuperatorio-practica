package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.*;
import ar.edu.utn.frc.tup.piii.entities.EstudioEntity;
import ar.edu.utn.frc.tup.piii.entities.ExtractorEntity;
import ar.edu.utn.frc.tup.piii.entities.PacienteEntity;
import ar.edu.utn.frc.tup.piii.entities.TurnoEntity;
import ar.edu.utn.frc.tup.piii.services.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TurnoController.class)
class TurnoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TurnoService turnoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTurnosTest() throws Exception {
        PacienteDTO paciente = new PacienteDTO(1L, "Paciente", "2020-4-4");
        ExtractorDTO extractor = new ExtractorDTO(1L, "Medico", "EEE333");
        EstudioDTO estudio = new EstudioDTO(1L, "Rayos X", "");
        List<TurnoDTO> turnoDtos = List.of(
                new TurnoDTO(1L, paciente, estudio, extractor, LocalDateTime.of(2020,4,4,10,0,0), TurnoStatus.DISPONIBLE, ""),
                new TurnoDTO(2L, paciente, estudio, extractor, LocalDateTime.of(2021,4,4,10,0,0), TurnoStatus.DISPONIBLE, "")
        );

        when(turnoService.getAllTurnos()).thenReturn(turnoDtos);

        mockMvc.perform(get("/api/v1/turnos")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].paciente.nombre_completo", is("Paciente")))
                .andExpect(jsonPath("$[0].extractor.nombre_completo", is("Medico")));

        verify(turnoService, times(1)).getAllTurnos();
    }

    @Test
    void getTurnosByEstudioTest() throws Exception {
        PacienteDTO paciente = new PacienteDTO(1L, "Paciente", "2020-4-4");
        ExtractorDTO extractor = new ExtractorDTO(1L, "Medico", "EEE333");
        EstudioDTO estudio = new EstudioDTO(1L, "Rayos X", "");
        List<TurnoDTO> turnoDtos = List.of(
                new TurnoDTO(1L, paciente, estudio, extractor, LocalDateTime.of(2020,4,4,10,0,0), TurnoStatus.DISPONIBLE, ""),
                new TurnoDTO(2L, paciente, estudio, extractor, LocalDateTime.of(2021,4,4,10,0,0), TurnoStatus.DISPONIBLE, "")
        );

        when(turnoService.getAllTurnosByEstudio(estudio.getId())).thenReturn(turnoDtos);

        mockMvc.perform(get("/api/v1/turnos")
                        .param("estudio_id", estudio.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].paciente.nombre_completo", is("Paciente")))
                .andExpect(jsonPath("$[0].extractor.nombre_completo", is("Medico")));

        verify(turnoService, times(1)).getAllTurnosByEstudio(1L);
    }

    @Test
    void getTurnosByExtractorTest() throws Exception {
        PacienteDTO paciente = new PacienteDTO(1L, "Paciente", "2020-4-4");
        ExtractorDTO extractor = new ExtractorDTO(1L, "Medico", "EEE333");
        EstudioDTO estudio = new EstudioDTO(1L, "Rayos X", "");
        List<TurnoDTO> turnoDtos = List.of(
                new TurnoDTO(1L, paciente, estudio, extractor, LocalDateTime.of(2020,4,4,10,0,0), TurnoStatus.DISPONIBLE, ""),
                new TurnoDTO(2L, paciente, estudio, extractor, LocalDateTime.of(2021,4,4,10,0,0), TurnoStatus.DISPONIBLE, "")
        );

        when(turnoService.getAllTurnosByExtractor(extractor.getId())).thenReturn(turnoDtos);

        mockMvc.perform(get("/api/v1/turnos")
                        .param("extractor_id", extractor.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].paciente.nombre_completo", is("Paciente")))
                .andExpect(jsonPath("$[0].extractor.nombre_completo", is("Medico")));

        verify(turnoService, times(1)).getAllTurnosByExtractor(1L);
    }

    @Test
    void getTurnosByFechaHoraTest() throws Exception {
        PacienteDTO paciente = new PacienteDTO(1L, "Paciente", "2020-4-4");
        ExtractorDTO extractor = new ExtractorDTO(1L, "Medico", "EEE333");
        EstudioDTO estudio = new EstudioDTO(1L, "Rayos X", "");
        List<TurnoDTO> turnoDtos = List.of(
                new TurnoDTO(1L, paciente, estudio, extractor, LocalDateTime.of(2020,4,4,10,0,0), TurnoStatus.DISPONIBLE, ""),
                new TurnoDTO(2L, paciente, estudio, extractor, LocalDateTime.of(2021,4,4,10,0,0), TurnoStatus.DISPONIBLE, "")
        );

        when(turnoService.getAllTurnosByFecha(turnoDtos.get(0).getFechaHora().toLocalDate())).thenReturn(turnoDtos);

        mockMvc.perform(get("/api/v1/turnos")
                        .param("fecha", turnoDtos.get(0).getFechaHora().toLocalDate().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].paciente.nombre_completo", is("Paciente")))
                .andExpect(jsonPath("$[0].extractor.nombre_completo", is("Medico")));

        verify(turnoService, times(1)).getAllTurnosByFecha(turnoDtos.get(0).getFechaHora().toLocalDate());
    }

    @Test
    void getTurnosByPacienteTest() throws Exception {
        PacienteDTO paciente = new PacienteDTO(1L, "Paciente", "2020-4-4");
        ExtractorDTO extractor = new ExtractorDTO(1L, "Medico", "EEE333");
        EstudioDTO estudio = new EstudioDTO(1L, "Rayos X", "");
        List<TurnoDTO> turnoDtos = List.of(
                new TurnoDTO(1L, paciente, estudio, extractor, LocalDateTime.of(2020,4,4,10,0,0), TurnoStatus.DISPONIBLE, ""),
                new TurnoDTO(2L, paciente, estudio, extractor, LocalDateTime.of(2021,4,4,10,0,0), TurnoStatus.DISPONIBLE, "")
        );

        when(turnoService.getAllTurnosByPaciente(paciente.getId())).thenReturn(turnoDtos);

        mockMvc.perform(get("/api/v1/turnos")
                        .param("paciente_id", paciente.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[0].paciente.nombre_completo", is("Paciente")))
                .andExpect(jsonPath("$[0].extractor.nombre_completo", is("Medico")));

        verify(turnoService, times(1)).getAllTurnosByPaciente(1L);
    }

    @Test
    void createNewTurnoTest() throws Exception {
        PacienteDTO paciente = new PacienteDTO(1L, "Paciente", "2020-4-4");
        ExtractorDTO extractor = new ExtractorDTO(1L, "Medico", "EEE333");
        EstudioDTO estudio = new EstudioDTO(1L, "Rayos X", "");

        NewTurnoDTO newTurnoDTO = new NewTurnoDTO(1L, 1L, 1L, LocalDateTime.now(),"");
        TurnoDTO turnoDto = new TurnoDTO(1L, paciente, estudio, extractor, newTurnoDTO.getFechaHora(), TurnoStatus.DISPONIBLE, "");

        when(turnoService.createTurno(any())).thenReturn(turnoDto);

        mockMvc.perform(put("/api/v1/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTurnoDTO))) //el param q recibe
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.paciente.nombre_completo", is("Paciente")))
                .andExpect(jsonPath("$.extractor.nombre_completo", is("Medico")));
    }

    @Test
    void programarTurnosTest() throws Exception {
        ExtractorDTO extractor = new ExtractorDTO(1L, "Medico", "EEE333");

        TurnoDTO turnoDto = new TurnoDTO(1L, null, null, extractor, LocalDateTime.of(2025,10,2,10,0,0), TurnoStatus.DISPONIBLE, null);

        when(turnoService.programarTurnos(any())).thenReturn(List.of(turnoDto));

        mockMvc.perform(post("/api/v1/turnos/programar")
                        .param("fecha", "2025-10-02")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turnoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].extractor.nombre_completo", is("Medico")))
                .andExpect(jsonPath("$[0].status", is("DISPONIBLE")));
    }
}