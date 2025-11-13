package ar.edu.utnfc.backend.ms_logistica.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CostoEstimacionRequestDTO(
    @NotNull UUID solicitudId,
    @NotBlank String rutaRef,
    List<SegmentoLogisticaDTO> segmentos,
    Map<String, Object> parametrosAdicionales
) {}