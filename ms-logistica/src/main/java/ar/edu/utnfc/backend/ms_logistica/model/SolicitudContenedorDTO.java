package ar.edu.utnfc.backend.ms_logistica.model;

import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SolicitudContenedorDTO {
    private UUID id;
    private UUID solicitudId;
    private UUID contenedorId;
}
