package ar.edu.utnfc.backend.ms_logistica.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class ContenedorDTO {
    private UUID id;
    private UUID clienteId;
    private String etiqueta;
    private BigDecimal pesoKg;
    private BigDecimal volumenM3;
    private String estado;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
