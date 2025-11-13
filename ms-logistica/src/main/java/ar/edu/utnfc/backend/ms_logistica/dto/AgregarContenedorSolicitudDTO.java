package ar.edu.utnfc.backend.ms_logistica.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AgregarContenedorSolicitudDTO(
    @NotNull UUID contenedorId
) {}
