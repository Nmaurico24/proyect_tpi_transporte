package ar.edu.utnfc.backend.ms_recursos.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class TarifaDTO {
    private UUID id;
    private String description;
    private UnidadTarifa unidad; // Asumo que UnidadTarifa es un enum
    private BigDecimal valor;
    private Moneda moneda; // Asumo que Moneda es un enum
    private LocalDate vigenciaBasta;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}