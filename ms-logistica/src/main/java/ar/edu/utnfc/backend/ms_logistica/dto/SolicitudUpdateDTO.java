package ar.edu.utnfc.backend.ms_logistica.dto;

import java.math.BigDecimal;
import ar.edu.utnfc.backend.ms_logistica.model.EstadoSolicitud;


public record SolicitudUpdateDTO(
    EstadoSolicitud estado,
    Integer prioridad,
    BigDecimal costoFinal,
    Integer duracionRealMin,
    String rutaRef
) {}