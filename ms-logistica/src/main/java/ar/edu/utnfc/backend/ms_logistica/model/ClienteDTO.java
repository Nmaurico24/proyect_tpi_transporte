package ar.edu.utnfc.backend.ms_logistica.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ClienteDTO {
    private UUID id;
    private String numero;
    private String nombre;
    private String telefono;
    private String email;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
