package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DepositoCreateDTO(
    @NotBlank String nombre,
    @NotBlank String direccion,
    @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal lat,
    @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal lng,
    @NotNull @Positive BigDecimal costoDiario,
    Boolean lsActive
) {}