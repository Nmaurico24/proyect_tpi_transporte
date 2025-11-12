package ar.edu.utnfc.backend.ms_recursos.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CostosEstimacionRequestDTO(
    @NotNull UUID solicitudId,
    @NotBlank String rutaRef,
    List<SegmentoCostoDTO> segmentos,
    Map<String, Object> parametrosAdicionales
) {}