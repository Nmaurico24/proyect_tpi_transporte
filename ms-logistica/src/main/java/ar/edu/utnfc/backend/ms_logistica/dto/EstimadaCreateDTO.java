package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_logistica.model.Moneda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EstimadaCreateDTO(
    @NotNull UUID solicitudId,
    @NotBlank String fuente,
    String payloadJson,  // Mantener como String
    @Positive BigDecimal distanciaKm,
    @Positive Integer duracionMin,
    @NotNull @Positive BigDecimal costoTotal,
    Moneda moneda
) {}