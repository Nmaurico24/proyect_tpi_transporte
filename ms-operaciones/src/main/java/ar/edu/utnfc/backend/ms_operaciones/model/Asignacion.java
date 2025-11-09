package ar.edu.utnfc.backend.ms_operaciones.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Asignacion {
    private UUID id;
    private UUID tramo_id;
    private String camion_ref;
    private String asignado_por;
    private LocalDateTime asignado_en;
}
