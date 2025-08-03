package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.*;
import ar.edu.utn.frc.tup.piii.entities.EstudioEntity;
import ar.edu.utn.frc.tup.piii.entities.ExtractorEntity;
import ar.edu.utn.frc.tup.piii.entities.PacienteEntity;
import ar.edu.utn.frc.tup.piii.entities.TurnoEntity;
import ar.edu.utn.frc.tup.piii.repositories.DisponibilidadTurnosRepository;
import ar.edu.utn.frc.tup.piii.repositories.EstudioRepository;
import ar.edu.utn.frc.tup.piii.repositories.PacienteRepository;
import ar.edu.utn.frc.tup.piii.repositories.TurnoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.web.servlet.function.RequestPredicates.method;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {

    @MockitoSpyBean
    private TurnoService turnoService;

    @MockitoBean
    private TurnoRepository turnoRepository;

    @MockitoBean
    private ExtractorService extractorService;

    @MockitoBean
    private PacienteService pacienteService;

    @MockitoBean
    private EstudioService estudioService;

    @MockitoBean
    private DisponibilidadTurnosRepository disponibilidadTurnosRepository;

    @MockitoBean
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        turnoService = new TurnoServiceImpl(turnoRepository,modelMapper,extractorService,pacienteService,estudioService,disponibilidadTurnosRepository);
    }

    @Test
    void createTurnoHappyPathTest() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        PacienteDTO paciente = new PacienteDTO(1L, "Marcos Casper", "1980-1-1");
        ExtractorDTO extractor = new ExtractorDTO(1L, "Laura Perez", "ENF1234");
        EstudioDTO estudio = new EstudioDTO(1L, "Análisis de sangre", "Requiere ayuno");

        TurnoEntity turnoEntity = new TurnoEntity(1L,
                modelMapper.map(paciente, PacienteEntity.class),
                modelMapper.map(extractor, ExtractorEntity.class),
                modelMapper.map(estudio, EstudioEntity.class),
                fecha1, TurnoStatus.DISPONIBLE, "");

        NewTurnoDTO newTurnoDTO = new NewTurnoDTO();
        newTurnoDTO.setPacienteId(paciente.getId());
        newTurnoDTO.setExtractorId(extractor.getId());
        newTurnoDTO.setEstudioId(estudio.getId());
        newTurnoDTO.setFechaHora(LocalDateTime.of(2026,2,2,10,0,0));

        when(estudioService.getEstudioById(1L)).thenReturn(estudio);
        when(extractorService.getExtractorById(1L)).thenReturn(extractor);
        when(pacienteService.getPacienteById(1L)).thenReturn(paciente);
        when(turnoRepository.save(any(TurnoEntity.class))).thenReturn(turnoEntity);

        TurnoDTO result = turnoService.createTurno(newTurnoDTO);

        assertNotNull(result);
        assertEquals(1L, result.getEstudio().getId());
        assertEquals(1L, result.getPaciente().getId());
        assertEquals(1L, result.getExtractor().getId());
        assertEquals(fecha1, result.getFechaHora());

    }

    @Test
    void createTurnoFailPacienteOrExtractorOrEstudioTest() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        NewTurnoDTO newTurnoDTO = new NewTurnoDTO(1L,1L,1L, fecha1,"");

        TurnoEntity existente = new TurnoEntity();
        existente.setPaciente(null);
        existente.setFechaHora(fecha1);

        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(existente));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> turnoService.createTurno(newTurnoDTO));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void createTurnoFailPacienteTieneTurnoMismoDiaTest() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        NewTurnoDTO newTurnoDTO = new NewTurnoDTO(1L,1L,1L, fecha1,"");

        TurnoEntity existente = new TurnoEntity();
        existente.setPaciente(new PacienteEntity());
        existente.setFechaHora(fecha1);

        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(existente));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> turnoService.createTurno(newTurnoDTO));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());

    }

    @Test
    void createTurnoFailTurnoExtractorOcupadoTest() throws Exception {
        NewTurnoDTO newTurnoDTO = new NewTurnoDTO();
        newTurnoDTO.setExtractorId(1L);
        newTurnoDTO.setFechaHora(LocalDateTime.now());

        // Hacer que el repository retorne algo que haga masDeUnTurno = true
        when(turnoRepository.findTurnoEntitiesByFechaHoraAndExtractorId(any(), anyLong()))
                .thenReturn(Arrays.asList(new TurnoEntity()));

        assertThrows(ResponseStatusException.class, () -> turnoService.createTurno(newTurnoDTO));
    }

    @Test
    void masDeUnTurno() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        LocalDateTime fecha = LocalDateTime.of(2025, 8, 10, 10, 0);
        NewTurnoDTO dto = new NewTurnoDTO(1L, 1L, 1L, fecha, "");

        TurnoEntity turnoExistente = new TurnoEntity();
        turnoExistente.setFechaHora(fecha);
        turnoExistente.setPaciente(null);

        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turnoExistente));

        Method metodoPrivado = turnoService.getClass().getDeclaredMethod("masDeUnTurno", NewTurnoDTO.class);
        metodoPrivado.setAccessible(true);

        boolean resultado = (boolean) metodoPrivado.invoke(turnoService, dto);

        assertTrue(resultado);

    }

    @Test
    void masDeUnTurno_DevuelveFalse_CuandoPacienteNoEsNull() throws Exception {
        LocalDateTime fecha = LocalDateTime.of(2025, 8, 10, 10, 0);
        NewTurnoDTO dto = new NewTurnoDTO(1L, 1L, 1L, fecha, "");

        TurnoEntity turno = new TurnoEntity();
        turno.setFechaHora(fecha);
        turno.setPaciente(new PacienteEntity());

        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno));

        Method metodoPrivado = turnoService.getClass().getDeclaredMethod("masDeUnTurno", NewTurnoDTO.class);
        metodoPrivado.setAccessible(true);

        boolean resultado = (boolean) metodoPrivado.invoke(turnoService, dto);

        assertFalse(resultado);
    }

    @Test
    void masDeUnTurno_DevuelveFalse_CuandoFechaNoCoincidePeroPacienteEsNull() throws Exception {
        LocalDateTime fecha = LocalDateTime.of(2025, 8, 10, 10, 0);
        NewTurnoDTO dto = new NewTurnoDTO(1L, 1L, 1L, fecha, "");

        TurnoEntity turno = new TurnoEntity();
        turno.setFechaHora(LocalDateTime.of(2025, 8, 10, 11, 0)); // Distinta hora
        turno.setPaciente(null);

        when(turnoRepository.findTurnoEntitiesByExtractorId(1L)).thenReturn(List.of(turno));

        Method metodoPrivado = turnoService.getClass().getDeclaredMethod("masDeUnTurno", NewTurnoDTO.class);
        metodoPrivado.setAccessible(true);

        boolean resultado = (boolean) metodoPrivado.invoke(turnoService, dto);

        assertFalse(resultado);
    }

    @Test
    void puedeAgendarTurno() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        LocalDateTime fecha = LocalDateTime.of(2025, 8, 10, 10, 0);
        NewTurnoDTO dto = new NewTurnoDTO(1L, 1L, 1L, fecha, "");

        TurnoEntity turno = new TurnoEntity();
        turno.setFechaHora(LocalDateTime.of(2025, 8, 10, 10, 0));
        turno.setPaciente(null);

        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(turno));

        Method metodoPrivado = turnoService.getClass().getDeclaredMethod("puedeAgendarTurno", NewTurnoDTO.class);
        metodoPrivado.setAccessible(true);

        boolean resultado = (boolean) metodoPrivado.invoke(turnoService, dto);

        assertTrue(resultado);

    }

    @Test
    void disponible_true() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        LocalDateTime fecha = LocalDateTime.of(2025, 8, 10, 10, 0);
        NewTurnoDTO dto = new NewTurnoDTO(null, 1L, 1L, fecha, "");

        TurnoEntity turno = new TurnoEntity();
        turno.setFechaHora( LocalDateTime.of(2025, 8, 10, 10, 0));
        turno.setPaciente(null);
        turno.setStatus(TurnoStatus.DISPONIBLE);

        when(turnoRepository.findAll()).thenReturn(List.of(turno));

        Method metodoPrivado = turnoService.getClass().getDeclaredMethod("disponible", NewTurnoDTO.class);
        metodoPrivado.setAccessible(true);

        boolean result = (boolean) metodoPrivado.invoke(turnoService, dto);

        assertTrue(result);
    }

    @Test
    void disponible_false() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        LocalDateTime fecha = LocalDateTime.of(2025, 8, 10, 10, 0);
        NewTurnoDTO dto = new NewTurnoDTO(1L, 1L, 1L, fecha, "");

        TurnoEntity turno = new TurnoEntity();
        turno.setFechaHora(fecha);
        turno.setPaciente(paciente);
        turno.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findAll()).thenReturn(List.of(turno));

        Method metodoPrivado = turnoService.getClass().getDeclaredMethod("disponible", NewTurnoDTO.class);
        metodoPrivado.setAccessible(true);

        boolean result = (boolean) metodoPrivado.invoke(turnoService, dto);

        assertFalse(result);
    }

    @Test
    void puedeAgendarturno_true() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PacienteEntity paciente = new PacienteEntity();
        paciente.setId(1L);

        LocalDateTime fecha = LocalDateTime.of(2025, 8, 10, 10, 0);
        NewTurnoDTO dto = new NewTurnoDTO(1L, 1L, 1L, fecha, "");

        TurnoEntity turno = new TurnoEntity();
        turno.setFechaHora(LocalDateTime.of(2025, 8, 10, 10, 0));
        turno.setPaciente(paciente);
        turno.setStatus(TurnoStatus.OCUPADO);

        when(turnoRepository.findTurnoEntitiesByPacienteId(1L)).thenReturn(List.of(turno));

        Method metodoPrivado = turnoService.getClass().getDeclaredMethod("puedeAgendarTurno", NewTurnoDTO.class);
        metodoPrivado.setAccessible(true);

        boolean result = (boolean) metodoPrivado.invoke(turnoService, dto);

        assertTrue(result);
    }


    @Test
    void programarTurnosHappyPathTest() {
        LocalDate fecha = LocalDate.of(2024, 1, 15);
        ExtractorDTO extractor1 = new ExtractorDTO(1L, "Juan Perez", "EFE123");
        ExtractorDTO extractor2 = new ExtractorDTO(2L, "Maria Gomez", "FDF222");
        List<ExtractorDTO> extractoresMock = List.of(extractor1, extractor2);

        when(turnoRepository.findTurnoEntitiesByFecha(fecha)).thenReturn(Collections.emptyList());
        when(extractorService.getAllExtractores()).thenReturn(extractoresMock);

        List<TurnoEntity> turnosGuardados = new ArrayList<>();
        //ThenAnswer permite definir un comportamiento personalizado cuando se llama a un metodo mockeado
        when(turnoRepository.save(any(TurnoEntity.class))).thenAnswer(invocation -> {
            TurnoEntity turno = invocation.getArgument(0);
            turno.setId(System.currentTimeMillis() + turnosGuardados.size());
            turnosGuardados.add(turno);
            return turno;
        });

        try {
            turnoService.programarTurnos(fecha);
        } catch (Exception e) {
        }
        assertFalse(turnosGuardados.isEmpty());
        assertEquals(32, turnosGuardados.size());
        turnosGuardados.forEach(turno -> {
            assertEquals(fecha, turno.getFechaHora().toLocalDate());
        });
        turnosGuardados.forEach(turno -> {
            assertNull(turno.getPaciente());
            assertNull(turno.getEstudio());
            assertNull(turno.getObservaciones());
        });
    }

    @Test
    void programarTurnosFailDiaYaProgramado() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        LocalDateTime fecha2 = LocalDateTime.of(2025,2,2,10,15,0);
        PacienteEntity paciente = new PacienteEntity(1L, "Marcos Casper", LocalDate.of(1980, 10, 2));
        ExtractorEntity extractor = new ExtractorEntity(1L, "Laura Perez", "ENF1234");
        EstudioEntity estudio = new EstudioEntity(1L, "Análisis de sangre", "Requiere ayuno");
        TurnoEntity turno1 = new TurnoEntity(1L, paciente, extractor, estudio, fecha1, TurnoStatus.DISPONIBLE, "");
        TurnoEntity turno2 = new TurnoEntity(2L, paciente, extractor, estudio, fecha2, TurnoStatus.DISPONIBLE, "");
        List<TurnoEntity> turnos = List.of(turno1, turno2);

        when(turnoRepository.findTurnoEntitiesByFecha(LocalDate.now())).thenReturn(turnos);

        assertThrows(ResponseStatusException.class, () -> {
            turnoService.programarTurnos(LocalDate.now());
        });
    }

    @Test
    void getAllTurnosByExtractor() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        LocalDateTime fecha2 = LocalDateTime.of(2025,2,2,10,15,0);
        PacienteEntity paciente = new PacienteEntity(1L, "Marcos Casper", LocalDate.of(1980, 10, 2));
        ExtractorEntity extractor = new ExtractorEntity(1L, "Laura Perez", "ENF1234");
        EstudioEntity estudio = new EstudioEntity(1L, "Análisis de sangre", "Requiere ayuno");
        TurnoEntity turno1 = new TurnoEntity(1L, paciente, extractor, estudio, fecha1, TurnoStatus.DISPONIBLE, "");
        TurnoEntity turno2 = new TurnoEntity(2L, paciente, extractor, estudio, fecha2, TurnoStatus.DISPONIBLE, "");
        List<TurnoEntity> turnos = List.of(turno1, turno2);

        when(turnoRepository.findTurnoEntitiesByExtractorId(extractor.getId())).thenReturn(turnos);

        List<TurnoDTO> result = turnoService.getAllTurnosByExtractor(extractor.getId());

        assertNotNull(result);
        assertEquals(turnos.size(), result.size());
    }

    @Test
    void getAllTurnosByPaciente() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        LocalDateTime fecha2 = LocalDateTime.of(2025,2,2,10,15,0);
        PacienteEntity paciente = new PacienteEntity(1L, "Marcos Casper", LocalDate.of(1980, 10, 2));
        ExtractorEntity extractor = new ExtractorEntity(1L, "Laura Perez", "ENF1234");
        EstudioEntity estudio = new EstudioEntity(1L, "Análisis de sangre", "Requiere ayuno");
        TurnoEntity turno1 = new TurnoEntity(1L, paciente, extractor, estudio, fecha1, TurnoStatus.DISPONIBLE, "");
        TurnoEntity turno2 = new TurnoEntity(2L, paciente, extractor, estudio, fecha2, TurnoStatus.DISPONIBLE, "");
        List<TurnoEntity> turnos = List.of(turno1, turno2);

        when(turnoRepository.findTurnoEntitiesByPacienteId(paciente.getId())).thenReturn(turnos);

        List<TurnoDTO> result = turnoService.getAllTurnosByPaciente(paciente.getId());

        assertNotNull(result);
        assertEquals(turnos.size(), result.size());
    }

    @Test
    void getAllTurnosByEstudio() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        LocalDateTime fecha2 = LocalDateTime.of(2025,2,2,10,15,0);
        PacienteEntity paciente = new PacienteEntity(1L, "Marcos Casper", LocalDate.of(1980, 10, 2));
        ExtractorEntity extractor = new ExtractorEntity(1L, "Laura Perez", "ENF1234");
        EstudioEntity estudio = new EstudioEntity(1L, "Análisis de sangre", "Requiere ayuno");
        TurnoEntity turno1 = new TurnoEntity(1L, paciente, extractor, estudio, fecha1, TurnoStatus.DISPONIBLE, "");
        TurnoEntity turno2 = new TurnoEntity(2L, paciente, extractor, estudio, fecha2, TurnoStatus.DISPONIBLE, "");
        List<TurnoEntity> turnos = List.of(turno1, turno2);

        when(turnoRepository.findTurnoEntitiesByEstudioId(estudio.getId())).thenReturn(turnos);

        List<TurnoDTO> result = turnoService.getAllTurnosByEstudio(estudio.getId());

        assertNotNull(result);
        assertEquals(turnos.size(), result.size());
    }

    @Test
    void getAllTurnosByFechaHora() {
        LocalDateTime fecha1 = LocalDateTime.of(2025,2,2,10,0,0);
        LocalDateTime fecha2 = LocalDateTime.of(2025,2,2,10,15,0);
        PacienteEntity paciente = new PacienteEntity(1L, "Marcos Casper", LocalDate.of(1980, 10, 2));
        ExtractorEntity extractor = new ExtractorEntity(1L, "Laura Perez", "ENF1234");
        EstudioEntity estudio = new EstudioEntity(1L, "Análisis de sangre", "Requiere ayuno");
        TurnoEntity turno1 = new TurnoEntity(1L, paciente, extractor, estudio, fecha1, TurnoStatus.DISPONIBLE, "");
        TurnoEntity turno2 = new TurnoEntity(2L, paciente, extractor, estudio, fecha2, TurnoStatus.DISPONIBLE, "");
        List<TurnoEntity> turnos = List.of(turno1, turno2);

        LocalDate fecha = fecha1.toLocalDate();

        when(turnoRepository.findTurnoEntitiesByFecha(fecha)).thenReturn(turnos);

        List<TurnoDTO> result = turnoService.getAllTurnosByFecha(fecha);

        assertNotNull(result);
        assertEquals(turnos.size(), result.size());
    }

    @Test
    void getAllTurnos() {
        PacienteEntity paciente = new PacienteEntity(1L, "Marcos Casper", LocalDate.of(1980, 10, 2));
        ExtractorEntity extractor = new ExtractorEntity(1L, "Laura Perez", "ENF1234");
        EstudioEntity estudio = new EstudioEntity(1L, "Análisis de sangre", "Requiere ayuno");
        TurnoEntity turno1 = new TurnoEntity(1L, paciente, extractor, estudio, LocalDateTime.now(), TurnoStatus.DISPONIBLE, "");
        TurnoEntity turno2 = new TurnoEntity(2L, paciente, extractor, estudio, LocalDateTime.now(), TurnoStatus.DISPONIBLE, "");
        List<TurnoEntity> turnos = List.of(turno1, turno2);

        when(turnoRepository.findAll()).thenReturn(turnos);

        List<TurnoDTO> turnosDTO = turnoService.getAllTurnos();

        assertNotNull(turnosDTO);
        assertEquals(2, turnosDTO.size());
        assertEquals(turno1.getPaciente().getId(), turnosDTO.get(0).getPaciente().getId());

    }

}
