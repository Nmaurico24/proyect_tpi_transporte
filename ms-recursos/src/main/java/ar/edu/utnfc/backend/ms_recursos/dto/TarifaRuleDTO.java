package ar.edu.utnfc.backend.ms_recursos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_recursos.model.Moneda;
import ar.edu.utnfc.backend.ms_recursos.model.UnidadTarifa;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TarifaRuleDTO(
    @NotBlank String description,
    @NotNull UnidadTarifa unidad,
    @NotNull @Positive BigDecimal valor,
    @NotNull Moneda moneda,
    @NotNull @Future LocalDate vigenciaHasta ,
    List<UUID> camionesIds, // Para alcance a camiones específicos
    List<UUID> depositosIds, // Para alcance a depósitos específicos
    Integer version
) {}