package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ContenedorDTO(
    UUID id,
    UUID clienteId,
    String etiqueta,
    BigDecimal pesoKg,
    BigDecimal volumenM3,
    String estado,
    Boolean isActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}