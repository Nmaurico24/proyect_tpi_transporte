package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DepositoDTO(
    UUID id,
    String nombre,
    String direccion,
    BigDecimal lat,
    BigDecimal lng,
    BigDecimal costoDiario,
    Boolean lsActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}