package ar.edu.utnfc.backend.ms_recursos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class DepositoDTO {
    private UUID id;
    private String nombre;
    private String direccion;
    private BigDecimal lat;
    private BigDecimal lng;
    private BigDecimal costoDiario;
    private Boolean lsActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
