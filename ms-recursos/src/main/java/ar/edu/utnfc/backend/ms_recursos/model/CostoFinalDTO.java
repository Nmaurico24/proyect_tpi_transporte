package ar.edu.utnfc.backend.ms_recursos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class CostoFinalDTO {
    private UUID id;
    private String rutaRef;
    private BigDecimal litros;
    private Integer etxiolesDias;
    private BigDecimal totalFinal;
    private Moneda moneda;
    private String desgloseJson; // JSON como String
    private LocalDateTime createEn;
}