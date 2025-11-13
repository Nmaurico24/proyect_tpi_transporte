package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;

public record DashboardSolicitudesDTO(
    Long totalSolicitudes,
    Long solicitudesPendientes,
    Long solicitudesEnCurso,
    Long solicitudesCompletadas,
    BigDecimal costoTotalEstimado,
    BigDecimal costoTotalReal
) {}