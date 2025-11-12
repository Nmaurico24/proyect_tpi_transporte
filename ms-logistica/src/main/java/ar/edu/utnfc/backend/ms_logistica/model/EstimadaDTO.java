package ar.edu.utnfc.backend.ms_logistica.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class EstimadaDTO {
    private UUID id;
    private UUID solicitudId;
    private String fuente;
    private String payloadJson;
    private BigDecimal distanciaKm;
    private Integer duracionMin;
    private BigDecimal costoTotal;
    private Moneda moneda;
    private LocalDateTime createdAt;
}
