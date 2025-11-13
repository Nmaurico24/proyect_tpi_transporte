package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_logistica.model.Moneda;

public record CostoEstimacionResponseDTO(
    UUID estimacionId,
    UUID solicitudId,
    String rutaRef,
    BigDecimal totalEstimado,
    Moneda moneda,
    Map<String, Object> desglose,
    LocalDateTime createEn
) {}