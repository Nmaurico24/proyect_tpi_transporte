package ar.edu.utnfc.backend.ms_logistica.dto;

import java.util.UUID;

public record SolicitudContenedorDTO(
    UUID id,
    UUID solicitudId,
    UUID contenedorId
) {}