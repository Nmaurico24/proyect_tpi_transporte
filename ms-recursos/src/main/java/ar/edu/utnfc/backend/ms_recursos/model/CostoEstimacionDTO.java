package ar.edu.utnfc.backend.ms_recursos.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder

public class CostoEstimacionDTO {
    private UUID id;
    private String solicitudRef;
    private String rutaPayLoad; // JSON como String
    private BigDecimal totalEstimado;
    private Moneda moneda;
    private String desgloseJson; // JSON como String
    private LocalDateTime createEn;
}