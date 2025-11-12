package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;

public record CamionUpdateDTO(
    String dominio,
    String nombre,
    String telefono,
    @Positive BigDecimal capPesoKg,
    @Positive BigDecimal capVolumeM3,
    @Positive BigDecimal costoOperacionHora,
    Boolean lsActive
) {}