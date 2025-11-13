package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ContenedorCreateDTO(
    @NotNull UUID clienteId,
    @NotBlank String etiqueta,
    @NotNull @Positive BigDecimal pesoKg,
    @NotNull @Positive BigDecimal volumenM3,
    String estado,
    Boolean isActive
) {}