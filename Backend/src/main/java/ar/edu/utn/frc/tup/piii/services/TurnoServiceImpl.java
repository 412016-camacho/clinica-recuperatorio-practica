package ar.edu.utn.frc.tup.piii.services;

import ar.edu.utn.frc.tup.piii.dtos.*;
import ar.edu.utn.frc.tup.piii.entities.*;
import ar.edu.utn.frc.tup.piii.repositories.DisponibilidadTurnosRepository;
import ar.edu.utn.frc.tup.piii.repositories.TurnoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;

    private final ModelMapper modelMapper;

    private final ExtractorService extractorService;

    private final PacienteService pacienteService;

    private final EstudioService estudioService;

    private final DisponibilidadTurnosRepository disponibilidadTurnosRepository;

    @Override
    public TurnoDTO createTurno(NewTurnoDTO newTurnoDTO) {
        // TODO: Implementar la lógica para agendar un turno
        EstudioDTO estudioById = estudioService.getEstudioById(newTurnoDTO.getEstudioId());
        ExtractorDTO extractorById = extractorService.getExtractorById(newTurnoDTO.getExtractorId());
        PacienteDTO pacienteById = pacienteService.getPacienteById(newTurnoDTO.getPacienteId());
        if(estudioById == null || extractorById == null || pacienteById == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Extractor, Paciente o Estudio no existen");
        if(puedeAgendarTurno(newTurnoDTO)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paciente ya tiene un turno agendado para el día indicado");
        if(masDeUnTurno(newTurnoDTO)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un turno para el extractor en esa fecha y hora");
        if(disponible(newTurnoDTO)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay turnos disponibles en ese horario para ese extractor");

        TurnoEntity turnoEntity = new TurnoEntity();
        PacienteDTO paciente = pacienteService.getPacienteById(newTurnoDTO.getPacienteId());
        PacienteEntity pacienteEntity = modelMapper.map(paciente, PacienteEntity.class);
        EstudioDTO estudio = estudioService.getEstudioById(newTurnoDTO.getEstudioId());
        EstudioEntity estudioEntity = modelMapper.map(estudio, EstudioEntity.class);
        ExtractorDTO extractor = extractorService.getExtractorById(newTurnoDTO.getExtractorId());
        ExtractorEntity extractorEntity = modelMapper.map(extractor, ExtractorEntity.class);

        turnoEntity.setPaciente(pacienteEntity);
        turnoEntity.setEstudio(estudioEntity);
        turnoEntity.setExtractor(extractorEntity);
        turnoEntity.setFechaHora(newTurnoDTO.getFechaHora());
        turnoEntity.setObservaciones(newTurnoDTO.getObservaciones());

        TurnoEntity turnoSaved = turnoRepository.save(turnoEntity);

        return modelMapper.map(turnoSaved, new  TypeToken<TurnoDTO>() {}.getType());
    }

    private boolean disponible(NewTurnoDTO newTurnoDTO) {
        for(TurnoEntity turnoEntity : turnoRepository.findAll()) {
            if(turnoEntity.getStatus().equals(TurnoStatus.DISPONIBLE) && turnoEntity.getFechaHora().equals(newTurnoDTO.getFechaHora()) && turnoEntity.getPaciente() == null){
             return true;
            }
        }
        return false;
    }

    private boolean masDeUnTurno(NewTurnoDTO newTurnoDTO) {
        return this.getAllTurnosByExtractor(newTurnoDTO.getExtractorId()).stream().anyMatch(
                t-> t.getFechaHora().equals(newTurnoDTO.getFechaHora()) && t.getPaciente() == null);
    }

    private boolean puedeAgendarTurno(NewTurnoDTO newTurnoDTO) {
        return this.getAllTurnosByPaciente(newTurnoDTO.getPacienteId()).stream().anyMatch(
                t-> t.getFechaHora().equals(newTurnoDTO.getFechaHora()));
    }

    @Override
    public List<TurnoDTO> getAllTurnosByExtractor(Long extractorId) {
        List<TurnoEntity> turnoEntities = turnoRepository.findTurnoEntitiesByExtractorId(extractorId);
        return modelMapper.map(turnoEntities, new TypeToken<List<TurnoDTO>>() {}.getType());
    }

    @Override
    public List<TurnoDTO> getAllTurnosByPaciente(Long pacienteId) {
        List<TurnoEntity> turnoEntities = turnoRepository.findTurnoEntitiesByPacienteId(pacienteId);
        return modelMapper.map(turnoEntities, new TypeToken<List<TurnoDTO>>() {}.getType());
    }

    @Override
    public List<TurnoDTO> getAllTurnosByEstudio(Long estudioId) {
        List<TurnoEntity> turnoEntities = turnoRepository.findTurnoEntitiesByEstudioId(estudioId);
        return modelMapper.map(turnoEntities, new TypeToken<List<TurnoDTO>>() {}.getType());
    }

    @Override
    public List<TurnoDTO> getAllTurnosByFecha(LocalDate fecha) {
        List<TurnoEntity> turnoEntities = turnoRepository.findTurnoEntitiesByFecha(fecha);
        return modelMapper.map(turnoEntities, new TypeToken<List<TurnoDTO>>() {}.getType());
    }

    @Override
    public List<TurnoDTO> getAllTurnos() {
        List<TurnoEntity> turnoEntities = turnoRepository.findAll();
        return modelMapper.map(turnoEntities, new TypeToken<List<TurnoDTO>>() {}.getType());
    }

    /*
        HELP:
        * Para componer LocalDateTime a partir de LocalDate y LocalTime, se puede usar el metodo atDate() de LocalTime.
          Ejemplo: LocalDateTime fechaHora = disponibilidad.getHoraInicio().atDate(fecha);
        * Se puede obtener la lista de intervalos de turnos desde la base de datos usando el repositorio DisponibilidadTurnosRepository.
          Ejemplo: List<DisponibilidadTurnosEntity> disponibilidades = disponibilidadTurnosRepository.findAll();
     */
    @Override
    public List<TurnoDTO> programarTurnos(LocalDate fecha) {
        // TODO: Implementar la lógica para programar turnos
        if(!turnoRepository.findTurnoEntitiesByFecha(fecha).isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los turnos ya están programados para la fecha indicada");

        List<ExtractorDTO> extractores = extractorService.getAllExtractores();

        LocalTime horaInicio = LocalTime.of(7, 0);
        LocalTime horaFin = LocalTime.of(10, 45);
        int intervalo = 15;

        List<TurnoDTO> resultado = new ArrayList<>();

        //Por cada extractor, se deben programar turnos cada 15 minutos entre las 07:00 y las 10:45 (inclusive).
        for(ExtractorDTO ext : extractores){
            ExtractorEntity extractor = modelMapper.map(ext, ExtractorEntity.class);
            LocalTime horaActual = horaInicio;
            while(!horaActual.isAfter(horaFin)){
                TurnoEntity turnoEntity = new TurnoEntity();
                turnoEntity.setExtractor(extractor);
                turnoEntity.setFechaHora(LocalDateTime.of(fecha, horaActual));
                turnoEntity.setPaciente(null);
                turnoEntity.setEstudio(null);
                turnoEntity.setObservaciones(null);
                turnoEntity.setStatus(TurnoStatus.DISPONIBLE);

                TurnoEntity saved = turnoRepository.save(turnoEntity);
                TurnoDTO dto = modelMapper.map(saved, new TypeToken<TurnoDTO>() {}.getType());
                resultado.add(dto);

                horaActual = horaActual.plusMinutes(intervalo);
            }
        }
      return resultado;
    }
}
