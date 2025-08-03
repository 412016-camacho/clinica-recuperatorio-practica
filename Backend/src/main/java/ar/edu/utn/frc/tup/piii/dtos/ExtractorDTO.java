package ar.edu.utn.frc.tup.piii.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractorDTO {

    private Long id;

    @NotNull
    @NotBlank
    @JsonProperty("nombre_completo")
    private String nombreCompleto;

    @NotNull
    @NotBlank
    private String matricula;
}
