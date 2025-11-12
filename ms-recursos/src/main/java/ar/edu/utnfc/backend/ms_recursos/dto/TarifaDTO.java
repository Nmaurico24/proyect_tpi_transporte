package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_recursos.model.Moneda;
import ar.edu.utnfc.backend.ms_recursos.model.UnidadTarifa;

public record TarifaDTO(
    UUID id,
    String description,
    UnidadTarifa unidad,
    BigDecimal valor,
    Moneda moneda,
    LocalDate vigenciaHasta ,
    Integer version,
    List<UUID> camionesAplicables,
    List<UUID> depositosAplicables,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}