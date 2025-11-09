package ar.edu.utnfc.backend.ms_logistica.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class SolicitudDTO {
    private UUID id;
    private UUID clienteId;
    private EstadoSolicitud estado;
    private Integer prioridad;
    private String origenDireccion;
    private BigDecimal origenLat;
    private BigDecimal origenLng;
    private String destinoDireccion;
    private BigDecimal destinoLat;
    private BigDecimal destinoLng;
    private BigDecimal costoEstimado;
    private BigDecimal costoFinal;
    private BigDecimal distanciaEstimadaKm;
    private Integer duracionEstimadaMin;
    private Integer duracionRealMin;
    private String rutaRef;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
