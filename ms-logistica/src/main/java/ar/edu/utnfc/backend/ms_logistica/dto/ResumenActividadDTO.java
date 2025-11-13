package ar.edu.utnfc.backend.ms_logistica.dto;

import java.util.List;

public record ResumenActividadDTO(
    Long totalEventos,
    Long eventosSistema,
    Long eventosUsuario,
    String estadoMasFrecuente,
    List<SolicitudEventoDTO> ultimosEventos
) {}