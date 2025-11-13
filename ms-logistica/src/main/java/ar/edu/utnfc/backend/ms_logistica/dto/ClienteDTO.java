package ar.edu.utnfc.backend.ms_logistica.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClienteDTO(
    UUID id,
    String numero,
    String nombre,
    String telefono,
    String email,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}