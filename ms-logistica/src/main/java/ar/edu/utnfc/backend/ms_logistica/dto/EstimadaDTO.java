package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_logistica.model.Moneda;

public record EstimadaDTO(
    UUID id,
    UUID solicitudId,
    String fuente,
    String payloadJson,  // Mantener como String
    BigDecimal distanciaKm,
    Integer duracionMin,
    BigDecimal costoTotal,
    Moneda moneda,
    LocalDateTime createdAt
) {}