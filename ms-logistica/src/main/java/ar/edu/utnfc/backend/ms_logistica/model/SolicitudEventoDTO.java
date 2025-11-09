package ar.edu.utnfc.backend.ms_logistica.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SolicitudEventoDTO {
    private UUID id;
    private UUID solicitudId;
    private String estado;
    private String detalle;
    private String actor;
    private LocalDateTime createdAt;
}
