package ar.edu.utn.frc.tup.piii.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "disponibilidades_turnos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilidadTurnosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime horaInicio;

    private Integer duracionMinutos;
}
