package ar.edu.utn.frc.tup.piii.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewTurnoDTO {

    @JsonProperty("paciente_id")
    @NotNull
    private Long pacienteId;

    @JsonProperty("estudio_id")
    @NotNull
    private Long estudioId;

    @JsonProperty("extractor_id")
    @NotNull
    private Long extractorId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull
    private LocalDateTime fechaHora;

    private String observaciones;
}
