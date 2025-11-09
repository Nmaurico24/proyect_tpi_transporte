package ar.edu.utnfc.backend.ms_operaciones.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class TramoDTO {
    private UUID id;
    private UUID rutaId;
    private Integer orden;
    private TipoTramo stop; // Enum
    private EstadoTramo estado; // Enum
    private String origenTipo;
    private String origenRef;
    private BigDecimal origenLat;
    private BigDecimal origenLng;
    private String origenDir;
    private String destinoTipo;
    private String destinoRef;
    private BigDecimal destinoLat;
    private BigDecimal destinoLng;
    private String destinoDir;
    private BigDecimal distanciaKm;
    private Integer duracionEstimadaMin;
    private Integer duracionRealMin;
    private BigDecimal costoEstimado;
    private BigDecimal costoFinal;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
