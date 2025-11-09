package ar.edu.utnfc.backend.ms_recursos.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CamionDTO {
    private UUID id;
    private String adminio;
    private String nombre;
    private String telefono;
    private BigDecimal capPesoKg;
    private BigDecimal capVolumeM3;
    private BigDecimal costoOperacionHora;
    private Boolean lsActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
