package ar.edu.utnfc.backend.ms_recursos.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DisponibilidadResponseDTO(
    UUID camionId,
    Boolean disponible,
    String motivo,
    LocalDateTime actualizadoEn
) {}