package ar.edu.utn.frc.tup.piii.controllers;

import ar.edu.utn.frc.tup.piii.dtos.ExtractorDTO;
import ar.edu.utn.frc.tup.piii.dtos.NewTurnoDTO;
import ar.edu.utn.frc.tup.piii.dtos.TurnoDTO;
import ar.edu.utn.frc.tup.piii.dtos.TurnoStatus;
import ar.edu.utn.frc.tup.piii.entities.*;
import ar.edu.utn.frc.tup.piii.repositories.DisponibilidadTurnosRepository;
import ar.edu.utn.frc.tup.piii.repositories.TurnoRepository;
import ar.edu.utn.frc.tup.piii.services.EstudioService;
import ar.edu.utn.frc.tup.piii.services.ExtractorService;
import ar.edu.utn.frc.tup.piii.services.PacienteService;
import ar.edu.utn.frc.tup.piii.services.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private TurnoService turnoService;

    @MockitoBean
    private TurnoRepository turnoRepository;

    @MockitoSpyBean
    private ExtractorService extractorService;

    @MockitoSpyBean
    private PacienteService pacienteService;

    @MockitoSpyBean
    private EstudioService estudioService;

    @MockitoSpyBean
    private DisponibilidadTurnosRepository disponibilidadTurnosRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTurnosTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);
        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        this.mockMvc
                .perform(
                        get("/api/v1/turnos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<TurnoDTO> turnos = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TurnoDTO.class));
                    assert turnos.size() == 2;
                    assert turnos.get(0).getId().equals(turno.getId());
                    assert turnos.get(1).getId().equals(turno2.getId());
                });
    }

    @Test
    void getTurnosByEstudioTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));

        this.mockMvc
                .perform(
                        get("/api/v1/turnos?estudio_id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<TurnoDTO> turnos = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TurnoDTO.class));
                    assert turnos.size() == 1;
                    assert turnos.get(0).getId().equals(turno2.getId());
                });
    }

    @Test
    void getTurnosByExtractorTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));

        this.mockMvc
                .perform(
                        get("/api/v1/turnos?extractor_id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<TurnoDTO> turnos = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TurnoDTO.class));
                    assert turnos.size() == 2;
                    assert turnos.get(0).getId().equals(turno.getId());
                    assert turnos.get(1).getId().equals(turno2.getId());
                });
    }

    @Test
    void getTurnosByFechaHoraTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));

        this.mockMvc
                .perform(
                        get("/api/v1/turnos?fecha=2025-01-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<TurnoDTO> turnos = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TurnoDTO.class));
                    assert turnos.size() == 2;
                    assert turnos.get(0).getId().equals(turno.getId());
                    assert turnos.get(1).getId().equals(turno2.getId());
                });
    }

    @Test
    void getTurnosByPacienteTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(turno2));

        this.mockMvc
                .perform(
                        get("/api/v1/turnos?paciente_id=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<TurnoDTO> turnos = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TurnoDTO.class));
                    assert turnos.size() == 1;
                    assert turnos.get(0).getId().equals(turno2.getId());
                });
    }

    @Test
    void createNewTurnoHappyPathTest() throws Exception {
        PacienteEntity paciente = new PacienteEntity(2L, "JorgePerez", LocalDate.of(1990, 1, 1));

        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turnoSaved = new TurnoEntity();
        turnoSaved.setId(1L);
        turnoSaved.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turnoSaved.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turnoSaved.setPaciente(paciente);
        turnoSaved.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turnoSaved.setStatus(TurnoStatus.OCUPADO);
        turnoSaved.setObservaciones("Observaciones del turno");

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(2L)).thenReturn(new ArrayList<>());
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 0), 1L)).thenReturn(List.of(turno));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))).thenReturn(List.of(turno));
        when(turnoRepository.save(Mockito.any())).thenReturn(turnoSaved);

        NewTurnoDTO newTurnoDTO = NewTurnoDTO.builder()
                .extractorId(1L)
                .pacienteId(2L)
                .estudioId(1L)
                .fechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))
                .observaciones("Observaciones del turno")
                .build();

        this.mockMvc
                .perform(
                        put("/api/v1/turnos")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(newTurnoDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    TurnoDTO response = objectMapper.readValue(content, TurnoDTO.class);
                    assert response.getId() == 1L;
                    assert response.getExtractor().getId() == 1L;
                    assert response.getPaciente().getId() == 2L;
                    assert response.getEstudio().getId() == 1L;
                    assert response.getFechaHora().equals(LocalDateTime.of(2025, 1, 1, 7, 0));
                    assert response.getObservaciones().equals("Observaciones del turno");
                    assert response.getStatus() == TurnoStatus.OCUPADO;
                });
    }

    @Test
    void createTurnoFailPacienteTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turnoSaved = new TurnoEntity();
        turnoSaved.setId(1L);
        turnoSaved.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turnoSaved.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turnoSaved.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turnoSaved.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turnoSaved.setStatus(TurnoStatus.OCUPADO);
        turnoSaved.setObservaciones("Observaciones del turno");


        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 0), 1L)).thenReturn(List.of(turno));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))).thenReturn(List.of(turno));
        when(turnoRepository.save(Mockito.any())).thenReturn(turnoSaved);

        NewTurnoDTO newTurnoDTO = NewTurnoDTO.builder()
                .extractorId(1L)
                .pacienteId(3L)
                .estudioId(1L)
                .fechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))
                .observaciones("Observaciones del turno")
                .build();

        this.mockMvc
                .perform(
                        put("/api/v1/turnos")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(newTurnoDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createTurnoFailExtractorTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turnoSaved = new TurnoEntity();
        turnoSaved.setId(1L);
        turnoSaved.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turnoSaved.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turnoSaved.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turnoSaved.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turnoSaved.setStatus(TurnoStatus.OCUPADO);
        turnoSaved.setObservaciones("Observaciones del turno");


        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 0), 1L)).thenReturn(List.of(turno));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))).thenReturn(List.of(turno));
        when(turnoRepository.save(Mockito.any())).thenReturn(turnoSaved);

        NewTurnoDTO newTurnoDTO = NewTurnoDTO.builder()
                .extractorId(4L)
                .pacienteId(1L)
                .estudioId(1L)
                .fechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))
                .observaciones("Observaciones del turno")
                .build();

        this.mockMvc
                .perform(
                        put("/api/v1/turnos")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(newTurnoDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createTurnoFailEstudioTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turnoSaved = new TurnoEntity();
        turnoSaved.setId(1L);
        turnoSaved.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turnoSaved.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turnoSaved.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turnoSaved.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turnoSaved.setStatus(TurnoStatus.OCUPADO);
        turnoSaved.setObservaciones("Observaciones del turno");


        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 0), 1L)).thenReturn(List.of(turno));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))).thenReturn(List.of(turno));
        when(turnoRepository.save(Mockito.any())).thenReturn(turnoSaved);

        NewTurnoDTO newTurnoDTO = NewTurnoDTO.builder()
                .extractorId(1L)
                .pacienteId(1L)
                .estudioId(5L)
                .fechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))
                .observaciones("Observaciones del turno")
                .build();

        this.mockMvc
                .perform(
                        put("/api/v1/turnos")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(newTurnoDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createTurnoFailPacienteTieneTurnoMismoDiaTest() throws Exception {
        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turnoSaved = new TurnoEntity();
        turnoSaved.setId(1L);
        turnoSaved.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turnoSaved.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turnoSaved.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turnoSaved.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turnoSaved.setStatus(TurnoStatus.OCUPADO);
        turnoSaved.setObservaciones("Observaciones del turno");


        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 0), 1L)).thenReturn(List.of(turno));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))).thenReturn(List.of(turno));
        when(turnoRepository.save(Mockito.any())).thenReturn(turnoSaved);

        NewTurnoDTO newTurnoDTO = NewTurnoDTO.builder()
                .extractorId(1L)
                .pacienteId(1L)
                .estudioId(1L)
                .fechaHora(LocalDateTime.of(2025, 1, 1, 7, 0))
                .observaciones("Observaciones del turno")
                .build();

        this.mockMvc
                .perform(
                        put("/api/v1/turnos")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(newTurnoDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createTurnoFailTurnoExtractorOcupadoTest() throws Exception {
        PacienteEntity paciente = new PacienteEntity(2L, "JorgePerez", LocalDate.of(1990, 1, 1));

        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turnoSaved = new TurnoEntity();
        turnoSaved.setId(1L);
        turnoSaved.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turnoSaved.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turnoSaved.setPaciente(paciente);
        turnoSaved.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turnoSaved.setStatus(TurnoStatus.OCUPADO);
        turnoSaved.setObservaciones("Observaciones del turno");

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(2L)).thenReturn(new ArrayList<>());
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 15), 1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15))).thenReturn(List.of(turno2));
        when(turnoRepository.save(Mockito.any())).thenReturn(turnoSaved);

        NewTurnoDTO newTurnoDTO = NewTurnoDTO.builder()
                .extractorId(1L)
                .pacienteId(2L)
                .estudioId(1L)
                .fechaHora(LocalDateTime.of(2025, 1, 1, 7, 15))
                .observaciones("Observaciones del turno")
                .build();

        this.mockMvc
                .perform(
                        put("/api/v1/turnos")
                                .contentType(APPLICATION_JSON_UTF8)
                                .content(objectMapper.writeValueAsString(newTurnoDTO)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void programarTurnosHappyPathTest() throws Exception {
        ExtractorDTO extractorDTO = new ExtractorDTO(1L, "MAT", "PEPE");
        DisponibilidadTurnosEntity d1 = new DisponibilidadTurnosEntity(1L, LocalTime.of(7,0), 15);
        DisponibilidadTurnosEntity d2 = new DisponibilidadTurnosEntity(2L, LocalTime.of(7,15), 15);
        DisponibilidadTurnosEntity d3 = new DisponibilidadTurnosEntity(2L, LocalTime.of(7,30), 15);

        TurnoEntity t1 = new TurnoEntity();
        t1.setId(3L);
        t1.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        t1.setFechaHora(LocalDateTime.of(2025, 1, 2, 7, 0));
        t1.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity t2 = new TurnoEntity();
        t2.setId(4L);
        t2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        t2.setFechaHora(LocalDateTime.of(2025, 1, 2, 7, 15));
        t2.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity t3 = new TurnoEntity();
        t3.setId(5L);
        t3.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        t3.setFechaHora(LocalDateTime.of(2025, 1, 2, 7, 30));
        t3.setStatus(TurnoStatus.DISPONIBLE);


        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(2L)).thenReturn(new ArrayList<>());
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 15), 1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15))).thenReturn(List.of(turno2));
        when(extractorService.getAllExtractores()).thenReturn(List.of(extractorDTO));
        when(disponibilidadTurnosRepository.findAll()).thenReturn(List.of(d1, d2, d3));
        when(turnoRepository.save(Mockito.any())).thenReturn(t1);
        when(turnoRepository.save(Mockito.any())).thenReturn(t2);
        when(turnoRepository.save(Mockito.any())).thenReturn(t3);

        this.mockMvc
                .perform(
                        post("/api/v1/turnos/programar?fecha=2025-01-02"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(result -> {
                    String content = result.getResponse().getContentAsString();
                    List<TurnoDTO> turnos = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, TurnoDTO.class));
                    assert turnos.size() == 3;
                    assert turnos.get(0).getStatus().equals(TurnoStatus.DISPONIBLE);
                    assert turnos.get(0).getExtractor().getId().equals(extractorDTO.getId());
                    assert turnos.get(1).getStatus().equals(TurnoStatus.DISPONIBLE);
                    assert turnos.get(1).getExtractor().getId().equals(extractorDTO.getId());
                    assert turnos.get(2).getStatus().equals(TurnoStatus.DISPONIBLE);
                    assert turnos.get(2).getExtractor().getId().equals(extractorDTO.getId());
                });
    }

    @Test
    void programarTurnosFailDiaYaProgramado() throws Exception {
        ExtractorDTO extractorDTO = new ExtractorDTO(1L, "MAT", "PEPE");
        DisponibilidadTurnosEntity d1 = new DisponibilidadTurnosEntity(1L, LocalTime.of(7,0), 15);
        DisponibilidadTurnosEntity d2 = new DisponibilidadTurnosEntity(2L, LocalTime.of(7,15), 15);
        DisponibilidadTurnosEntity d3 = new DisponibilidadTurnosEntity(2L, LocalTime.of(7,30), 15);

        TurnoEntity t1 = new TurnoEntity();
        t1.setId(3L);
        t1.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        t1.setFechaHora(LocalDateTime.of(2025, 1, 2, 7, 0));
        t1.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity t2 = new TurnoEntity();
        t2.setId(4L);
        t2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        t2.setFechaHora(LocalDateTime.of(2025, 1, 2, 7, 15));
        t2.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity t3 = new TurnoEntity();
        t3.setId(5L);
        t3.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        t3.setFechaHora(LocalDateTime.of(2025, 1, 2, 7, 30));
        t3.setStatus(TurnoStatus.DISPONIBLE);


        TurnoEntity turno = new TurnoEntity();
        turno.setId(1L);
        turno.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 0));
        turno.setStatus(TurnoStatus.DISPONIBLE);

        TurnoEntity turno2 = new TurnoEntity();
        turno2.setId(2L);
        turno2.setExtractor(new ExtractorEntity(1L, "MAT", "PEPE"));
        turno2.setEstudio(new EstudioEntity(1L, "Estudio de Sangre", "Descripcion del estudio"));
        turno2.setPaciente(new PacienteEntity(1L, "JuanPerez", LocalDate.of(1990, 1, 1)));
        turno2.setFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15));
        turno2.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByEstudioId(1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.of(2025, 1, 1))).thenReturn(List.of(turno, turno2));
        when(turnoRepository.findTurnoEntitiesByPacienteId(2L)).thenReturn(new ArrayList<>());
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(LocalDateTime.of(2025, 1, 1, 7, 15), 1L)).thenReturn(List.of(turno2));
        when(turnoRepository.findTurnoEntitiesByFechaHora(LocalDateTime.of(2025, 1, 1, 7, 15))).thenReturn(List.of(turno2));
        when(extractorService.getAllExtractores()).thenReturn(List.of(extractorDTO));
        when(disponibilidadTurnosRepository.findAll()).thenReturn(List.of(d1, d2, d3));
        when(turnoRepository.save(Mockito.any())).thenReturn(t1);
        when(turnoRepository.save(Mockito.any())).thenReturn(t2);
        when(turnoRepository.save(Mockito.any())).thenReturn(t3);

        this.mockMvc
                .perform(
                        post("/api/v1/turnos/programar?fecha=2025-01-01"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
