package ar.edu.utnfc.backend.ms_recursos.dto;

import jakarta.validation.constraints.NotNull;

public record DisponibilidadRequestDTO(
    @NotNull Boolean disponible,
    String motivo
) {}