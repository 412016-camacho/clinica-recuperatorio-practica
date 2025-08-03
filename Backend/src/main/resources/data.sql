INSERT INTO estudios (id, nombre_estudio, descripcion)
VALUES (1, 'Análisis de Sangre', 'Requiere ayuno'),
       (2, 'Análisis de Orina', 'Recolectar muestra en ayunas'),
       (3, 'Perfil Lipídico', 'No consumir grasas 12 horas antes');

INSERT INTO pacientes (id, nombre_completo, fecha_nacimiento)
VALUES (1, 'Lucía Romero', '1990-04-03'),
       (2, 'Juan Fernández', '1985-09-12');

INSERT INTO extractores (id, nombre_completo, matricula)
VALUES (1, 'Laura Pérez', 'EXT1234'),
       (2, 'Carlos Méndez', 'EXT5678'),
       (3, 'Andrea López', 'EXT9012');

INSERT INTO disponibilidades_turnos (id, hora_inicio, duracion_minutos)
VALUES (1, '07:00:00', 15),
       (2, '07:15:00', 15),
       (3, '07:30:00', 15),
       (4, '07:45:00', 15),
       (5, '08:00:00', 15),
       (6, '08:15:00', 15),
       (7, '08:30:00', 15),
       (8, '08:45:00', 15),
       (9, '09:00:00', 15),
       (10, '09:15:00', 15),
       (11, '09:30:00', 15),
       (12, '09:45:00', 15),
       (13, '10:00:00', 15),
       (14, '10:15:00', 15),
       (15, '10:30:00', 15),
       (16, '10:45:00', 15);

INSERT INTO turnos (id, fecha_hora, observaciones, paciente_id, extractor_id, estudio_id, status)
VALUES (100, '2023-01-01T10:45:00', 'Consulta general',  1,1,1, 'DISPONIBLE'),
       (101, '2023-01-02T10:45:00', 'Control pediátrico', 2,2,2, 'OCUPADO');