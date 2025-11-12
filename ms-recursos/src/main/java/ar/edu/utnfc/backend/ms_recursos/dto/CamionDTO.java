package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CamionDTO(
    UUID id,
    String dominio,
    String nombre,
    String telefono,
    BigDecimal capPesoKg,
    BigDecimal capVolumeM3,
    BigDecimal costoOperacionHora,
    Boolean lsActive,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}