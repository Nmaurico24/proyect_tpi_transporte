package ar.edu.utnfc.backend.ms_operaciones.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;


@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class RutaDTO {
    private UUID id;
    private String solicitudRef;
    private EstadoRuta estado; // Enum
    private BigDecimal distanciaTotalKm;
    private Integer duracionEstimadaMin;
    private BigDecimal costoEstimadoTotal;
    private BigDecimal costoFinalTotal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}