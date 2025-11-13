package ar.edu.utnfc.backend.ms_logistica.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitudEventoCreateDTO(
    @NotNull UUID solicitudId,
    @NotBlank String estado,
    String detalle,
    String actor
) {}