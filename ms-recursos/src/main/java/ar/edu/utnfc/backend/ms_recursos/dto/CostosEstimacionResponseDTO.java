package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_recursos.model.Moneda;

public record CostosEstimacionResponseDTO(
    UUID estimacionId,
    UUID solicitudId,
    String rutaRef,
    BigDecimal totalEstimado,
    Moneda moneda,
    Map<String, Object> desglose,
    LocalDateTime createEn
) {}