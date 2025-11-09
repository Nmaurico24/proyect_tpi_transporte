package ar.edu.utnfc.backend.ms_operaciones.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class TrackingEventoDTO {
    private UUID id;
    private UUID tramoId;
    private String tipo;
    private String actor;
    private BigDecimal lat;
    private BigDecimal lng;
    private String descripcion;
    private LocalDateTime registradoEn;
}
