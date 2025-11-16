package ar.edu.utnfc.backend.ms_operaciones.dto.tracking;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TrackingLineaTiempoResponse {
    private String solicitudRef;
    private String ultimoEstado;
    private String ultimoEvento;
    private Instant actualizadoEn;
}
