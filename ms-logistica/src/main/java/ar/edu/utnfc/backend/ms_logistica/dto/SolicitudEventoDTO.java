package ar.edu.utnfc.backend.ms_logistica.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitudEventoDTO(
    UUID id,
    UUID solicitudId,
    String estado,
    String detalle,
    String actor,
    LocalDateTime createdAt
) {}
