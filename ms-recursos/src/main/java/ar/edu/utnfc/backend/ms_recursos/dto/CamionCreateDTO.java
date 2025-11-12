package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CamionCreateDTO(
    @NotBlank String dominio,
    @NotBlank String nombre,
    String telefono,
    @NotNull @Positive BigDecimal capPesoKg,
    @NotNull @Positive BigDecimal capVolumeM3,
    @NotNull @Positive BigDecimal costoOperacionHora,
    Boolean lsActive
) {}
