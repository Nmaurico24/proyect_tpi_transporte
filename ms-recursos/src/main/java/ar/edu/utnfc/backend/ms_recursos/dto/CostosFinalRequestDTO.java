package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CostosFinalRequestDTO(
    @NotBlank String rutaRef,
    @NotNull @Positive BigDecimal litrosConsumidos,
    @NotNull @Positive Integer estadiasDias,
    Map<String, Object> costosAdicionales
) {}