package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_recursos.model.Moneda;

public record CostosFinalResponseDTO(
    UUID costoFinalId,
    String rutaRef,
    BigDecimal litros,
    Integer estadiasDias,
    BigDecimal totalFinal,
    Moneda moneda,
    Map<String, Object> desglose,
    LocalDateTime createEn
) {}
