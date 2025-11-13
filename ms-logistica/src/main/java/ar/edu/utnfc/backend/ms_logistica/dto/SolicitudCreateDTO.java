package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import java.util.UUID;

import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SolicitudCreateDTO(
    @NotNull UUID clienteId,
    @NotNull EstadoSolicitud estado,
    @Min(1) @Max(5) Integer prioridad,
    @NotBlank String origenDireccion,
    @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal origenLat,
    @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal origenLng,
    @NotBlank String destinoDireccion,
    @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal destinoLat,
    @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal destinoLng,
    @Positive BigDecimal costoEstimado,
    @Positive BigDecimal costoFinal,
    @Positive BigDecimal distanciaEstimadaKm,
    @Positive Integer duracionEstimadaMin,
    String rutaRef
) {}